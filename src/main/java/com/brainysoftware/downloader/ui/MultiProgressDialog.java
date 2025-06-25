package com.brainysoftware.downloader.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MultiProgressDialog {
    private final Stage stage;
    private final List<Label> labels = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();
    private Runnable onCancel = null;

    public MultiProgressDialog(String title, int count) {
        stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        for (int i = 0; i < count; i++) {
            Label label = new Label("Task " + (i + 1));
            ProgressBar bar = new ProgressBar(0);
            bar.setPrefWidth(300);

            labels.add(label);
            progressBars.add(bar);

            root.getChildren().addAll(label, bar);
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
        }
    }

    public void setLabel(int index, String text) {
        if (index >= 0 && index < labels.size()) {
            Platform.runLater(() -> labels.get(index).setText(text));
        }
    }
    
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }
}
