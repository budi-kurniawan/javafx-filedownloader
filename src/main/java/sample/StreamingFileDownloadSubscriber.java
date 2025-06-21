package sample;

import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public class StreamingFileDownloadSubscriber implements HttpResponse.BodySubscriber<Path> {

    private final CompletableFuture<Path> result = new CompletableFuture<>();
    private final Path outputPath;
    private final DownloadListener listener;

    private Flow.Subscription subscription;
    private OutputStream outputStream;
    private long bytesDownloaded = 0;

    public StreamingFileDownloadSubscriber(Path outputPath, DownloadListener listener) {
        this.outputPath = outputPath;
        this.listener = listener;
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
            listener.onProgress(bytesDownloaded);
            subscription.request(1); // request next chunk
        } catch (IOException e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        try {
            outputStream.close();
        } catch (IOException ignored) {}
        result.completeExceptionally(throwable);
        listener.onError(throwable);
    }

    @Override
    public void onComplete() {
        try {
            outputStream.close();
            result.complete(outputPath);
            listener.onComplete(outputPath);
        } catch (IOException e) {
            onError(e);
        }
    }

}
