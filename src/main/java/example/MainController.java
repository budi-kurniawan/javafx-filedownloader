package example;

import java.nio.file.Paths;
import java.util.List;

import com.brainysoftware.downloader.DownloadRequest;
import com.brainysoftware.downloader.ui.ConcurrentDownloadDialog;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;

/**
 * @author Budi Kurniawan (https://brainysoftware.com)
 */
public class MainController {

	@FXML
	private MenuBar menuBar;

	@FXML
	private void handleAboutAction(final ActionEvent event) {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setTitle("About this software");
        errorAlert.setHeaderText("Downloader 0.1");
        errorAlert.setContentText("Author: Budi Kurniawan (https://brainysoftware.com)");
        errorAlert.showAndWait();
	}

	@FXML
    private void handleDownload(final ActionEvent event) {
        List<DownloadRequest> downloadRequests = List.of(
                new DownloadRequest(
                        "https://huggingface.co/facebook/bart-large-cnn/resolve/main/pytorch_model.bin?download=true", 
                        Paths.get("D://downloads/pytorch_model.bin"))
                ,
                new DownloadRequest(
                        "https://huggingface.co/facebook/bart-large-cnn/resolve/main/rust_model.ot?download=true", 
                        Paths.get("D://downloads/rust_model.ot")),
                new DownloadRequest(
                        "https://huggingface.co/facebook/bart-large-cnn/resolve/main/tokenizer.json?download=true", 
                        Paths.get("D://downloads/tokenizer.json"))
                );
        ConcurrentDownloadDialog dialog = new ConcurrentDownloadDialog("Download Progress",
                downloadRequests);
        dialog.download();
    }

	@FXML
	private void handleExit(final ActionEvent event) {
	    Platform.exit();
	}

}