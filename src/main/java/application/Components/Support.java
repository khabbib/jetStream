package application.Components;
import application.Controller;
import javafx.event.ActionEvent;

/**
 * This class handles support path.
 */
public class Support {
    private Controller controller;

    /**
     * @param controller
     */
    public Support(Controller controller){
        this.controller = controller;
    }

    /**
     * @param e
     */
    public void supportInfo(ActionEvent e){
        System.out.println(e.getSource());
        System.out.println(controller.issue_btn_sup);
        if (e.getSource() == controller.issue_btn_sup){
            System.out.println("issue_btn clicked");
            controller.issue_btn_sup.setStyle("-fx-background-color: #eee; -fx-text-fill: #112");
            controller.feedback_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #eee");
            controller.contact_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #eee");
            controller.issue_panel_sup.toFront();
        }else if(e.getSource() == controller.feedback_btn_sup){
            controller.feedback_btn_sup.setStyle("-fx-background-color: #eee; -fx-text-fill: #112");
            controller.issue_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #eee");
            controller.contact_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #eee");
            controller.feedback_panel_sup.toFront();
        }else if(e.getSource() == controller.contact_btn_sup){
            controller.contact_panel_sup.toFront();
            controller.contact_btn_sup.setStyle("-fx-background-color: #eee; -fx-text-fill: #112");
            controller.feedback_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #eee");
            controller.issue_btn_sup.setStyle("-fx-background-color: #333; -fx-text-fill: #eee");
        }
    }
}
