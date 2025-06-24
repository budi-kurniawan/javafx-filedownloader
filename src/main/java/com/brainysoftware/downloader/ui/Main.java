package com.brainysoftware.downloader.ui;

import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * An alternative to extending Application is to do this:
 * 
 * public class MainEntry {
    public static void main(String[] args) {
        Controller controller = new Controller();

        final MainStage mainStage = new MainStage(controller);
        mainStage.init();

        Platform.startup(() -> {
            // create primary stage
            Stage stage = new Stage();

            mainStage.start(stage);
        });
    }
}

 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.<Parent>load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

//        GUIManager.currentScene = scene;
//        GUIManager.currentStage = stage;

        stage.setTitle("PDF Editor - Brainy Software Inc.");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/icon.png")));
        
        stage.setScene(scene);
        stage.show();
        
        Parameters params = getParameters();
        List<String> args = params.getRaw(); // get the args in launch(args) in main
    }
    
    @Override
    public void stop() {
    }

    public static void main(String[] args) {
        launch(args);
    }  
    
}