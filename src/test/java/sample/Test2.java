package sample;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Test2 {
    public static void main(String[] args) throws Exception {
        List<URI> targets = Arrays.asList(
                new URI("https://postman-echo.com/get?foo1=bar1"),
                new URI("https://postman-echo.com/get?foo2=bar2")
        );

        HttpClient client = HttpClient.newHttpClient();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        int index = 0;
        for (URI target : targets) {
            int fileIndex = index++;  // unique index for each file
            HttpRequest request = HttpRequest.newBuilder(target).GET().build();

            CompletableFuture<Void> future = client
                    .sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                    .thenAccept(response -> {
                        Path path = Paths.get("D://downloads/response_" + fileIndex + ".txt");
                        try {
                            Files.write(path, response.body());
                            System.out.println("Saved response to " + path);
                        } catch (IOException e) {
                            System.err.println("Failed to save response: " + e.getMessage());
                        }
                    }).exceptionally(e -> {
                        System.err.println("Request failed: " + e.getMessage());
                        return null;
                    });

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println("All responses saved.");
    }
}
