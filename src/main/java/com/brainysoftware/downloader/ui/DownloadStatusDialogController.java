package com.brainysoftware.downloader.ui;

import java.nio.file.Path;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class DownloadStatusDialogController {

    @FXML
    private ToggleGroup pagesGroup;
    
    @FXML
    private Label pathLabel;

    @FXML
    private TextField pagesTextField;
    
    @FXML
    private Label numPagesLabel;

    public String getPages() {
        RadioButton selected = (RadioButton) pagesGroup.getSelectedToggle();
        if ("All".equals(selected.getText())) {
            return "All";
        } else {
            return pagesTextField.getText();
        }
    }
    
    public void setPathLabel(Path path) {
        pathLabel.setText(path.getFileName().toString());
    }
    
    public void setNumPagesLabel(String num) {
        numPagesLabel.setText(num);
    }
    
    public TextField getPagesTextField() {
        return pagesTextField;
    }
    
    @FXML
    private void handleRadioButtonAction() {
        RadioButton selectedRadioButton = (RadioButton) pagesGroup.getSelectedToggle();
        pagesTextField.setVisible(!"All".equals(selectedRadioButton.getText()));
    }    

}