package application.Components;
import application.Controller;
import application.model.ConfirmActions;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * This class handles support path, where users can send email to admin.
 */
public class Support {
    private Controller controller;
    private ConfirmActions confirmActions;

    /**
     * Class constructor.
     * @param controller connects all methods and variables.
     */
    public Support(Controller controller){
        this.controller = controller;
        confirmActions = new ConfirmActions(controller);
    }

    /**
     * This method sends specific email to chosen part such as report issue, feedback and contact.
     * Method also handles error if text fields is empty etc.
     * @param e is event listener for buttons
     * @author Sossio and Khabib.
     */
    public void supportInfo(ActionEvent e){
        System.out.println(e.getSource());
        System.out.println(controller.issue_btn_sup);

        if (e.getSource() == controller.issue_btn_sup){
            activeBtn();
            controller.issue_btn_sup.setStyle("-fx-background-color: #ff7000; -fx-text-fill: #fff");
            controller.issue_panel_sup.toFront();

        }else if(e.getSource() == controller.feedback_btn_sup){
            activeBtn();
            controller.feedback_btn_sup.setStyle("-fx-background-color: #ff7000; -fx-text-fill: #fff");
            controller.feedback_panel_sup.toFront();

        }else if(e.getSource() == controller.contact_btn_sup){
            activeBtn();
            controller.contact_btn_sup.setStyle("-fx-background-color: #ff7000; -fx-text-fill: #fff");
            controller.contact_panel_sup.toFront();
        } else if(e.getSource() == controller.send_issue_btn_sup) {

            String title = controller.title_issue_txt_sup.getText();
            String email = controller.email_issue_txt_sup.getText();
            String msg = controller.msg_issue_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(Purchase.sendSupportEmail(email, title, msg)) {
                    Purchase.sendAutoConfirmEmailSupport(email);
                    confirmActions.displayMessage(controller.sup_report_display_msg, "Report issue sent!", false);
                    resetText(controller.title_issue_txt_sup, controller.email_issue_txt_sup, controller.msg_issue_txt_sup);
                }
            } else {
                confirmActions.displayMessage(controller.sup_report_display_msg, "Empty fields!", true);
            }

        } else if(e.getSource() == controller.send_fb_btn_sup) {

            String title = controller.subject_fb_txt_sup.getText();
            String email = controller.email_fb_txt_sup.getText();
            String msg = controller.msg_fb_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(Purchase.sendSupportEmail(email, title, msg)) {
                    Purchase.sendAutoConfirmEmailSupport(email);
                    confirmActions.displayMessage(controller.sup_feedback_display_msg, "Feedback sent!", false);
                    resetText(controller.subject_fb_txt_sup, controller.email_fb_txt_sup, controller.msg_fb_txt_sup);
                }
            } else {
                confirmActions.displayMessage(controller.sup_feedback_display_msg, "Empty fields!", true);
            }

        } else if(e.getSource() == controller.send_contact_btn_sup) {

            String title = controller.subject_contact_txt_sup.getText();
            String email = controller.email_contact_txt_sup.getText();
            String msg = controller.msg_contact_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(Purchase.sendSupportEmail(email, title, msg)) {
                    Purchase.sendAutoConfirmEmailSupport(email);
                    confirmActions.displayMessage(controller.sup_contact_display_msg, "Contact sent!", false);
                    resetText(controller.subject_contact_txt_sup, controller.email_contact_txt_sup, controller.msg_contact_txt_sup);
                }
            } else {
                confirmActions.displayMessage(controller.sup_contact_display_msg, "Empty fields!", true);
            }

        }
    }

    /**
     * This method shows which button is active.
     * @author Khabib and Sossio.
     */
    private void activeBtn() {
        controller.issue_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #fff");
        controller.feedback_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #fff");
        controller.contact_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #fff");
    }

    /**
     * This method checks if text fields is empty or not.
     * @param title text.
     * @param email text.
     * @param msg text.
     * @return true if its not empty.
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
     * This method will reset all text in text fields.
     * @param textField1 fxml id.
     * @param textField2 fxml id.
     * @param textArea fxml id.
     * @author Sossio.
     */
    private void resetText(TextField textField1, TextField textField2, TextArea textArea) {
        textField1.setText("");
        textField2.setText("");
        textArea.setText("");
    }
}
