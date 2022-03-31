package application.Model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;

public class SiteManager {
    public static int addSitePlace(int antalPlats) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Chose a site place");
        stage.setMinWidth(1000);
        stage.setMinHeight(400);

        int output = -1;
        GridPane grid = new GridPane();
        grid.addColumn(10);
        grid.addRow(10);
        for (int i = 0; i <= antalPlats; i++){
            for (int r = 0; r <= antalPlats/10; r++){
                Label sit = new Label("" + i);
                grid.add(sit, i,r);

            }
            // Image img = new Image("jetStream.png");
        }

        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
        return 33;
    };
}
