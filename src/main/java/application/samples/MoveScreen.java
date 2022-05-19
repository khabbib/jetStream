package application.samples;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 * This is a test class to move a scene on click.
 * @author Khabib.
 */
public class MoveScreen {
    private static double xOffset =0;
    private static double yOffset =0;

    /**
     * Class constructor.
     * @param root ir root.
     * @param stage is parent stage.
     * @author Khabib
     */
    public MoveScreen(Parent root, Stage stage){
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        //move around here
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    /**
     * Main method to run the sample
     * @param args argument
     * @author Khabib
     */
    public static void main(String[]args){
        new MoveScreen(null, null); // Need to be replaced with real arguments
    }

}
