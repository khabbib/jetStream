package application;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Optional;

/**
 * This class is used to warn if the user really want to confirm an action.
 */
public class ErrorHandler {
    private Controller controller;

    /**
     * Constructor to ErrorHandler
     * @param controller instance of control class
     */
    public ErrorHandler(Controller controller){
        this.controller = controller;
    }

    /**
     * This method is used for critical actions for instance if a ticket needs to be canceled, it needs a confirmation of user.
     * @param title // is the conformation windows title
     * @param message // is a question to the user if the user willing to confirm the action or not.
     * @param content // is more detail information about the actual action
     * @return // return a status of true or false which means if the user confirm the action or not.
     * @author Habib Mohammad
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
     * This method validates the input from text fields and checks if it's limitation is correct.
     * @param textField comes from fxml text field id.
     * @param limitType indicates which text shall be validated.
     * @return true if it's okay.
     * @author Sossio.
     */
    public static boolean validateInputLimit(TextField textField, String limitType) {

        boolean isTrue = false;

        if ((textField.getText().length() >= 3 && textField.getText().length() <= 30) && limitType == "firstname") {
            isTrue = true;
        }
        else if ((textField.getText().length() >= 3 && textField.getText().length() <= 30) && limitType == "lastname") {
            isTrue = true;
        }
        else if ((textField.getText().length() >= 5 && textField.getText().length() <= 60) && limitType == "address") {
            isTrue = true;
        }
        else if ((textField.getText().length() >= 6 && textField.getText().length() <= 30) && limitType == "email") {
            isTrue = true;
        }
        else if ((textField.getText().length() == 10) && limitType == "phone") {
            isTrue = true;
        }
        else if ((textField.getText().length() >= 8 && textField.getText().length() <= 20) && limitType == "password") {
            isTrue = true;
        }
        else if ((textField.getText().contains("@") && (textField.getText().contains("gmail") || textField.getText().contains("hotmail") || textField.getText().contains("yahoo") || textField.getText().contains("outlook"))) && limitType == "email-format") {
            isTrue = true;
        }

        return isTrue;
    }

    /**
     * Display error or success message in the whole program
     * @param label Label panel : Label
     * @param msg message to display : String
     * @param isError is an error? : boolean
     * @author Khabib. Developed by Sossio and Khabib.
     */
    public void displayMessage(Label label, String msg, boolean isError) {
        if (label != null){
            if(isError){
                label.setStyle("-fx-text-fill:red");
                controller.playSystemSound("Error", "sounds/error.wav");
            } else {
                label.setStyle("-fx-text-fill:green");
                controller.playSystemSound("Success", "sounds/success.wav");
            }
            label.setText(msg);
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(a -> label.setText(null));
            pause.play();
        }else {
            System.out.println("SUCCESS REGISTRATION! display msg on screen!");
        }
    }
}
