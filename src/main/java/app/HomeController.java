package app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField receive;

    public void showText(String txt){
        receive.setText(txt);
    }


    public void switchToHome(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        root = loader.load();

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Home window");
        stage.setScene(scene);
        stage.show();
    }
}
