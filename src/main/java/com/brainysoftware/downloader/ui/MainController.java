package com.brainysoftware.downloader.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * @author Budi Kurniawan (http://brainysoftware.com)
 */
public class MainController implements Initializable {
    
    @FXML private MenuBar menuBar;
    @FXML private ToolBar toolBar;
    @FXML private Label label;
    @FXML private VBox centerPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize MainController...");
    }
    
    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            KeyCode keyCode = keyEvent.getCode();
            if (keyCode == KeyCode.DOWN) {
                System.out.println("execute down");
            } else if (keyCode == KeyCode.UP) {
                System.out.println("execute UP");
            }
        }
    }
}