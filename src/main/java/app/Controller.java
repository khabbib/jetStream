package app;
import app.HomeController;
import app.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML // tar in en reference from javaFX element
    private Circle avatar; // profile circle
    @FXML
    private ButtonBar logout; // btn bar
    @FXML
    private TextField password;
    @FXML
    private Label error;
    private User message;
    @FXML
    private TextField sender;


    //////  NAVIGATE TO PAGES  ///////

    // the method will switch the user to the Home page


    // the method will switch the user to the login page
    public void switchToLogin(ActionEvent e) throws IOException {

        String name = sender.getText();
        FXMLLoader loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        Parent root = loader.load();

        HomeController hc = loader.getController();
        hc.showText(name);

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login window");
        stage.setScene(scene);
        stage.show();
    }

    // the method will switch the user to the dashboard page
    public void switchToDashboard(ActionEvent e)throws IOException{
        if (password != null) {
            message = new User(password.getText());
        }
        if (message != null){
            if (message.getMessage().equals("hello")){
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
                stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setTitle("Dashboard window");
                stage.setScene(scene);
                stage.show();
            } else {
                error.setText("Wrong password!");
            }
        }else {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
            stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Dashboard window");
            stage.setScene(scene);
            stage.show();
        }

    }

    // the method will switch the user to the checking page
    public void switchToChecking(ActionEvent e)throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Checking.fxml")));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Checking window");
        stage.setScene(scene);
        stage.show();
    }






    //////  MENU  ///////

    // the method will open the menu once the user clicked on his profile
    public void openMenu() {
        logout.setLayoutX(0); // this will move in menu from outside the window
        System.out.println("Menu opened");
    }

    // the method will close the menu
    public void closeMenu() {
        logout.setLayoutX(-84); // this will move out the menu outside the window
        System.out.println("Menu closed");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("1231");
    }
}