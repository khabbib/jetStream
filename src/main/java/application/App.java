package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.util.Objects;


/**
 * This class executes the program.
 * @author khabib
 */
public class App extends Application {

    @FXML public StackPane pane;

    /**
     * The overwrite method will start the application extended JavaFX.
     * @param stage javaFX stage
     * @author Khabib.
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
            stage.setTitle("Home");
            stage.show();

            Media buzzer = new Media(getClass().getResource("sounds/start.wav").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(buzzer);
            mediaPlayer.play();
            System.out.println("'Start' fx played!");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Start application
     * @param args argument
     */
    public static void main(String[] args) {
        launch(args);
    }
}