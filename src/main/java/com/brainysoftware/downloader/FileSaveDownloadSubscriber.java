package com.brainysoftware.downloader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public class FileSaveDownloadSubscriber implements HttpResponse.BodySubscriber<Path> {

    private final CompletableFuture<Path> result = new CompletableFuture<>();
    private final Path outputPath;

    private Flow.Subscription subscription;
    private OutputStream outputStream;
    private long contentLength;
    private long bytesDownloaded = 0;
    private LocalDateTime startTime;
    
    public FileSaveDownloadSubscriber(Path outputPath, long contentLength) {
        this.startTime = LocalDateTime.now();
        this.outputPath = outputPath;
        this.contentLength = contentLength;
        try {
            this.outputStream = Files.newOutputStream(outputPath,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open file for writing", e);
        }
    }

    @Override
    public CompletionStage<Path> getBody() {
        return result;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        
        subscription.request(1); // request first chunk
    }

    @Override
    public void onNext(List<ByteBuffer> items) {
        try {
            for (ByteBuffer buffer : items) {
                int len = buffer.remaining();
                bytesDownloaded += len;
                byte[] data = new byte[len];
                buffer.get(data);
                outputStream.write(data);
            }
            subscription.request(1); // request next chunk
        } catch (IOException e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        try {
            outputStream.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        result.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        try {
            outputStream.close();
            result.complete(outputPath);
        } catch (IOException e) {
            onError(e);
        }
        LocalDateTime finishTime = LocalDateTime.now();
        System.out.println("download of " + this.outputPath.toString());
        System.out.println("start " + startTime);
        System.out.println("finish " + finishTime);
    }
    
}
