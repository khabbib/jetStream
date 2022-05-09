package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Objects;


/**
 * This class executes the program.
 */
public class App extends Application {

    @FXML public StackPane pane;

    /**
     * @param stage
     */
    @Override public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));
            Scene scene = new Scene(root);
            //MoveScreen.moveScreen(root, stage); // move windows by clicking on the stage
            stage.setScene(scene);
            stage.setResizable(false);
            Image img = new Image("./application/image/officialLogo.png");
            stage.getIcons().add(img);
            
            //stage.initStyle(StageStyle.UNDECORATED); // to remove windows border
            Image img = new Image("/application/image/jetStream.png");
            stage.getIcons().add(img);
            stage.setTitle("Home");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Main application runs here
    public static void main(String[] args) {
        launch(args);
    }
}