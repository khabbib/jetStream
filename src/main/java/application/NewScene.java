package application;

import application.Model.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;


public class NewScene {
    //private static final Random RND = new Random();
    //private              World         world;
   // private              CountryRegion europeanUnion;


    public static  void showNewScene(String title, User user) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(400);
        //StackPane pane = new StackPane();

        if (user != null){
            Label label = new Label();
            label.setText(user.getId());

            Label email = new Label();
            email.setText(user.getEmail());

            Label password = new Label();
            password.setText(user.getPassword());

            Button closeBtn = new Button("Clone window");
            closeBtn.setOnAction(e -> stage.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label,email, password, closeBtn);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            stage.setScene(scene);
            stage.showAndWait();
        }
    }
}
