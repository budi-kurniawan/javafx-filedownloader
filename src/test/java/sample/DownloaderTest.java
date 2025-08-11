package sample;

import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.brainysoftware.downloader.DownloadRequest;
import com.brainysoftware.downloader.Downloader;

/**
 * @author Budi Kurniawan (https://brainysoftware.com)
 */
public class DownloaderTest {
    public static void main(String[] args) {
        System.out.println("Downloading ... this may take longer than 10 minutes");
        String[] uris = {
                "https://huggingface.co/facebook/bart-large-cnn/resolve/main/pytorch_model.bin?download=true",
                "https://huggingface.co/facebook/bart-large-cnn/resolve/main/rust_model.ot?download=true"
        };
        uris[0] = "https://theage.com.au";
        uris[1] = "https://cnn.com";
        List<DownloadRequest> downloadRequests = List.of(
                new DownloadRequest(uris[0], Paths.get("D://downloads/pytorch_model.bin")),
                new DownloadRequest(uris[1], Paths.get("D://downloads/rust_model.ot")));
        Downloader downloader = new Downloader();
        List<CompletableFuture<HttpResponse<Path>>> futures = downloader.download(downloadRequests);
        // need to join() otherwise this thread will complete before downloads are complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

}
