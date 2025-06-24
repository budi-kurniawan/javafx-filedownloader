package com.brainysoftware.downloader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Downloader {
    public void download(List<DownloadSourceDestination> downloadSourcesDestinations) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        long t1 = System.currentTimeMillis();
        AtomicBoolean cancelled = new AtomicBoolean(false);
        List<CompletableFuture<HttpResponse<Path>>> futures = new ArrayList<>();
        for (DownloadSourceDestination sourceDestination: downloadSourcesDestinations) {
            URI uri = URI.create(sourceDestination.uri());
            Path savePath = sourceDestination.savePath();
            HttpRequest request = HttpRequest.newBuilder(uri).build();
            CompletableFuture<HttpResponse<Path>> response = client.sendAsync(request,
                    responseInfo -> {
                        HttpHeaders headers = responseInfo.headers();
                        // try to get Content-Length from header
                        long contentLength = headers.firstValueAsLong("Content-Length").orElse(-1L);
                        if (contentLength == -1L) {
                            // No Content-Length header found, retrieve explicitly
                            try {
                                contentLength = getContentLength(client, uri);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return new FileSaveDownloadSubscriber(savePath, contentLength,
                            cancelled);
                    });
            futures.add(response);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
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
//                        "https://florentineeyewear.com.au/",
                        "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/encoder_model.onnx?download=true", 
                        Paths.get("D://downloads/encoder_model.onnx"))
                ,
                new DownloadSourceDestination(
//                        "https://florentineeyewear.com.au/",
                        "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/decoder_model.onnx?download=true", 
                        Paths.get("D://downloads/decoder_model.onnx"))
                );
        Downloader downloader = new Downloader();
        downloader.download(list);
    }
}
