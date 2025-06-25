package com.brainysoftware.downloader.ui;

import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import com.brainysoftware.downloader.DownloadRequest;
import com.brainysoftware.downloader.Downloader;
import com.brainysoftware.downloader.listener.DownloadListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;

/**
 * 
 * @author Budi Kurniawan (https://brainysoftware.com)
 * 
 */

public class MenuController implements Initializable {

	@FXML
	private MenuBar menuBar;

	@FXML
	private void handleAboutAction(final ActionEvent event) {
		GUIManager.getInstance().showMessageDialog("Downloader 0.1", "Author: Budi Kurniawan (https://brainysoftware.com)");
	}

	@FXML
    private void handleDownload(final ActionEvent event) {
	    String[] uris = {
//              "https://theage.com.au",
              "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/encoder_model.onnx?download=true", 
              "https://cnn.com",
//            "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/decoder_model.onnx?download=true", 
	    };
	    
	    
        AtomicBoolean cancelled = new AtomicBoolean(false);
        MultiProgressDialog dialog = new MultiProgressDialog("Download Progress",
                uris.length);
        DownloadListener listener = new UIDownloadListener(dialog); // share among downloads
        List<DownloadRequest> downloadRequests = List.of(
                new DownloadRequest(
                        0,
//                        "https://theage.com.au",
                        "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/encoder_model.onnx?download=true", 
                        Paths.get("D://downloads/encoder_model.onnx"),
                        cancelled,
                        listener)
                ,
                new DownloadRequest(
                        1,
//                        "https://cnn.com",
                        "https://huggingface.co/budi2020/bart-large-cnn-onnx/resolve/main/decoder_model.onnx?download=true", 
                        Paths.get("D://downloads/decoder_model.onnx"),
                        cancelled,
                        listener)
                );
        dialog.setOnCancel(() -> cancelled.set(true));
        dialog.show();
        
        long t1 = System.currentTimeMillis();
        Downloader downloader = new Downloader();
        downloader.download(downloadRequests);
        long t2 = System.currentTimeMillis();
        System.out.println("time taken:" + (t2 - t1));


    }

	@FXML
	private void handleExit(final ActionEvent event) {
		System.exit(0);
	}

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
	    GUIManager.Builder.build(menuBar);
		menuBar.setFocusTraversable(true);
		System.out.println("Initialize MenuController...");
	}
	
}