package application.components.user;
import application.Controller;
import application.ErrorHandler;
import application.components.Email.SupportEmail;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * This class handles support windows, where users can send email to admin.
 */
public class Support {
    private Controller controller;
    private ErrorHandler errorHandler;

    /**
     * Constructor to Support.
     * @param controller instance of control class
     * @author Habib
     */
    public Support(Controller controller){
        this.controller = controller;
        errorHandler = new ErrorHandler(controller);
    }

    /**
     * This method sends specific email to chosen part such as report issue, feedback and contact.
     * Method also handles error if text fields is empty etc.
     * @param e is event listener for buttons
     * @author Khabib developed by sossio.
     */
    public void supportInfo(ActionEvent e){
        System.out.println(e.getSource());
        System.out.println(controller.issue_btn_sup);

        if (e.getSource() == controller.issue_btn_sup){
            unactiveBtn();
            controller.issue_btn_sup.setStyle("-fx-background-color: #ff7000; -fx-text-fill: #fff");
            controller.issue_panel_sup.toFront();

        }else if(e.getSource() == controller.feedback_btn_sup){
            unactiveBtn();
            controller.feedback_btn_sup.setStyle("-fx-background-color: #ff7000; -fx-text-fill: #fff");
            controller.feedback_panel_sup.toFront();

        }else if(e.getSource() == controller.contact_btn_sup){
            unactiveBtn();
            controller.contact_btn_sup.setStyle("-fx-background-color: #ff7000; -fx-text-fill: #fff");
            controller.contact_panel_sup.toFront();
        } else if(e.getSource() == controller.send_issue_btn_sup) {

            String title = controller.title_issue_txt_sup.getText();
            String email = controller.email_issue_txt_sup.getText();
            String msg = controller.msg_issue_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(SupportEmail.send(email, title, msg)) {
                    SupportEmail.sendAutoEmail(email);
                    errorHandler.displayMessage(controller.sup_report_display_msg, "Report issue sent!", false);
                    resetText(controller.title_issue_txt_sup, controller.email_issue_txt_sup, controller.msg_issue_txt_sup);
                }
            } else {
                errorHandler.displayMessage(controller.sup_report_display_msg, "Empty fields!", true);
            }

        } else if(e.getSource() == controller.send_fb_btn_sup) {

            String title = controller.subject_fb_txt_sup.getText();
            String email = controller.email_fb_txt_sup.getText();
            String msg = controller.msg_fb_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(SupportEmail.send(email, title, msg)) {
                    SupportEmail.sendAutoEmail(email);
                    errorHandler.displayMessage(controller.sup_feedback_display_msg, "Feedback sent!", false);
                    resetText(controller.subject_fb_txt_sup, controller.email_fb_txt_sup, controller.msg_fb_txt_sup);
                }
            } else {
                errorHandler.displayMessage(controller.sup_feedback_display_msg, "Empty fields!", true);
            }

        } else if(e.getSource() == controller.send_contact_btn_sup) {

            String title = controller.subject_contact_txt_sup.getText();
            String email = controller.email_contact_txt_sup.getText();
            String msg = controller.msg_contact_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(SupportEmail.send(email, title, msg)) {
                    SupportEmail.sendAutoEmail(email);
                    errorHandler.displayMessage(controller.sup_contact_display_msg, "Contact sent!", false);
                    resetText(controller.subject_contact_txt_sup, controller.email_contact_txt_sup, controller.msg_contact_txt_sup);
                }
            } else {
                errorHandler.displayMessage(controller.sup_contact_display_msg, "Empty fields!", true);
            }

        }
    }

    /**
     * This method shows which button is active.
     * @author Khabib and Sossio.
     */
    private void unactiveBtn() {
        controller.issue_btn_sup.setStyle("-fx-background-color: #221E4E; -fx-text-fill: #fff");
        controller.feedback_btn_sup.setStyle("-fx-background-color: #221E4E; -fx-text-fill: #fff");
        controller.contact_btn_sup.setStyle("-fx-background-color: #221E4E; -fx-text-fill: #fff");
    }

    /**
     * This method checks if text fields is empty or not.
     * @param title title : string
     * @param email email address : string
     * @param msg message : string
     * @return return a true or false value after checking : boolean
     * @author Sossio.
     */
    private boolean checkFields(String title, String email, String msg) {
        boolean ok = false;
        if(!title.isEmpty() && !email.isEmpty() && !msg.isEmpty()) {
            ok = true;
        }
        return ok;
    }

    /**
     * This method will reset all text fields in support form.
     * @param textField1 text field 1 : FXML textField
     * @param textField2 text field 2 : FXML textField
     * @param textArea text field 3 : FXML textField
     * @author Sossio.
     */
    private void resetText(TextField textField1, TextField textField2, TextArea textArea) {
        textField1.setText("");
        textField2.setText("");
        textArea.setText("");
    }
}
