package application;

import eu.hansolo.fx.world.World;
import eu.hansolo.fx.world.WorldBuilder;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;

import static javafx.application.Application.launch;

public class App extends Application {
    @FXML
    public StackPane pane;
    public World world;

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            //stage.getIcons().add(new Image("application/jetStream.png"));

            //stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Home");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}