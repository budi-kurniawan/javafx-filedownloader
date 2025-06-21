package sample;

import java.net.URI;
import java.net.http.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadFileWithProgress {

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/encoder_model.onnx?download=true");
        Path output = Paths.get("D://downloads/encoder_model.onnx");

        DownloadListener listener = new DownloadListener() {
            @Override
            public void onProgress(long bytesDownloaded) {
                System.out.println("Downloaded: " + bytesDownloaded + " bytes");
            }

            @Override
            public void onComplete(Path path) {
                System.out.println("Download complete: " + path);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Download failed: " + error.getMessage());
            }
        };

        HttpRequest request = HttpRequest.newBuilder(uri).build();

        HttpResponse<Path> response = client.send(request,
                responseInfo -> new StreamingFileDownloadSubscriber(output, listener));

        System.out.println("Saved to: " + response.body());
    }
}
