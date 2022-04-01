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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    //@FXML private Label success_msg_dash;
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
    public void switchToDashboard(ActionEvent e) throws IOException {
        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            User user = Db.authenticationUser(login_email.getText(), login_pass.getText());
            if (user != null) {
                renderDashboard(e, user);
            } else {
                error.setText("Wrong email or pass!");
            }
        } else {
            error.setText("Fill the field!");
        }
    }

    @FXML private Label chosen_sit;
    @FXML public static VBox display_filght;
    //@FXML public static VBox valdeRese;
    // the method will render dashboard page for user
    public void renderDashboard(ActionEvent e, User user) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")));
        this.user = user;
        display_filght = (VBox) root.lookup("#display_filght");
        //valdeRese = (VBox) root.lookup("#valdeRese");
        System.out.println("hhelelffljdsfljsd");
        anchorPane = (AnchorPane) root.lookup("#anchorPane");
        chosen_sit = (Label) root.lookup("#chosen_sit");
        world = CreateWorld.init();

        anchorPane.getChildren().add(world);
        anchorPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        System.out.println(user.getName() + ", " + user.getId());
        u_name = (Label) root.lookup("#u_name");
        u_id = (Label) root.lookup("#u_id");
        u_name.setText(user.getName());
        u_id.setText(user.getId());
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
        String chosenSit = SiteManager.addSitePlace();
        if (chosenSit != null) {
            chosen_sit.setText(chosenSit);
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
        display_filght.getChildren().clear();
        ArrayList<FlygResa> compare = new ArrayList<>();
        for (int i = 0; i < resor.size();i++){
            ScrollPane sp = new ScrollPane();

            HBox hbox = new HBox(1);
            hbox.setPadding(new Insets(20));
            //hbox.setBorder(new Border(new BorderStroke(Color.PINK,BorderStrokeStyle.DASHED,null,null)));
            hbox.setEffect(new DropShadow(2.0, Color.BLACK));
            hbox.setBackground(new Background(new BackgroundFill(Color.rgb(210,210,210),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            //Label nr = new Label();
            hbox.setSpacing(10);
            //nr.setText(i + ". ");

            Image img = new Image("/application/jetStream.png");

            ImageView image = new ImageView(img);
            image.setFitWidth(40);
            image.setFitHeight(50);

            Label titleF = new Label();
            titleF.setText(resor.get(i).getFrom());

            Label titleD = new Label();
            titleD.setText(resor.get(i).getDistination());
            Label date = new Label();
            date.setText(resor.get(i).getDate());

            Button btn = new Button("Välja");
            btn.setStyle("-fx-background-color: #eee; -fx-text-fill: #333; -fx-padding: 20px 35");
            int finalI1 = i;
            btn.setOnAction(e -> {
                //valdeRese.getChildren().clear();
                //valdeRese.getChildren().add(display_filght.getChildren().get(finalI1));
                display_filght.getChildren().get(finalI1).setOpacity(0.8);
                for (int m = 0; m < display_filght.getChildren().size(); m++){
                    if (display_filght.getChildren().get(m) != display_filght.getChildren().get(finalI1)) {
                        display_filght.getChildren().get(m).setOpacity(1);
                    }
                }
                compare.add(resor.get(finalI1));

                /*HBox ls = new HBox();
                ls.getChildren().add(display_filght.getChildren().get(finalI1));
                hbox.setBackground(new Background(new BackgroundFill(Color.rgb(133, 200, 138),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
                hbox.setAlignment(Pos.TOP_CENTER);
                display_filght.getChildren().clear();
                display_filght.getChildren().add(ls);*/
            });
            hbox.getChildren().addAll(image, titleF, titleD, btn);
            display_filght.getChildren().addAll(hbox);
            sp.setContent(display_filght);
        }
        /*date_dash_flight.setText(resor.get(0).getDate());
        destination_dash_flight.setText(resor.get(0).getDistination());
        from_dash_flight.setText(resor.get(0).getFrom());*/
    }
}
