package com.brainysoftware.downloader.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.brainysoftware.downloader.DownloadRequest;
import com.brainysoftware.downloader.Downloader;
import com.brainysoftware.downloader.listener.DownloadListener;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Budi Kurniawan (https://brainysoftware.com)
 */
public class ConcurrentDownloadDialog {
    private final Stage stage;
    private final List<Label> labels = new ArrayList<>();
    private final List<Label> progressLabels = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();
    private Runnable onCancel = null;
    private boolean[] complete;
    private AtomicBoolean cancelled = new AtomicBoolean(false);
    private List<DownloadRequest> downloadRequests;
    
    public ConcurrentDownloadDialog(String title, List<DownloadRequest> downloadRequests) {
        this.downloadRequests = downloadRequests;
        stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        int count = downloadRequests.size();

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        complete = new boolean[count];
        
        for (DownloadRequest dr : downloadRequests) {
            VBox vBox = new VBox();
            vBox.setSpacing(8);
            Label label = new Label(dr.savePath().getFileName().toString());
            Label progressLabel = new Label("  (0%)");
            ProgressBar bar = new ProgressBar(/*init value=*/ 0);
            bar.setPrefWidth(300);
            bar.setStyle("-fx-accent:red");
            HBox hBox = new HBox(label, progressLabel);
            labels.add(label);
            progressLabels.add(progressLabel);
            progressBars.add(bar);
            
            vBox.getChildren().addAll(hBox, bar);
            VBox.setMargin(bar, new Insets(-5, 0, 0, 0));
            root.getChildren().add(vBox);
        }

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            stage.hide();
            if (onCancel != null) {
                onCancel.run();
            }
        });
        root.getChildren().add(cancelButton);
        stage.setScene(new Scene(root));
        this.setOnCancel(() -> cancelled.set(true));
    }

    public void show() {
        Platform.runLater(stage::show);
    }

    public void hide() {
        Platform.runLater(stage::hide);
    }

    public void setProgress(int index, double value) {
        if (index >= 0 && index < progressBars.size()) {
            Platform.runLater(() -> progressBars.get(index).setProgress(value));
            Platform.runLater(() -> 
                    progressLabels.get(index).setText("  (" + ((int) (100 * value)) + "%)"));
        }
    }

    public void setLabel(int index, String text) {
        if (index >= 0 && index < labels.size()) {
            Platform.runLater(() -> labels.get(index).setText(text));
        }
    }
    
    public void setComplete(int index) {
        int count = labels.size();
        complete[index] = true;
        if (index >= 0 && index < count) {
            Platform.runLater(() -> progressBars.get(index).setStyle("-fx-accent:green"));
            for (int i = 0; i < count; i++) {
                if (!complete[i]) {
                    return;
                }
            }
            Platform.runLater(() -> {
                showMessageDialog("Download Status", "File download(s) complete", "");
                Platform.runLater(stage::hide);
            });
        }
    }
    
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }
    
    public void showMessageDialog(String title, String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
    
    
    public void download() {
        DownloadListener listener = new UIDownloadListener(this); // share among downloads
        this.show();
        Downloader downloader = new Downloader();
        downloader.setDownloadListener(listener);
        downloader.download(downloadRequests);
    }

}
