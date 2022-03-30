package application;
import application.Database.Db;
import application.Model.CreateWorld;
import application.Model.User;
import eu.hansolo.fx.world.World;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
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
    private TextField login_pass;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField login_email;
    @FXML
    private Label error;
    @FXML
    private Label registration_error;
    private User user;
    @FXML
    private TextField sender;
    @FXML
    private Label success_msg;
    // from registration
    @FXML
    private TextField name, lname, adress, email, number;
    private ArrayList<User> userList = new ArrayList<>();
    private String msg;

    private World world;




    public void openNewScene(ActionEvent e){
            NewScene.showNewScene("New window", "Hello from controller");
    }

    //////  NAVIGATE TO PAGES  ///////

    // the method will switch the user to the Home page
    public void switchToHome(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }



    // the method will switch the user to the dashboard page
    public void switchToDashboard(ActionEvent e) throws IOException {
        //if (!userList.isEmpty()){
            //for (User item: userList){
        if (login_pass != null){
            if (login_email.getText().equals("email") && login_pass.getText().equals("pass")) {
                renderDashboard(e);
            } else {
                renderDashboard(e);
                error.setText("Wrong password!");
            }
        }else {
            renderDashboard(e);
        }
        //}
       // }else {
          //  success_msg.setText("Register before accessing to item");
        //}
    }
    public void renderDashboard(ActionEvent e)throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
        anchorPane = (AnchorPane) root.lookup("#anchorPane");
        world = CreateWorld.init();
            try {
                anchorPane.getChildren().add(world);
                anchorPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            } catch (Exception b) {
                b.printStackTrace();
                System.out.println("error");
            }
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Dashboard window");
        stage.setScene(scene);
        stage.show();
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

    // the method will switch the user to the registration page
    public void switchToRegistration(ActionEvent e)throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Registration.fxml")));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Registration window");
        stage.setScene(scene);
        stage.show();
    }

    // the method will register the user and return to the login page
    public void registeruser(ActionEvent e) throws SQLException, IOException {
        user = new User(name.getText(), lname.getText(), adress.getText(), email.getText(), number.getText());
        boolean ok = Db.saveUser(user);
        if (ok){
            renderLoginPage(e, "successfully registered the user!");
        }else {
            registration_error.setText("Couldn't register the information");
        }
    }

    // the method will switch the user to the login page
    public void switchToLogin(ActionEvent e) throws IOException {
        renderLoginPage(e, null);
    }
    // render pages
    public void renderLoginPage(ActionEvent e, String msg) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        success_msg = (Label) root.lookup("#success_msg");
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login window");
        stage.setScene(scene);
        success_msg.setText(msg);
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

    }
}