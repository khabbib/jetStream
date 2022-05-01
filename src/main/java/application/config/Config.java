package application.config;

import application.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Config {
    private Controller controller;
    private Stage stage;
    private Scene scene;
    private Parent root;


    public Config(Controller controller,Parent root,Stage stage ){
        this.controller = controller;
        this.root = root;
        this.stage = stage;
    }


    //////// FXML RENDER //////////
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

    public Parent getRoot() {
        return this.root;
    }
}
