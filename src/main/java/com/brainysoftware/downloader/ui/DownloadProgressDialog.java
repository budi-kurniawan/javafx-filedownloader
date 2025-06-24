package com.brainysoftware.downloader.ui;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;

public class DownloadProgressDialog extends Dialog<Void> {
    private final ProgressBar progressBar;
    private final Button cancelButton;

    public DownloadProgressDialog(String title, String message) {
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);

        // Create and configure UI components
        Label label = new Label(message);
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            //close(); // Close the dialog
            if (onCancel != null) onCancel.run();
        });

        VBox content = new VBox(10, label, progressBar, cancelButton);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().clear(); // No default OK/Cancel buttons
    }

    // Allows caller to set progress value
    public void setProgress(double value) {
        Platform.runLater(() -> progressBar.setProgress(value));
    }

    // Allows caller to handle cancel
    private Runnable onCancel;

    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }
}
