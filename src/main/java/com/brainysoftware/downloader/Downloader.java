package com.brainysoftware.downloader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.brainysoftware.downloader.listener.DownloadListener;

/**
 * @author Budi Kurniawan (https://brainysoftware.com)
 */
public class Downloader {
    private DownloadListener downloadListener;
    private AtomicBoolean cancelFlag;
    
    public Downloader() {
        this.cancelFlag = new AtomicBoolean(false);
    }
    
    public Downloader(AtomicBoolean cancelFlag) {
        if (cancelFlag == null) {
            this.cancelFlag = new AtomicBoolean(false);
        } else {
            this.cancelFlag = cancelFlag;
        }
    }
    
    public List<CompletableFuture<HttpResponse<Path>>> download(List<DownloadRequest> downloadRequests) {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        List<CompletableFuture<HttpResponse<Path>>> futures = new ArrayList<>();
        int length = downloadRequests.size();
        for (int i = 0; i < length; i++) {
            DownloadRequest downloadRequest = downloadRequests.get(i);
            URI uri = URI.create(downloadRequest.uri());
            HttpRequest request = HttpRequest.newBuilder(uri).build();
            final int index = i;
            CompletableFuture<HttpResponse<Path>> future = httpClient.sendAsync(request,
                    responseInfo -> {
                        HttpHeaders headers = responseInfo.headers();
                        long contentLength = headers.firstValueAsLong("Content-Length")
                                .orElse(-1L);
                        if (contentLength == -1L) {
                            contentLength = getContentLength(httpClient, uri);
                        }
                        // if we just pass i as the first argument, we'll get a
                        // "i defined in an enclosing scope must be final or effectively final"
                        return new FileSaveDownloadSubscriber(index, contentLength, cancelFlag,
                                new DownloadRequest(downloadRequest.uri(), downloadRequest.savePath()), downloadListener);
                    });
            futures.add(future);
        }
        return futures;
    }
    
    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
    
    private static long getContentLength(HttpClient client, URI uri) {
        HttpRequest headRequest = HttpRequest.newBuilder()
                .uri(uri)
                .HEAD()
                .build();
        try {
            HttpResponse<Void> response = client.send(headRequest, HttpResponse.BodyHandlers.discarding());
            return response.headers().firstValueAsLong("Content-Length").orElse(-1L);
        } catch (IOException | InterruptedException e) {
            return -1L;
        }
    }
}
