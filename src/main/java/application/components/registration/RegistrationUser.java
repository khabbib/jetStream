package application.components.registration;

import application.Controller;
import application.ErrorHandler;
import application.config.Config;
import application.api.Db;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.sql.SQLException;

/**
 * This class registers only users and handles errors.
 */
public class RegistrationUser {
    private ErrorHandler errorHandler;
    private Controller controller;
    private Config config;
    private Db db;

    /**
     * Class constructor.
     * @param controller connects variables and methods.
     * @param db connects to database.
     * @param config connects to root.
     *
     */
    public RegistrationUser(Controller controller, Db db, Config config){
        errorHandler = new ErrorHandler(controller);
        this.controller = controller;
        this.config = config;
        this.db = db;
    }

    /**
     * This method registers a user to database.
     * It checks if all text fields is verified with its specific limitation.
     * @return boolean.
     * @throws SQLException if any sql issue occur.
     * @author Khabib Developed by Sossio.
     */
    public boolean registerUser() {
        boolean registered = false;
        if (ErrorHandler.validateInputLimit(controller.registration_first_name, "firstname")){
            if (ErrorHandler.validateInputLimit(controller.registration_last_name, "lastname")){
                if (ErrorHandler.validateInputLimit(controller.registration_address, "address")){
                    if(ErrorHandler.validateInputLimit(controller.registration_email, "email")){
                        if (ErrorHandler.validateInputLimit(controller.registration_phone_number, "phone")){
                            if (ErrorHandler.validateInputLimit(controller.registration_password, "password")){
                                if (controller.registration_password.getText().equals(controller.registration_confirm_password.getText())){
                                    if(ErrorHandler.validateInputLimit(controller.registration_email, "email-format")){
                                        boolean ok = db.saveUser(controller.registration_first_name.getText(), controller.registration_last_name.getText(), controller.registration_address.getText(), controller.registration_email.getText(), controller.registration_phone_number.getText(), controller.registration_password.getText(), false);
                                        if (ok) {
                                            try {
                                                db.setProfilePicture("application/profiles/user.png", controller.registration_email.getText());
                                            } catch (SQLException ex) {
                                                ex.printStackTrace();
                                            }
                                            registered = true;
                                        } else {
                                            errorHandler.displayMessage(controller.registration_error_lbl, "Not registered! (Email exists!)", true);
                                        }
                                    }else {
                                        errorHandler.displayMessage(controller.email_issue_reg, "Type issue [email]", true);
                                    }
                                }else {
                                    errorHandler.displayMessage(controller.confirm_password_issue_reg, "Mach issue!", true);
                                }
                            }else {
                                errorHandler.displayMessage(controller.password_issue_reg, "Size issue 8-20", true);
                            }
                        }else {
                            errorHandler.displayMessage(controller.phone_number_issue_reg, "Size issue 10 digit", true);
                        }
                    } else {
                        errorHandler.displayMessage(controller.email_issue_reg, "Size issue 6-30", true);
                    }
                }else {
                    errorHandler.displayMessage(controller.address_issue_reg, "Size issue 5-60", true);
                }
            } else {
                errorHandler.displayMessage(controller.last_name_issue_reg, "Size issue 3-30", true);
            }
        } else {
            errorHandler.displayMessage(controller.name_issue_reg, "Size issue 3-30", true);
        }
        return registered;
    }

    /**
     * This method is called when register button is clicked.
     * @param e handles an event on button.
     * @param controller connects to variables and methods.
     * @author Khabib. Developed by Sossio.
     */
    public void registerUserBtnAction(ActionEvent e, Controller controller) {
        boolean ok = registerUser();
        System.out.println("Kommer in i if-satsen!");
        if (ok){
            controller.root = config.render(e, "user/Login", "Login");
            controller.success_msg_lbl = (Label) controller.root.lookup("#success_msg_lbl");
            errorHandler.displayMessage(controller.success_msg_lbl, "User successfully registered!", false);
        } else {
            errorHandler.displayMessage(controller.success_msg_lbl, "Error", true);
        }
    }
}
