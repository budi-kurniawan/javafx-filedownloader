package com.brainysoftware.downloader.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class DownloadStatusDialog extends Dialog<String> {
    
    public DownloadStatusDialog(String[] urls) {
        // Set dialog title and header
//        Path path = pdfHandler.getPath();
//        int numPages = pdfHandler.getNumPages();
        setTitle("Download Statuses");
        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("download-status-dialog.fxml"));
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(loader.load());
            setDialogPane(dialogPane);

            // Add OK and Cancel buttons
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Get the controller to retrieve data
            DownloadStatusDialogController controller = loader.getController();
//            controller.setPathLabel(path);
//            controller.setNumPagesLabel(Integer.toString(numPages));

            // for Cancel button
            setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return "OK";
                }
                return null;
            });

            Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
            // calls event.consume() if input is invalid to prevent the dialog from closing
            okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                TextField pagesTextField = controller.getPagesTextField();
                GUIManager guiManager = GUIManager.getInstance();
                if (!pagesTextField.isVisible()) {
                    // All pages selected
//                    int[] range = IntStream.range(0, numPages).toArray();
//                    setResultConverter(buttonType -> {
//                        if (buttonType == ButtonType.OK) {
//                            return new ImportPDFDialogResponse(range);
//                        }
//                        return null;
//                    });
                } else {
                    // Only some pages selected

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}