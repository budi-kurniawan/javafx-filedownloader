package com.brainysoftware.downloader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public class FileSaveDownloadSubscriber implements HttpResponse.BodySubscriber<Path> {

    private final CompletableFuture<Path> result = new CompletableFuture<>();
    private final Path outputPath;

    private Flow.Subscription subscription;
    private OutputStream outputStream;
    private long bytesDownloaded = 0;
    
    private static int no = 2;

    public FileSaveDownloadSubscriber(Path outputPath) {
        this.outputPath = outputPath;
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
        System.out.println("onSubscribe");
        this.subscription = subscription;
        subscription.request(1); // request first chunk
    }

    @Override
    public void onNext(List<ByteBuffer> items) {
        System.out.println("onNext----- items.size:" + items.size());
        try {
            for (ByteBuffer buffer : items) {
                int len = buffer.remaining();
                bytesDownloaded += len;
                byte[] data = new byte[len];
                buffer.get(data);
                outputStream.write(data);
            }
            System.out.println("request again:" + no);
            //subscription.request(no++); // request next chunk
            subscription.request(1); // request next chunk
        } catch (IOException e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError");
        try {
            outputStream.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
            
        }
        result.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete");
        try {
            outputStream.close();
            result.complete(outputPath);
        } catch (IOException e) {
            onError(e);
        }
    }

}
