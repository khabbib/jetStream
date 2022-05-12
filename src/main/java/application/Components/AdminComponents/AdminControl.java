package application.Components.AdminComponents;

import application.Controller;
import application.database.Connection;
import application.model.Book;
import application.model.Flight;
import application.model.User;
import application.model.UserHistory;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class AdminControl {
    private Controller controller;
    private Connection connection;

    public AdminControl(Controller controller, Connection connection) {
        this.controller = controller;
        this.connection = connection;
    }

    public void switchToAdminView(ActionEvent e, Controller controller) {
        if (!controller.login_pass.getText().isEmpty() && !controller.login_email.getText().isEmpty()) {
            try {
                User user = connection.authenticationAdmin(controller.login_email.getText(), controller.login_pass.getText());
                if (user != null) {

                    controller.main_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    controller.main_scene = new Scene(controller.root);
                    controller.main_stage.setTitle("Admin window");
                    controller.main_stage.setScene(controller.main_scene);
                    controller.main_stage.show();


                    controller.adminControl.fillMemmbersTable(controller.root);
                    controller.adminControl.fillTicketTable(controller.root);
                    controller.adminControl.fillTableFlights(controller.root);

                    controller.member_listview = (ListView<String>) controller.root.lookup("#member_listview");
                    if(controller.member_listview != null)
                    {
                        ArrayList<User> member = connection.getAllUsers();
                        ArrayList<String> temp = new ArrayList<>();
                        int pageNr = 0;
                        for(User item: member)
                        {
                            pageNr++;
                            StringBuilder temp2 = new StringBuilder();
                            System.out.println(item.isIsadmin() + " Obedddddd ");
                            temp2.append(pageNr).append(" Member[ id. ").append(item.getUserId()).append(", First Name: ").append(item.getFirstName()).append(", List Name: ").append(item.getLastName()).append(", Adress: ").append(item.getAddress()).append(", Email: ").append(item.getEmail()).append(", Number: ").append(item.getPhoneNumber()).append(", Password: ").append(item.getPassword()).append(", isAdmin: ").append(item.isIsadmin()).append(" ]");
                            temp.add(temp2.toString());
                        }

                        ObservableList<String> tickets = FXCollections.observableList(temp);
                        controller.member_listview.setItems(tickets);

                    }

                    controller.ticket_listview = (ListView<String>) controller.root.lookup("#ticket_listview");
                    if(controller.ticket_listview != null)
                    {


                        ArrayList<Book> ticket = connection.searchTicket();
                        ArrayList<String> temp = new ArrayList<>();
                        for(Book item: ticket)
                        {
                            StringBuilder temp2 = new StringBuilder();
                            temp2.append("Ticket[ user. ").append(item.getUser_id()).append(", flightid: ").append(item.getFlight_id()).append(", seat number: ").append(item.getSeatNbr()).append(" ]");
                            temp.add(temp2.toString());
                        }
                        ObservableList<String> tickets = FXCollections.observableList(temp);
                        controller.ticket_listview.setItems(tickets);
                    }
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

    //This metod fills members infomation from database to columns
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
                    controller.deletS_btn_mbr_admin.setDisable(true);
                }

            }
        });


    }

    //This metod updates membertabel
    public void updateMemberTable() throws SQLException {
        ArrayList<User> list = connection.getAllUsers();

        controller.fetchedList_member_admin = FXCollections.observableArrayList(list);
        controller.table_member_admin.setItems(controller.fetchedList_member_admin);
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
        ArrayList<UserHistory> list = connection.searchDataForTableHistory(0, true);

        controller.fetchedList_ticket_admin = FXCollections.observableArrayList(list);
        controller.table_tickets.setItems(controller.fetchedList_ticket_admin);
    }

    public void fillTableFlights(Parent root) throws SQLException {

        controller.select_all_box_flight_admin = (CheckBox) root.lookup("#select_all_box_flight_admin");
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
        controller.table_flight_admin.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("select_col_table_flight"));


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
        controller.select_all_box_flight_admin.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("selected all");
                controller.items_flight_admin = controller.table_flight_admin.getItems();
                boolean selectedAllItems = false; // selected or not

                for (Flight item : controller.items_flight_admin) {
                    if (controller.select_all_box_flight_admin.isSelected()) {
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
        ArrayList<Flight> list = connection.getAllFlights();

        controller.fetchedList_flight_admin = FXCollections.observableArrayList(list);
        controller.table_flight_admin.setItems(controller.fetchedList_flight_admin);
    }
}
