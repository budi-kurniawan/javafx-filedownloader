package com.brainysoftware.downloader.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.<Parent>load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("File Downloader - Brainy Software Inc.");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/icon.png")));
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() {
    }

    public static void main(String[] args) {
        launch(args);
    }  
    
}