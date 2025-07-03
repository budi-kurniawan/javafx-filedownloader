package com.brainysoftware.downloader.ui;

import java.util.ArrayList;
import java.util.List;

import com.brainysoftware.downloader.DownloadRequest;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MultiProgressDialog {
    private final Stage stage;
    private final List<Label> labels = new ArrayList<>();
    private final List<Label> progressLabels = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();
    private Runnable onCancel = null;
    private boolean[] complete;

    public MultiProgressDialog(String title, List<DownloadRequest> downloadRequests) {
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
            vBox.setMargin(bar, new Insets(-5, 0, 0, 0));
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
                    System.out.println("complete one");
                    return;
                }
            }
            GUIManager guiManager = GUIManager.getInstance();
            Platform.runLater(() -> {
                guiManager.showMessageDialog("Message", "File downloads complete");
                Platform.runLater(stage::hide);
            });
        }
    }
    
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }
}
