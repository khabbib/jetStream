package application.Components.AdminComponents;

import application.Controller;
import application.database.Connection;
import application.model.User;
import application.model.UserHistory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminControl {
    private Controller controller;
    private Connection connection;
    public AdminControl(Controller controller, Connection connection){
        this.controller = controller;
        this.connection = connection;
    }


    public void fillMemmbersTable(Parent root) throws SQLException {

        controller.select_col_mbr_admin = (CheckBox) root.lookup("#select_col_mbr_admin");
        controller.delet_btn_mbr_admin = (Button) root.lookup("#delet_btn_mbr_admin");
        controller.table_member_admin = (TableView<User>) root.lookup("#table_member_admin");

        controller.table_member_admin.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("size"));

        controller.table_member_admin.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("userId"));
        controller.table_member_admin.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("firstName"));
        controller.table_member_admin.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("lastName"));
        controller.table_member_admin.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("address"));
        controller.table_member_admin.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("email"));
        controller.table_member_admin.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        controller.table_member_admin.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("isadmin"));
        controller.table_member_admin.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("box"));


        controller.table_member_admin.getSelectionModel().selectedItemProperty().addListener((ObservableList, oldValue, newValue) ->{

            if (newValue != null){
                if (newValue.getBox().isSelected()){
                    newValue.getBox().setSelected(false);
                }else
                    newValue.getBox().setSelected(true);
                System.out.println("selected item: " + newValue.getBox() + " value: " + newValue.getBox().isSelected());
            }
            controller.deletS_btn_mbr_admin.setDisable(true);

        });

        updateMemberTable();
        controller.select_col_mbr_admin.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("selected all");
                controller.items_admin = controller.table_member_admin.getItems();
                boolean selectedAllItems = false; // selected or not

                for (User item : controller.items_admin){
                    if (controller.select_col_mbr_admin.isSelected()){
                        selectedAllItems = true;
                        item.getBox().setSelected(true);
                        System.out.println(item.getBox().isSelected() + " state");
                    }else {
                        selectedAllItems = false;
                        item.getBox().setSelected(false);
                        System.out.println(item.getBox().isSelected() + " state");
                    }
                }

                if (selectedAllItems){ // check if all items are selected
                    controller.delet_btn_mbr_admin.setDisable(false);

                }else {
                    controller.deletS_btn_mbr_admin.setDisable(true);
                }

            }
        });






    }

    public void updateMemberTable() throws SQLException {
        ArrayList<User> list = connection.getAllUsers();

        controller.fetchedList_admin = FXCollections.observableArrayList(list);
        controller.table_member_admin.setItems(controller.fetchedList_admin);
    }
}
