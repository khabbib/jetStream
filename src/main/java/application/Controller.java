package application;

import application.Database.Db;
import application.Model.CreateWorld;
import application.Model.FlygResa;
import application.Model.SiteManager;
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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
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
    // @FXML tar in en reference from javaFX element
    @FXML private Circle avatar; // profile circle
    @FXML private ButtonBar logout; // btn bar
    @FXML private TextField login_pass;
    @FXML private AnchorPane anchorPane;
    @FXML private TextField login_email;
    @FXML private Label error;
    @FXML private Label registration_error;
    private User user;
    @FXML private TextField sender;
    @FXML private Label success_msg;
    @FXML private Label success_msg_dash;
    @FXML private Label u_name, u_id;

    // from registration
    @FXML
    private TextField name, lname, adress, email, number, password;
    private ArrayList<User> userList = new ArrayList<>();
    private String msg;

    private World world;

    public void openNewScene(ActionEvent e) {}

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
    public void switchToDashboard(ActionEvent e) {
        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            try {
                User user = Db.authenticationUser(login_email.getText(), login_pass.getText());
                if (user != null) {
                    renderDashboard(e, user);
                } else {
                    error.setText("Wrong email or pass!");
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        } else {
            error.setText("Fill the field!");
        }
    }

    @FXML private static Label date_dash_flight;
    @FXML private static Label destination_dash_flight;
    @FXML private static Label from_dash_flight;
    @FXML private Label chosen_sit;

    // the method will render dashboard page for user
    public void renderDashboard(ActionEvent e, User user) throws IOException {
        this.user = user;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
        anchorPane = (AnchorPane) root.lookup("#anchorPane");
        success_msg_dash = (Label) root.lookup("#success_msg_dash");
        //output_info = (Label) root.lookup("#output_info");
        date_dash_flight = (Label) root.lookup("#date_dash_flight");
        destination_dash_flight = (Label) root.lookup("#destination_dash_flight");
        from_dash_flight = (Label) root.lookup("#from_dash_flight");
        chosen_sit = (Label) root.lookup("#chosen_sit");
        success_msg_dash.setText("Active");
        world = CreateWorld.init();
        try {
            anchorPane.getChildren().add(world);
            anchorPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            System.out.println(user.getName() + " , " + user.getId());
            u_name = (Label) root.lookup("#u_name");
            u_id = (Label) root.lookup("#u_id");
            u_name.setText(user.getName());
            u_id.setText(user.getId());
        } catch (Exception b) {
            b.printStackTrace();
        }
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Dashboard window");
        stage.setScene(scene);
        stage.show();
    }

    // the method will switch the user to the checking page
    public void switchToChecking(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Checking.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Checking window");
        stage.setScene(scene);
        stage.show();
    }

    // the method will switch the user to the registration page
    public void switchToRegistration(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Registration.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Registration window");
        stage.setScene(scene);
        stage.show();
    }

    // the method will register the user and return to the login page
    public void registeruser(ActionEvent e) throws SQLException, IOException {

        user = new User(null, name.getText(), lname.getText(), adress.getText(), email.getText(), number.getText(), password.getText(), false);
        System.out.println(user.getName() + "fsdfsdfsdf");
        boolean ok = Db.saveUser(user);
        if (ok) {
            renderLoginPage(e, "successfully registered the user!");
        } else {
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
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login window");
        stage.setScene(scene);
        success_msg.setText(msg);
        stage.show();
    }

    public void switchToviewFlights(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FlightsView.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("FlightsView");
        stage.setScene(scene);
        stage.show();
    }

    //This metod will switch to adminview
    public void switchToAdminView(ActionEvent e) {

        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            try {
                User user = Db.authenticationAdmin(login_email.getText(), login_pass.getText());
                if (user != null) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminView.fxml")));
                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setTitle("Admin window");
                    stage.setScene(scene);
                    stage.show();
                } else {
                    error.setText("Wrong email or pass!");
                }
            }catch (IOException io){
                io.printStackTrace();
            }
        } else {
            error.setText("Fill the field!");
        }
    }

    public void switchToMembersView(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MemberView.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Members window");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToBookedFligthsView(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("BookedFlightsView.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
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

    // the method will open the profile
    public void openProfile() throws FileNotFoundException {
        int user_id = Integer.parseInt(u_id.getText());
        User user = Db.getUserWithID(user_id);
        NewScene.showNewScene(user.getName() + "'s Profile", null);

    }

    public void choseSit(ActionEvent e) {
        int chosenSit = SiteManager.addSitePlace(40);
        if (chosenSit != -1) {
            chosen_sit.setText(String.valueOf(chosenSit));
        }

    }

    public void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are about to Exit!");
        alert.setContentText("Do you really want to Exit?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) anchorPane.getScene().getWindow();
            System.out.println("You have successfully exited!");
            stage.close();
        }
    }

    //Denna metoden gör så att man kan logga ut från application
    public void logout(ActionEvent event) {
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Logout");
        alert2.setHeaderText("You are about to logout!");
        alert2.setContentText("Do you really want to logout?");

        if (alert2.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) anchorPane.getScene().getWindow();
            System.out.println("You have successfully logged out!");
            stage.close();
        }

    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){

    }

    public static void fyllTable (ArrayList < FlygResa > resor) {
        date_dash_flight.setText(resor.get(0).getDate());
        destination_dash_flight.setText(resor.get(0).getDistination());
        from_dash_flight.setText(resor.get(0).getFrom());
    }
}
