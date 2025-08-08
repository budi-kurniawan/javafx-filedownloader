package example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Budi Kurniawan (https://brainysoftware.com)
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.<Parent>load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("File Downloader - BrainySoftware.com");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/example/icon.png")));
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