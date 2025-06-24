package com.brainysoftware.downloader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Downloader {
    public record UriAndSavePath(String uri, Path savePath) {}
    
    public void download(List<UriAndSavePath> urisAndSavePaths) {
        UriAndSavePath uriAndSavePath = urisAndSavePaths.get(0);
        URI uri = URI.create(uriAndSavePath.uri());
        Path savePath = uriAndSavePath.savePath();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder(uri).build();
        CompletableFuture<HttpResponse<Path>> response = client.sendAsync(request,
                responseInfo -> new FileSaveDownloadSubscriber(savePath));
        try {
            response.get().body();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) {
        List<UriAndSavePath> list = List.of(
                new UriAndSavePath(
                        "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/encoder_model.onnx?download=true", 
                        Paths.get("D://downloads/encoder_model.onnx")));
        Downloader downloader = new Downloader();
        downloader.download(list);
    }
}
