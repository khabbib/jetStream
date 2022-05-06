package application.Components;

import application.Controller;
import application.config.Config;
import application.database.Connection;
import application.model.ConfirmActions;
import application.model.User;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.util.Duration;

import java.sql.SQLException;

/**
 * This class registers users and handles errors. Only users!
 */
public class RegistrationUser {
    private Controller controller;
    private Config config;
    private Connection connection;

    private ConfirmActions confirmActions;

    /**
     * @param controller
     * @param connection
     * @param config
     */
    public RegistrationUser(Controller controller, Connection connection, Config config){
        this.controller = controller;
        this.connection = connection;
        this.config = config;
        confirmActions = new ConfirmActions(controller);
    }

    /**
     * @return boolean.
     * @throws SQLException if any sql issue occur.
     * @author Khabib and Sossio. Developed by Sossio.
     */
    public boolean registerUser() {
        boolean registered = false;
        if (!controller.first_name_reg.getText().isEmpty() && !controller.last_name_reg.getText().isEmpty() && !controller.address_reg.getText().isEmpty() && !controller.emailaddress_reg.getText().isEmpty() && !controller.phone_number_reg.getText().isEmpty() && !controller.password_reg.getText().isEmpty() && !controller.confirm_password_reg.getText().isEmpty()){
            if ((controller.first_name_reg.getText().length() >= 3 && controller.first_name_reg.getText().length() <= 30)){
                if ((controller.last_name_reg.getText().length() >= 3 && controller.last_name_reg.getText().length() <= 30)){
                    if ((controller.address_reg.getText().length() >= 5 && controller.address_reg.getText().length() <= 60)){
                        if((controller.emailaddress_reg.getText().length() >= 6 && controller.emailaddress_reg.getText().length() <= 30)){
                            if ((controller.phone_number_reg.getText().length() == 10)){
                                if (controller.password_reg.getText().length() >= 8 && controller.password_reg.getText().length() <= 20){
                                    if (controller.password_reg.getText().equals(controller.confirm_password_reg.getText())){
                                        if(controller.emailaddress_reg.getText().contains("@") && (controller.emailaddress_reg.getText().contains("gmail") || controller.emailaddress_reg.getText().contains("hotmail") || controller.emailaddress_reg.getText().contains("yahoo") || controller.emailaddress_reg.getText().contains("outlook"))){
                                            boolean ok = connection.saveUser(controller.first_name_reg.getText(), controller.last_name_reg.getText(), controller.address_reg.getText(), controller.emailaddress_reg.getText(), controller.phone_number_reg.getText(), controller.password_reg.getText(), false);
                                            if (ok) {
                                                try {
                                                    connection.setProfilePicture("application/profiles/user.png", controller.emailaddress_reg.getText());
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                                registered = true;
                                            } else {
                                                confirmActions.displayMessage(controller.registration_error, "Not registered! (Email exists!)", true);
                                            }
                                        }else {
                                            confirmActions.displayMessage(controller.email_issue_reg, "Type issue [email]", true);
                                        }
                                    }else {
                                        confirmActions.displayMessage(controller.confirm_password_issue_reg, "Mach issue!", true);
                                    }
                                }else {
                                    confirmActions.displayMessage(controller.password_issue_reg, "Size issue 8-20", true);
                                }
                            }else {
                                confirmActions.displayMessage(controller.phone_number_issue_reg, "Size issue 10 digit", true);
                            }
                        } else {
                            confirmActions.displayMessage(controller.email_issue_reg, "Size issue 6-30", true);
                        }
                    }else {
                        confirmActions.displayMessage(controller.address_issue_reg, "Size issue 5-60", true);
                    }
                } else {
                    confirmActions.displayMessage(controller.last_name_issue_reg, "Size issue 3-30", true);
                }
            } else {
                confirmActions.displayMessage(controller.name_issue_reg, "Size issue 3-30", true);
            }
        }else {
            confirmActions.displayMessage(controller.registration_error, "Empty field issue", true);
        }
        return registered;
    }
}
