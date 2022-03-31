package application;

import application.Model.Book;
import application.Model.FlygResa;
import application.Model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.LightBase;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;


public class NewScene {

    public static ArrayList<FlygResa> showNewScene(String title, ArrayList<FlygResa> resor) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(400);
        ArrayList<FlygResa> output = new ArrayList<>();

        ListView<FlygResa> listv = new ListView<>();
        if (resor != null) {
            StringBuilder info = new StringBuilder();
            for (FlygResa item : resor){
                listv.getItems().add(item);
                //listv.getItems().add("From: " + item.getFrom() + ", to: " + item.getDistination() + ", date: " +item.getDate());
                //info.append("From: ").append(item.getDate()).append(" destination: ").append(item.getDistination()).append(" date: ").append(item.getDate()).append("\n");
            }
            Label list = new Label();
            list.setText(info.toString());
            Button closeBtn = new Button("Chose Flight");
            closeBtn.setOnAction(e -> stage.close());

            listv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FlygResa>() {
                @Override
                public void changed(ObservableValue<? extends FlygResa> observable, FlygResa oldValue, FlygResa newValue) {
                    output.add(listv.getSelectionModel().getSelectedItem());
                }
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(listv, closeBtn);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            stage.setScene(scene);
            stage.showAndWait();
        }
        System.out.println(output.get(0).getFrom() + " valde");
        return output;
    }
}
