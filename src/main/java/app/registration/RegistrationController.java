package app.registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAction;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML
    private TextField name, lname, email,address, number;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void register(ActionEvent e){
        System.out.println("Registerd!");
    }
}
