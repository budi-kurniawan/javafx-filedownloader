package com.brainysoftware.downloader.ui;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
	private void handleOpenDocument(final ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PDF Document");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));
	}

	@FXML
    private void handleDownload(final ActionEvent event) {
        String[] urls = {"", ""};
        DownloadStatusDialog dialog = new DownloadStatusDialog(urls);
        dialog.showAndWait().ifPresent(result -> {
            // change view and DocumentNote of current page
//            float degrees = result.degrees();
//            int pageNo = GUIManager.currentPageNo;
//            EventManager.getInstance().firePageRotatedEvent(
//                    new PageRotatedEvent(event.getSource(), 
//                            GUIManager.currentPageNo, degrees));
//            PDFHandler pdfHandler = GUIManager.getInstance().getCurrentPDFHandler();
//            String scope = result.scope().toLowerCase();
//            if (scope.startsWith("all")) {
//                int numPages = pdfHandler.getNumPages();
//                for (int i = 0; i < numPages; i++) {
//                    PageNote pageNote = pdfHandler.getPageNote(i);
//                    pageNote.setRotated(degrees);
//                    pageNote.getThumbnailView().setRotate(degrees);
//                }
//            } else {
//                PageNote pageNote = pdfHandler.getPageNote(pageNo);
//                pageNote.setRotated(degrees);
//                pageNote.getThumbnailView().setRotate(degrees);
//            }
//            // update display of current page
//            EventManager.getInstance().firePageSelectedEvent(
//                    new PageSelectedEvent(event.getSource(), pageNo));
        });

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