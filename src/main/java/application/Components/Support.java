package application.Components;
import application.Controller;
import application.auth.Purchase;
import application.model.ConfirmActions;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * This class handles support path.
 */
public class Support {
    private Controller controller;
    private ConfirmActions confirmActions;

    /**
     * @param controller
     */
    public Support(Controller controller){
        this.controller = controller;
        confirmActions = new ConfirmActions(controller);
    }

    /**
     * @param e
     */
    public void supportInfo(ActionEvent e){
        System.out.println(e.getSource());
        System.out.println(controller.issue_btn_sup);

        if (e.getSource() == controller.issue_btn_sup){
            activeBtn();
            controller.issue_btn_sup.setStyle("-fx-background-color: #eee; -fx-text-fill: #112");
            controller.issue_panel_sup.toFront();

        }else if(e.getSource() == controller.feedback_btn_sup){
            activeBtn();
            controller.feedback_btn_sup.setStyle("-fx-background-color: #eee; -fx-text-fill: #112");
            controller.feedback_panel_sup.toFront();

        }else if(e.getSource() == controller.contact_btn_sup){
            activeBtn();
            controller.contact_btn_sup.setStyle("-fx-background-color: #eee; -fx-text-fill: #112");
            controller.contact_panel_sup.toFront();
        } else if(e.getSource() == controller.send_issue_btn_sup) {

            String title = controller.title_issue_txt_sup.getText();
            String email = controller.email_issue_txt_sup.getText();
            String msg = controller.msg_issue_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(Purchase.sendSupportEmail(email, title, msg)) {
                    Purchase.sendAutoConfirmEmailSupport(email);
                    resetText(controller.title_issue_txt_sup, controller.email_issue_txt_sup, controller.msg_issue_txt_sup);
                }
            } else {
                confirmActions.displayMessage(controller.sup_report_error_msg, "Empty fields!", true);
            }

        } else if(e.getSource() == controller.send_fb_btn_sup) {

            String title = controller.subject_fb_txt_sup.getText();
            String email = controller.email_fb_txt_sup.getText();
            String msg = controller.msg_fb_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(Purchase.sendSupportEmail(email, title, msg)) {
                    Purchase.sendAutoConfirmEmailSupport(email);
                    resetText(controller.subject_fb_txt_sup, controller.email_fb_txt_sup, controller.msg_fb_txt_sup);
                }
            } else {
                confirmActions.displayMessage(controller.sup_feedback_error_msg, "Empty fields!", true);
            }

        } else if(e.getSource() == controller.send_contact_btn_sup) {

            String title = controller.subject_contact_txt_sup.getText();
            String email = controller.email_contact_txt_sup.getText();
            String msg = controller.msg_contact_txt_sup.getText();

            boolean ok = checkFields(title, email, msg);
            if(ok) {
                if(Purchase.sendSupportEmail(email, title, msg)) {
                    Purchase.sendAutoConfirmEmailSupport(email);
                    resetText(controller.subject_contact_txt_sup, controller.email_contact_txt_sup, controller.msg_contact_txt_sup);
                }
            } else {
                confirmActions.displayMessage(controller.sup_contact_error_msg, "Empty fields!", true);
            }

        }
    }

    private void activeBtn() {
        controller.issue_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #112");
        controller.feedback_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #112");
        controller.contact_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #112");
    }

    private boolean checkFields(String title, String email, String msg) {
        boolean ok = false;
        if(!title.isEmpty() && !email.isEmpty() && !msg.isEmpty()) {
            ok = true;
        }
        return ok;
    }

    private void resetText(TextField textField1, TextField textField2, TextArea textArea) {
        textField1.setText("");
        textField2.setText("");
        textArea.setText("");
    }
}
