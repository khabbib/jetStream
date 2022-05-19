package application.components.admin;

import application.Controller;
import application.api.Db;
import application.components.flight.Flight;
import application.components.user.User;
import application.components.ticket.UserHistory;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminControl {
    private Controller controller;
    private Db db;

    public AdminControl(Controller controller, Db db) {
        this.controller = controller;
        this.db = db;
    }

    /**
     * This metod switches the user from the login page to admin page. The metod authenticates if the user is admin from the database
     * if it is true, the admin page is rendered
     * @param e
     * @param controller
     * @author Obed
     */
    public void switchToAdminView(ActionEvent e, Controller controller) {
        if (!controller.login_pass.getText().isEmpty() && !controller.login_email.getText().isEmpty()) {
            try {
                User user = db.authenticationAdmin(controller.login_email.getText(), controller.login_pass.getText());
                if (user != null) {
                    controller.playSystemSound("Login", "sounds/login.wav");
                    controller.main_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    controller.main_scene = new Scene(controller.root);
                    controller.main_stage.setTitle("Admin window");
                    controller.main_stage.setScene(controller.main_scene);
                    controller.main_stage.show();


                    controller.adminControl.fillMemmbersTable(controller.root);
                    controller.adminControl.fillTicketTable(controller.root);
                    controller.adminControl.fillTableFlights(controller.root);



                } else {
                    controller.error_message_lbl.setText("Wrong email or pass!");
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(a -> controller.error_message_lbl.setText(null));
                    pause.play();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            controller.error_message_lbl.setText("Fill the field!");
        }
    }


    /**
     *This metod creates a table view of members. Every column get its infomation from the database table userr
     * inside this metod there is also delete function that checks if a checkbox is selected or not
     * if selected the piece of infomation can be deleted from databse
     * There is also a updated object inside the metod, that updates the tableview if refresh button is selected
     *  @param root
     * @throws SQLException
     */
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


        controller.table_member_admin.getSelectionModel().selectedItemProperty().addListener((ObservableList, oldValue, newValue) -> {

            if (newValue != null) {
                if (newValue.getBox().isSelected()) {
                    newValue.getBox().setSelected(false);
                } else
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
                controller.items_member_admin = controller.table_member_admin.getItems();
                boolean selectedAllItems = false; // selected or not

                for (User item : controller.items_member_admin) {
                    if (controller.select_col_mbr_admin.isSelected()) {
                        selectedAllItems = true;
                        item.getBox().setSelected(true);
                        System.out.println(item.getBox().isSelected() + " state");
                    } else {
                        selectedAllItems = false;
                        item.getBox().setSelected(false);
                        System.out.println(item.getBox().isSelected() + " state");
                    }
                }

                if (selectedAllItems) { // check if all items are selected
                    controller.delet_btn_mbr_admin.setDisable(false);


                } else {
                    controller.delet_btn_mbr_admin.setDisable(true);
                }

            }
        });


    }

    //This metod updates membertabel
    public void updateMemberTable() throws SQLException {
        ArrayList<User> list = db.getAllUsers();

        controller.items_member_admin = FXCollections.observableArrayList(list);
        controller.table_member_admin.setItems(controller.items_member_admin);
    }

    public void fillTicketTable(Parent root) throws SQLException {

        controller.select_col_ticket_admin = (CheckBox) root.lookup("#select_col_ticket_admin");
        controller.deleteTicketBtn_ticket_admin = (Button) root.lookup("#deleteTicketBtn_ticket_admin");
        controller.table_tickets = (TableView<UserHistory>) root.lookup("#table_tickets");

        controller.table_tickets.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("no_col_table_historik"));
        controller.table_tickets.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("company_col_table_historik"));
        controller.table_tickets.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("model_col_table_historik"));
        controller.table_tickets.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("rfc_col_table_historik"));
        controller.table_tickets.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("flightid_col_table_historik"));
        controller.table_tickets.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("from_col_table_historik"));
        controller.table_tickets.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("to_col_table_historik"));
        controller.table_tickets.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("seatno_col_table_historik"));
        controller.table_tickets.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("date_col_table_historik"));
        controller.table_tickets.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("price_col_table_historik"));
        controller.table_tickets.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("select_col_table_historik"));


        controller.table_tickets.getSelectionModel().selectedItemProperty().addListener((ObservableList, oldValue, newValue) -> {

            if (newValue != null) {
                if (newValue.getSelect_col_table_historik().isSelected()) {
                    newValue.getSelect_col_table_historik().setSelected(false);
                } else
                    newValue.getSelect_col_table_historik().setSelected(true);
                System.out.println("selected item: " + newValue.getSelect_col_table_historik() + " value: " + newValue.getSelect_col_table_historik().isSelected());
            }
            controller.deleteTicketBtn_ticket_admin.setDisable(true);

        });

        updateTicketTabel();
        controller.select_col_ticket_admin.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("selected all");
                controller.items_ticket_admin = controller.table_tickets.getItems();
                boolean selectedAllItems = false; // selected or not

                for (UserHistory item : controller.items_ticket_admin) {
                    if (controller.select_col_ticket_admin.isSelected()) {
                        selectedAllItems = true;
                        item.getSelect_col_table_historik().setSelected(true);
                        System.out.println(item.getSelect_col_table_historik().isSelected() + " state");
                    } else {
                        selectedAllItems = false;
                        item.getSelect_col_table_historik().setSelected(false);
                        System.out.println(item.getSelect_col_table_historik().isSelected() + " state");
                    }
                }

                if (selectedAllItems) { // check if all items are selected
                    controller.deleteTicketBtn_ticket_admin.setDisable(false);

                } else {
                    controller.deleteTicketBtn_ticket_admin.setDisable(true);
                }

            }
        });

    }
    public void updateTicketTabel () throws SQLException {
        ArrayList<UserHistory> list = db.searchDataForTableHistory(-1, null, true);

        controller.items_ticket_admin = FXCollections.observableArrayList(list);
        controller.table_tickets.setItems(controller.items_ticket_admin);
    }

    public void fillTableFlights(Parent root) throws SQLException {

        controller.select_all_box_flight_admin = (CheckBox) root.lookup("#select_col_flight_admin");
        controller.delete_singelFlightBtn_admin = (Button) root.lookup("#delete_singelFlightBtn_admin");
        controller.table_flight_admin = (TableView<Flight>) root.lookup("#table_flight_admin");

        controller.table_flight_admin.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("size"));
        controller.table_flight_admin.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.table_flight_admin.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("departure_name"));
        controller.table_flight_admin.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("departure_date"));
        controller.table_flight_admin.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("departure_time"));
        controller.table_flight_admin.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("destination_name"));
        controller.table_flight_admin.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("destination_date"));
        controller.table_flight_admin.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("destination_time"));
        controller.table_flight_admin.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("price"));
        controller.table_flight_admin.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("rTur"));
        controller.table_flight_admin.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("p_id"));
        controller.table_flight_admin.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("box"));


        controller.table_flight_admin.getSelectionModel().selectedItemProperty().addListener((ObservableList, oldValue, newValue) -> {

            if (newValue != null) {
                if (newValue.getFlightBox().isSelected()) {
                    newValue.getFlightBox().setSelected(false);
                } else
                    newValue.getFlightBox().setSelected(true);
                System.out.println("selected item: " + newValue.getFlightBox() + " value: " + newValue.getFlightBox().isSelected());
            }
            controller.delete_singelFlightBtn_admin.setDisable(true);

        });

        updateFlightTable();
        controller.select_col_flight_admin.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("selected all");
                controller.items_flight_admin = controller.table_flight_admin.getItems();
                boolean selectedAllItems = false; // selected or not

                for (Flight item : controller.items_flight_admin) {
                    if (controller.select_col_flight_admin.isSelected()) {
                        selectedAllItems = true;
                        item.getFlightBox().setSelected(true);
                        System.out.println(item.getFlightBox().isSelected() + " state");
                    } else {
                        selectedAllItems = false;
                        item.getFlightBox().setSelected(false);
                        System.out.println(item.getFlightBox().isSelected() + " state");
                    }
                }

                if (selectedAllItems) { // check if all items are selected
                    controller.delete_singelFlightBtn_admin.setDisable(false);

                } else {
                    controller.delete_singelFlightBtn_admin.setDisable(true);
                }

            }
        });


    }

    public void updateFlightTable() throws SQLException {
        ArrayList<Flight> list = db.getAllFlights();

        controller.items_flight_admin = FXCollections.observableArrayList(list);
        controller.table_flight_admin.setItems(controller.items_flight_admin);
    }






}
