package application.Components;

import application.Controller;
import application.config.Config;
import application.database.Connection;
import application.model.ConfirmActions;

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
        if (!controller.registration_first_name.getText().isEmpty() && !controller.registration_last_name.getText().isEmpty() && !controller.registration_address.getText().isEmpty() && !controller.registration_email.getText().isEmpty() && !controller.registration_phone_number.getText().isEmpty() && !controller.registration_password.getText().isEmpty() && !controller.registration_confirm_password.getText().isEmpty()){
            if ((controller.registration_first_name.getText().length() >= 3 && controller.registration_first_name.getText().length() <= 30)){
                if ((controller.registration_last_name.getText().length() >= 3 && controller.registration_last_name.getText().length() <= 30)){
                    if ((controller.registration_address.getText().length() >= 5 && controller.registration_address.getText().length() <= 60)){
                        if((controller.registration_email.getText().length() >= 6 && controller.registration_email.getText().length() <= 30)){
                            if ((controller.registration_phone_number.getText().length() == 10)){
                                if (controller.registration_password.getText().length() >= 8 && controller.registration_password.getText().length() <= 20){
                                    if (controller.registration_password.getText().equals(controller.registration_confirm_password.getText())){
                                        if(controller.registration_email.getText().contains("@") && (controller.registration_email.getText().contains("gmail") || controller.registration_email.getText().contains("hotmail") || controller.registration_email.getText().contains("yahoo") || controller.registration_email.getText().contains("outlook"))){
                                            boolean ok = connection.saveUser(controller.registration_first_name.getText(), controller.registration_last_name.getText(), controller.registration_address.getText(), controller.registration_email.getText(), controller.registration_phone_number.getText(), controller.registration_password.getText(), false);
                                            if (ok) {
                                                try {
                                                    connection.setProfilePicture("application/profiles/user.png", controller.registration_email.getText());
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                                registered = true;
                                            } else {
                                                confirmActions.displayMessage(controller.registration_error_lbl, "Not registered! (Email exists!)", true);
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
            confirmActions.displayMessage(controller.registration_error_lbl, "Empty field issue", true);
        }
        return registered;
    }
}
