package application.config;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * This class render FXML file dynamically.
 * @author Khabib
 */
public class Config {
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Constructor to Config
     * @param root
     * @param stage
     * @author Khabib
     */
    public Config(Parent root,Stage stage ){
        this.root = root;
        this.stage = stage;
    }

    /**
     * The method render FXML pages on screen.
     * @param e event
     * @param name name of FXML file
     * @param title a title for the FXML file
     * @return return home root.
     * @author Khabib.
     */
    public Parent render(ActionEvent e, String name, String title){
        try {
            this.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/application/"+name +".fxml")));
            this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            this.scene = new Scene(root);
            //stage.initStyle(StageStyle.TRANSPARENT);
            //MoveScreen.moveScreen(root,stage);
            this.stage.setTitle(title);
            this.stage.setScene(scene);
            this.stage.show();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return root;
    }

}
