package com.brainysoftware.downloader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Downloader {
    public void download(List<DownloadSourceDestination> downloadSourcesDestinations) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        long t1 = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(downloadSourcesDestinations.size());
        List<CompletableFuture<HttpResponse<Path>>> responses = new ArrayList<>();
        for (DownloadSourceDestination sourceDestination: downloadSourcesDestinations) {
            URI uri = URI.create(sourceDestination.uri());
            Path savePath = sourceDestination.savePath();
            try {
                long contentLength = getContentLength(client, uri);
                HttpRequest request = HttpRequest.newBuilder(uri).build();
                CompletableFuture<HttpResponse<Path>> response = client.sendAsync(request,
                        responseInfo -> new FileSaveDownloadSubscriber(savePath, contentLength));
                executor.submit(() -> {
                    try {
                        System.out.println("submit get.body");
                        response.get().body();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        CompletableFuture.allOf(responses.toArray(new CompletableFuture[0])).join();
        long t2 = System.currentTimeMillis();
        System.out.println("time taken:" + (t2 - t1));
    }
    
    private static long getContentLength(HttpClient client, URI uri) throws IOException, InterruptedException {
        HttpRequest headRequest = HttpRequest.newBuilder()
                .uri(uri)
                .HEAD()
                .build();
        HttpResponse<Void> response = client.send(headRequest, HttpResponse.BodyHandlers.discarding());
        return response.headers().firstValueAsLong("Content-Length").orElse(-1L);
    }


    public static void main(String[] args) {
        List<DownloadSourceDestination> list = List.of(
                new DownloadSourceDestination(
                        "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/encoder_model.onnx?download=true", 
                        Paths.get("D://downloads/encoder_model.onnx")),
                new DownloadSourceDestination(
                        "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/decoder_model.onnx?download=true", 
                        Paths.get("D://downloads/decoder_model.onnx"))
                );
        Downloader downloader = new Downloader();
        downloader.download(list);
    }
}
