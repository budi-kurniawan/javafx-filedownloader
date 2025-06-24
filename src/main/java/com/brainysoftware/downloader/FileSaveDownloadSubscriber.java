package com.brainysoftware.downloader;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileSaveDownloadSubscriber implements HttpResponse.BodySubscriber<Path> {

    private final CompletableFuture<Path> result = new CompletableFuture<>();
    private final Path outputPath;
    private AtomicBoolean cancelled;

    private Flow.Subscription subscription;
    private FileChannel fileChannel;
    private long contentLength;
    private long bytesDownloaded = 0;
    private LocalDateTime startTime;
    
    public FileSaveDownloadSubscriber(Path outputPath, long contentLength, AtomicBoolean cancelled) {
        this.startTime = LocalDateTime.now();
        this.outputPath = outputPath;
        this.contentLength = contentLength;
        try {
            this.fileChannel = FileChannel.open(outputPath,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open file for writing", e);
        }
        this.cancelled = cancelled;
    }

    @Override
    public CompletionStage<Path> getBody() {
        System.out.println("=========getBody() called");
        return result;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // request first chunk
    }

    @Override
    public void onNext(List<ByteBuffer> items) {
        if (cancelled.get()) {
            onComplete();
            try {
                Files.delete(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            for (ByteBuffer buffer : items) {
                int len = buffer.remaining();
                bytesDownloaded += len;
//                byte[] data = new byte[len];
//                buffer.get(data);
                fileChannel.write(buffer);
            }
            subscription.request(1); // request next chunk
        } catch (IOException e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        try {
            this.fileChannel.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        result.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete-----");
        try {
            fileChannel.close();
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
