package app;
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


    public void sendData(User user){

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

    // the method will switch the user to the login page
    public void switchToLogin(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login window");
        stage.setScene(scene);
        //success_msg.setText(msg);
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


    public void registeruser(ActionEvent e) throws SQLException, IOException {
        user = new User(name.getText(), lname.getText(), adress.getText(), email.getText(), number.getText());
        //db = new DB();
        boolean ok = saveUser(user);
        if (ok){
            renderPage(e);
        }else {
            System.out.println("no went through");
        }
    }

    public boolean saveUser(User user) throws SQLException{
        boolean ok = false;
        if(user != null){
            Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            stmt.executeUpdate("insert into userr(u_f_name, u_l_name, u_address, u_email, u_phone_nr, u_password) values('" + user.getName() + "' , '" + user.getLname() + "' , '" + user.getAdress()+ "' , '" + user.getAdress() +"' , '" + user.getEmail() + "', '" + user.getNumber() +"')");
            ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + user.getEmail() +"'");
            while (rs.next()){
                System.out.println("User saved not from db");
                System.out.println(rs);
                ok= true;
            }
            con.close();
            stmt.close();
        }
        return ok;
    }
    public Connection getDatabaseConnection() {

        String url = "jdbc:postgresql://pgserver.mau.se:5432/am2510";
        String user = "am2510";
        String password = "zyvl0ir7";

        Connection con = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // the method will switch the user to the registration page
    public void switchToLoginFromRegistration(ActionEvent e)throws IOException{
        if (!name.getText().isEmpty() && !lname.getText().isEmpty() && !adress.getText().isEmpty() && !email.getText().isEmpty() && !number.getText().isEmpty()){
            System.out.println(name.getText());
            user = new User(name.getText(), lname.getText(), adress.getText(), email.getText(), number.getText());
            userList.add(user);
            renderPage(e);
            // send this message to the login page
            System.out.println("user successfully registered!");
            //success_msg.setText("User successfully registered! \n ");
        }else {
            registration_error.setText("Empty field!");
        }
    }

    public void renderPage(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login window");
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