package application.components.login;

import application.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

/**
 * This class handle the login page password. Show the password if user wants it.
 * @author khabib
 */
public class ShowPasswordField {
    /**
     * Constructor to ShowPasswordField.
     * @param e event for login page.
     * @param controller instance of control class.
     * @author Khabib
     */
    public void showPassword(ActionEvent e, Controller controller){
        if (e.getSource() == controller.show_pasword_login){
            controller.login_pass.setVisible(true);
        }
    }

    /**
     * This method handle the operation for showing the password to user.
     * @param controller instance of control class.
     */
    public void showPassFieldLogin(Controller controller){
        int maxLength = 15;
        if (controller.show_pasword_login.isSelected()){
            controller.show_password_field_login.setDisable(false);
            controller.show_password_field_login.setOpacity(1);
            controller.show_password_field_login.setText(controller.login_pass.getText());
            System.out.println("Select");

        }else{
            controller. show_password_field_login.setDisable(true);
            controller.show_password_field_login.setText(null);
            controller.show_password_field_login.setOpacity(0);
        }
        controller.login_pass.textProperty().addListener(new ChangeListener<String>() {
            private boolean validating = false;
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!validating) {
                    validating = true;
                    String newText = newValue;
                    if (newText.length() > maxLength) {
                        newText = newText.substring(0, maxLength);
                    }
                    controller.show_password_field_login.setText(newText);
                    controller.login_pass.setText(newText);

                    validating = false;
                }
            }
        });
    }
}
