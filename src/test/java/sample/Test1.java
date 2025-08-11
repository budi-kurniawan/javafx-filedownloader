package sample;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Test1 {
    public static void main(String[] args) throws Exception {
        List<URI> targets = Arrays.asList(new URI("https://postman-echo.com/get?foo1=bar1"),
                new URI("https://postman-echo.com/get?foo2=bar2"));
        HttpClient client = HttpClient.newHttpClient();
        List<CompletableFuture<String>> futures = targets.stream()
                .map(target -> client
                        .sendAsync(HttpRequest.newBuilder(target).GET().build(), HttpResponse.BodyHandlers.ofString())
                        .thenApply(response -> response.body()))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenRun(() -> futures.forEach(f -> {
            try {
                System.out.println(f.get()); // f.get() blocks until this future completes
            } catch (Exception e) {
                System.err.println("Failed to get response: " + e.getMessage());
            }
        })).join(); // Wait for all to finish
    }
}
