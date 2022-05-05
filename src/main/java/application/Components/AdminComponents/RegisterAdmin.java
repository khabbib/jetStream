package application.Components.AdminComponents;

import application.Controller;
import application.config.Config;
import application.database.Connection;
import application.model.ConfirmActions;
import javafx.event.ActionEvent;

import java.sql.SQLException;

public class RegisterAdmin
{
    private Controller controller;
    private Config config;
    private Connection connection;

    private ConfirmActions confirmActions;

    /**
     * @param controller
     * @param connection
     * @param config
     */
    public RegisterAdmin(Controller controller, Connection connection, Config config){
        this.controller = controller;
        this.connection = connection;
        this.config = config;
        confirmActions = new ConfirmActions(controller);
    }

    public boolean registerUserAdmin(ActionEvent e) throws SQLException {

        boolean registered = false;
        if (!controller.first_name_reg_admin.getText().isEmpty() &&
                !controller.last_name_reg_admin.getText().isEmpty() &&
                !controller.address_reg_admin.getText().isEmpty() &&
                !controller.emailaddress_reg_admin.getText().isEmpty() &&
                !controller.phone_number_reg_admin.getText().isEmpty() &&
                !controller.password_reg_admin.getText().isEmpty() &&
                !controller.confirm_password_reg_admin.getText().isEmpty()
        ){
            if ((controller.first_name_reg_admin.getText().length() >= 3 && controller.first_name_reg_admin.getText().length() <= 30)){
                if ((controller.last_name_reg_admin.getText().length() >= 3 && controller.last_name_reg_admin.getText().length() <= 30)){
                    if ((controller.address_reg_admin.getText().length() >= 5 && controller.address_reg_admin.getText().length() <= 60)){
                        if((controller.emailaddress_reg_admin.getText().length() >= 6 && controller.emailaddress_reg_admin.getText().length() <= 30)){
                            if ((controller.phone_number_reg_admin.getText().length() == 10)){
                                if (controller.password_reg_admin.getText().length() >= 8 && controller.password_reg_admin.getText().length() <= 20){
                                    if (controller.password_reg_admin.getText().equals(controller.confirm_password_reg_admin.getText())){
                                        if(controller.emailaddress_reg_admin.getText().contains("@") && (controller.emailaddress_reg_admin.getText().contains("gmail") || controller.emailaddress_reg_admin.getText().contains("hotmail") || controller.emailaddress_reg_admin.getText().contains("yahoo") || controller.emailaddress_reg_admin.getText().contains("outlook"))){
                                            if(controller.isAdminCheckbox.isSelected()) {
                                                boolean ok1 = connection.saveUser(controller.first_name_reg_admin.getText(), controller.last_name_reg_admin.getText(), controller.address_reg_admin.getText(), controller.emailaddress_reg_admin.getText(), controller.phone_number_reg_admin.getText(), controller.password_reg_admin.getText(), true);
                                                if(ok1) {
                                                    registered = true;
                                                } else {
                                                    confirmActions.displayMessage(controller.registration_error_admin, "Not registered! (Email exists!)", true);
                                                }
                                            } else{
                                                boolean ok2 = connection.saveUser(controller.first_name_reg_admin.getText(), controller.last_name_reg_admin.getText(), controller.address_reg_admin.getText(), controller.emailaddress_reg_admin.getText(), controller.phone_number_reg_admin.getText(), controller.password_reg_admin.getText(), false);
                                                if(ok2) {
                                                    registered = true;
                                                } else {
                                                    confirmActions.displayMessage(controller.registration_error_admin, "Not registered! (Email exists!)", true);
                                                }
                                            }
                                        }else {
                                            confirmActions.displayMessage(controller.email_issue_reg_admin, "Type issue [email]", true);
                                        }
                                    }else {
                                        confirmActions.displayMessage(controller.confirm_password_issue_reg_admin, "Mach issue [confirm password]", true);
                                    }
                                }else {
                                    confirmActions.displayMessage(controller.password_issue_reg_admin, "Size issue 8-20", true);
                                }
                            }else {
                                confirmActions.displayMessage(controller.phone_number_issue_reg_admin, "Size issue 10 digit", true);
                            }
                        } else {
                            confirmActions.displayMessage(controller.email_issue_reg_admin, "Size issue 6-30", true);
                        }
                    }else {
                        confirmActions.displayMessage(controller.address_issue_reg_admin, "Size issue 5-60", true);
                    }
                } else {
                    confirmActions.displayMessage(controller.last_name_issue_reg_admin, "Size issue 3-30", true);
                }
            } else {
                confirmActions.displayMessage(controller.name_issue_reg_admin, "Size issue 3-30", true);
            }
        }else {
            confirmActions.displayMessage(controller.registration_error_admin, "Empty field issue", true);
        }
        return registered;
    }
}
