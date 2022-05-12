package application.Components;

import application.Controller;
import application.database.Connection;
import application.model.CreateWorld;
import application.model.User;
import application.moveScreen.MoveScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class DashboardController {
    private Controller controller;
    private Parent root;
    private Connection connection;
    public DashboardController(Controller controller, Parent root, Connection connection){
        this.controller = controller;
        this.root = root;
        this.connection = connection;
    }

    /**
     *
     */
    public void toggleMenuColor(Controller controller) {
        controller.menu_highlight_color_flight.setVisible(false);
        controller.menu_highlight_color_history.setVisible(false);
        controller.menu_highlight_color_entertainment.setVisible(false);
        controller.menu_highlight_color_support.setVisible(false);

        controller.map_menu_user_image.setOpacity(0.5);
        controller.history_menu_user_image.setOpacity(0.5);
        controller.entertainment_menu_user_image.setOpacity(0.5);
        controller.support_menu_user_image.setOpacity(0.5);
    }
    public void userInitializeFXML(Parent root, User user){
        // flight list related variables
        controller.nbr_of_available_flights = (Label) root.lookup("#nbr_of_available_flights");
        // global error message for user dashboard
        controller.user_dashboard_msgbox_pane = (Pane)root.lookup("#msgBox_user_dashboard");
        controller.user_notification_lbl = (Label) root.lookup("#notify_user_dashboard");
        // login success message
        //
        // scrollpane seats
        controller.eco_seat_scrollpane = (ScrollPane) root.lookup("#eco_seat_scrollpane");
        controller.business_seat_scrollpane = (ScrollPane) root.lookup("#business_seat_scrollpane");
        controller.eco_seat_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controller.business_seat_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        controller.flight_seats_eco_anchorpane = (AnchorPane) controller.eco_seat_scrollpane.getContent();
        controller.flights_seats_business_anchorpane = (AnchorPane) controller.business_seat_scrollpane.getContent();

        // user menu images
        controller.map_menu_user_image = (ImageView)root.lookup("#map_menu_user");
        controller.entertainment_menu_user_image = (ImageView)root.lookup("#game_menu_user");
        controller.history_menu_user_image = (ImageView)root.lookup("#historik_menu_user");
        controller.support_menu_user_image = (ImageView)root.lookup("#support_menu_user");

        // Success page
        controller.history_reference_number_lbl = (Label) root.lookup("#rfc_no_sucesspnl");

        // Support page
        controller.sup_report_display_msg = (Label) root.lookup("#sup_report_error_msg");
        controller.sup_contact_display_msg = (Label) root.lookup("#sup_contact_error_msg");
        controller.sup_feedback_display_msg = (Label) root.lookup("#sup_feedback_error_msg");

        //registration page
        controller.name_issue_reg = (Label) root.lookup("#name_issue_reg");
        controller.last_name_issue_reg = (Label) root.lookup("#last_name_issue_reg");
        controller.address_issue_reg = (Label) root.lookup("#address_issue_reg");
        controller.email_issue_reg = (Label) root.lookup("#email_issue_reg");
        controller.phone_number_issue_reg = (Label) root.lookup("#phone_number_issue_reg");
        controller.password_issue_reg = (Label) root.lookup("#password_issue_reg");
        controller.confirm_password_issue_reg = (Label) root.lookup("#confirm_password_issue_reg");

        // Purchase info
        controller.card_nbr = (TextField) root.lookup("#card_nbr");
        controller.card_fname = (TextField) root.lookup("#card_fname");
        controller.card_lname = (TextField) root.lookup("#card_lname");
        controller.card_month = (TextField) root.lookup("#card_month");
        controller.card_year = (TextField) root.lookup("#card_year");
        controller.card_cvc = (TextField) root.lookup("#card_cvc");
        controller.card_counter_nbr = (Label) root.lookup("#card_counter_nbr");
        controller.payment_err_msg = (Label) root.lookup("#payment_err_msg");

        // Config scrollbar

        // Passenger info
        controller.booking_first_name_textfield = (TextField) root.lookup("#booking_first_name_textfield");
        controller.booking_last_name_textfield = (TextField) root.lookup("#booking_last_name_textfield");
        controller.booking_four_digits_textfield = (TextField) root.lookup("#booking_four_digits_textfield");
        controller.booking_email_textfield = (TextField) root.lookup("#booking_email_textfield");
        controller.booking_seat_number_lbl = (Label) root.lookup("#booking_seat_number_lbl");
        controller.booking_retur_seat_number_lbl = (Label) root.lookup("#booking_retur_seat_number_lbl");
        controller.booking_msg_lbl = (Label) root.lookup("#booking_msg_lbl");
        controller.booking_flight_number_lbl = (Label) root.lookup("#booking_flight_number_lbl");
        controller.booking_price_lbl = (Label) root.lookup("#booking_price_lbl");
        controller.booking_departure_lbl = (Label) root.lookup("#booking_departure_lbl");
        controller.booking_departure_extra_lbl = (Label) root.lookup("#booking_departure_extra_lbl");
        controller.booking_destination_lbl = (Label) root.lookup("#booking_destination_lbl");
        controller.booking_destination_extra_lbl = (Label) root.lookup("#booking_destination_extra_lbl");
        controller.booking_is_retur_lbl = (Label) root.lookup("#booking_is_retur_lbl");

        // look up for global variables
        controller.username_lbl = (Label) root.lookup("#username_lbl");
        controller.booking_first_name_textfield = (TextField) root.lookup("#booking_first_name_textfield");
        controller.booking_last_name_textfield = (TextField) root.lookup("#booking_last_name_textfield");
        controller.booking_email_textfield = (TextField) root.lookup("#booking_email_textfield");
        //controller.u_id = (Label) root.lookup("#u_id");
        controller.return_date_pick_hbox = (HBox)root.lookup("return_date_pick_hbox");
        controller.search_list_appear = (ListView<String>) root.lookup("#searchListAprear");
        controller.search_list_appear_second = (ListView<String>) root.lookup("#searchListAprear2");
        controller.search_list_appear_third = (ListView<String>) root.lookup("#searchListAprear3");

        controller.booking_seat_anchorpane = (AnchorPane) root.lookup("#booking_seat_anchorpane");
        controller.booking_passenger_anchorpane = (AnchorPane) root.lookup("#booking_passenger_anchorpane");
        controller.flights_scrollpane = (ScrollPane) root.lookup("#flights_scrollpane");
        controller.flights_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controller.flight_display_vbox = (VBox) controller.flights_scrollpane.getContent();
        controller.world_map_scrollpane = (ScrollPane) root.lookup("#world_map_scrollpane");
        controller.search_f_name = (TextField) root.lookup("#search_f_name");
        controller.booking_profile_image = (ImageView) root.lookup("#booking_profile_image");

        // loader in login
        //controller.login_loader_flight = (ImageView) root.lookup("#login_loader_flight");
        //controller.login_loader_flight.setVisible(false); // set loader to false
    }

    public void switchToDashboard(ActionEvent e, Controller controller) throws IOException {
        if (!controller.login_email.getText().isEmpty() || !controller.login_pass.getText().isEmpty()) {
            if (controller.login_email.getText().contains("@") && (controller.login_email.getText().contains("gmail") || controller.login_email.getText().contains("hotmail") || controller.login_email.getText().contains("yahoo") || controller.login_email.getText().contains("outlook"))) {
                User user = controller.connection.authenticationUser(controller.login_email.getText(), controller.login_pass.getText());
                if (user != null) {
                    controller.renderDashboard(e, user);
                    controller.login_loader_flight.setVisible(true); // set loader to true
                    controller.playSoundLogin("Login", "sounds/login.wav");
                    try {
                        controller.profile_image_preview_imageview.setImage(controller.connection.getProfilePicture(user));
                    } catch (SQLException ei) {
                        ei.printStackTrace();
                    }
                } else {
                    controller.confirmActions.displayMessage(controller.error_message_lbl, "Wrong email or password!", true);
                }
            } else {
                controller.confirmActions.displayMessage(controller.error_message_lbl, "Email has wrong format!", true);
            }
        } else {
            controller.confirmActions.displayMessage(controller.error_message_lbl, "Email or password is empty, please fill in fields!", true);
        }
    }

    public void renderDashboard(ActionEvent e, User user, Controller controller) {
        controller.user = user;
        controller.root = controller.config.render(e,"user/Dashboard", "User Dashboard");
        controller.dashboardController.userInitializeFXML(controller.root, user);
        controller.initializeFXM.initializeProfile(controller.root, user);
        controller.initializeFXM.initializeWeather(controller.root);
        controller.weatherPaneBase.setClip(new Rectangle(186, 334));
        controller.create_world = new CreateWorld();
        controller.world_map = controller.create_world.init(controller, controller.connection);
        controller.create_world.addWorldInMap(controller.world_map_scrollpane, user);
        controller.world_map_scrollpane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        controller.world_map.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        controller.setInfoIntoTableHistorik();
    } // the method will render dashboard page for user

    public void noLoginRequired(ActionEvent e, Controller controller) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Dashboard.fxml")));
        this.userInitializeFXML(controller.root, controller.user);
        controller.flights_scrollpane = (ScrollPane) root.lookup("#flights_scrollpane");
        controller.flights_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controller.booking_seat_anchorpane = (AnchorPane) root.lookup("#booking_seat_anchorpane");

        controller.flight_display_vbox = (VBox) controller.flights_scrollpane.getContent();
        controller.world_map_scrollpane = (ScrollPane) root.lookup("#world_map_scrollpane");
        controller.world_map_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controller.world_map_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controller.create_world = new CreateWorld();
        controller.world_map = controller.create_world.init(controller, connection);

        controller.world_map_scrollpane.setContent(new StackPane(controller.world_map));
        controller.world_map_scrollpane.setBackground(new Background(new BackgroundFill(controller.world_map.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        controller.username_lbl = (Label) root.lookup("#username_lbl");
        controller.username_lbl.setText(null);
        controller.main_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        controller.main_scene = new Scene(root);
        MoveScreen.moveScreen(root, controller.main_stage);
        controller.main_stage.setTitle("Test dashboard window");
        controller.main_stage.setScene(controller.main_scene);
        controller.main_stage.show();
    }// shortcut login to user dashboard

}
