package application.components.registration;

import application.Controller;
import application.config.Config;
import application.api.Db;
import application.ErrorHandler;
import javafx.event.ActionEvent;

import java.sql.SQLException;

/**
 * This class registers both users admins and handles errors.
 */
public class RegisterAdmin {
    private Controller controller;
    private Config config;
    private Db db;
    private ErrorHandler errorHandler;

    /**
     * Class constructor.
     * @param controller connects variables and methods.
     * @param db connects to database.
     * @param config connects to root.
     */
    public RegisterAdmin(Controller controller, Db db, Config config){
        this.controller = controller;
        this.db = db;
        this.config = config;
        errorHandler = new ErrorHandler(controller);
    }

    /**
     * This method registers a user or admin to database.
     * It checks if all text fields is verified with its specific limitation.
     * @param e handles an event.
     * @return boolean.
     * @author Obed. Developed by Sossio.
     */
    public boolean registerUserAdmin(ActionEvent e) {
        boolean registered = false;
        if (ErrorHandler.validateInputLimit(controller.first_name_reg_admin, "firstname")){
            if (ErrorHandler.validateInputLimit(controller.last_name_reg_admin, "lastname")){
                if (ErrorHandler.validateInputLimit(controller.address_reg_admin, "address")){
                    if(ErrorHandler.validateInputLimit(controller.emailaddress_reg_admin, "email")){
                        if (ErrorHandler.validateInputLimit(controller.phone_number_reg_admin, "phone")){
                            if (ErrorHandler.validateInputLimit(controller.password_reg_admin, "password")){
                                if (controller.password_reg_admin.getText().equals(controller.confirm_password_reg_admin.getText())){
                                    if(ErrorHandler.validateInputLimit(controller.emailaddress_reg_admin, "email-format")){
                                        if(controller.is_admin_checkbox.isSelected()) {
                                            boolean ok1 = db.saveUser(controller.first_name_reg_admin.getText(), controller.last_name_reg_admin.getText(), controller.address_reg_admin.getText(), controller.emailaddress_reg_admin.getText(), controller.phone_number_reg_admin.getText(), controller.password_reg_admin.getText(), true);
                                            if(ok1) {
                                                try {
                                                    db.setProfilePicture("application/profiles/user.png", controller.registration_email.getText());
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                                registered = true;
                                            } else {
                                                errorHandler.displayMessage(controller.registration_error_admin, "Not registered! (Email exists!)", true);
                                            }
                                        } else{
                                            boolean ok2 = db.saveUser(controller.first_name_reg_admin.getText(), controller.last_name_reg_admin.getText(), controller.address_reg_admin.getText(), controller.emailaddress_reg_admin.getText(), controller.phone_number_reg_admin.getText(), controller.password_reg_admin.getText(), false);
                                            if(ok2) {
                                                try {
                                                    db.setProfilePicture("application/profiles/user.png", controller.registration_email.getText());
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                                registered = true;
                                            } else {
                                                errorHandler.displayMessage(controller.registration_error_admin, "Not registered! (Email exists!)", true);
                                            }
                                        }
                                    }else {
                                        errorHandler.displayMessage(controller.email_issue_reg_admin, "Type issue [email]", true);
                                    }
                                }else {
                                    errorHandler.displayMessage(controller.confirm_password_issue_reg_admin, "Mach issue [confirm password]", true);
                                }
                            }else {
                                errorHandler.displayMessage(controller.password_issue_reg_admin, "Size issue 8-20", true);
                            }
                        }else {
                            errorHandler.displayMessage(controller.phone_number_issue_reg_admin, "Size issue 10 digit", true);
                        }
                    } else {
                        errorHandler.displayMessage(controller.email_issue_reg_admin, "Size issue 6-30", true);
                    }
                }else {
                    errorHandler.displayMessage(controller.address_issue_reg_admin, "Size issue 5-60", true);
                }
            } else {
                errorHandler.displayMessage(controller.last_name_issue_reg_admin, "Size issue 3-30", true);
            }
        } else {
            errorHandler.displayMessage(controller.name_issue_reg_admin, "Size issue 3-30", true);
        }
        return registered;
    }

    /**
     * This method is called when register button is clicked.
     * @param e handles an event on button.
     * @param controller connects to variables and methods.
     * @author Obed. Developed by Sossio.
     */
    public void registerUserAdminBtnAction(ActionEvent e, Controller controller) throws SQLException {
        boolean ok = controller.registerAdmin.registerUserAdmin(e);
        if (ok){
            controller.adminControl.updateMemberTable();
            controller.admin_members_anchorpane.toFront();
            errorHandler.displayMessage(controller.registration_error_admin, "User successfully registered!", false);
            controller.first_name_reg_admin.setText("");
            controller.last_name_reg_admin.setText("");
            controller.first_name_reg_admin.setText("");
            controller.address_reg_admin.setText("");
            controller.emailaddress_reg_admin.setText("");
            controller.phone_number_reg_admin.setText("");
            controller.password_reg_admin.setText("");
            controller.confirm_password_reg_admin.setText("");
        }
    }
}
