package application.model;

import application.Controller;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Optional;

/**
 * #comment (comment this class and create javadoc to every method)
 *  This class is used to warn if the user really want to confirm an action.
 */
public class ConfirmActions {
    private Controller controller;

    /**
     * @param controller
     */
    public ConfirmActions(Controller controller){
        this.controller = controller;
    }

    /**
     * The following method will open a confirmation window to the user to confirm an action.
     * @param title // is the conformation windows title
     * @param message // is a question to the user if the user willing to confirm the action or not.
     * @param content // is more detail information about the actual action
     * @return // return a status of true or false which means if the user confirm the action or not.
     */
    public boolean confirmThisAction(String title, String message, String content){
        boolean confirm = false;
        if (title != null && message != null && content != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(message);
            alert.setContentText(content);
            Image img = new Image("application/image/warning.png");
            ImageView icon = new ImageView(img);
            icon.setFitHeight(50);
            icon.setFitWidth(50);
            alert.getDialogPane().setGraphic(icon);

            Optional<ButtonType> option = alert.showAndWait();
            option.get();
            if (option.get() == ButtonType.CANCEL) {
                return false;
            } else if (option.get() == ButtonType.OK) {
                confirm = true;
            }
        }

        return confirm;
    }

    /**
     * User global notification
     * @param box
     * @param label
     * @param msg
     * @author Khabib.
     */
    public void notifyError(Pane box, Label label, String msg) {
        box.setVisible(true);
        label.setText(msg);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(a -> {
            box.setVisible(false);
            label.setText(null);
        });
        pause.play();

    }

    /**
     * Display error on screen by its tag
     * @param label
     * @param msg
     * @param isError
     * @author Khabib. Developed by Sossio and Khabib.
     */
    public void displayMessage(Label label, String msg, boolean isError) {
        if (label != null){
            if(isError){
                label.setStyle("-fx-text-fill:red");
            } else {
                label.setStyle("-fx-text-fill:green");
            }
            label.setText(msg);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(a -> label.setText(null));
            pause.play();
        }else {
            System.out.println("SUCESS REGISTRATION! display msg on screen!");
        }
    }
}
