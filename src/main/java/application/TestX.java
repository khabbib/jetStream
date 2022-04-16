package application;

import application.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class TestX implements Initializable {
    @FXML private Label name;
    private User user;

    /**
     * @param user
     */
    public void passData(User user){
        this.user = user;
        name.setText(user.getName());
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
