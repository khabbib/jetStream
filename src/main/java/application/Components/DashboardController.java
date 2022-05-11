package application.Components;

import application.Controller;
import application.database.Connection;
import application.model.User;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class DashboardController {
    private Controller controller;
    private Parent root;
    private Connection connection;
    public DashboardController(Controller controller, Parent root, Connection connection){
        this.controller = controller;
        this.root = root;
        this.connection = connection;
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
        controller.booking_first_name_textfield = (TextField) root.lookup("#name_seat_pnl");
        controller.booking_last_name_textfield = (TextField) root.lookup("#lname_seat_pnl");
        controller.booking_four_digits_textfield = (TextField) root.lookup("#fourdigit_seat_pnl");
        controller.booking_email_textfield = (TextField) root.lookup("#email_seat_pnl");
        controller.booking_seat_number_lbl = (Label) root.lookup("#seat_nbr_seat_pnl");
        controller.booking_retur_seat_number_lbl = (Label) root.lookup("#rtur_seat_nbr_seat_pnl");
        controller.booking_msg_lbl = (Label) root.lookup("#msg_seat_pnl");
        controller.booking_flight_number_lbl = (Label) root.lookup("#flight_nbr_seat_pnl");
        controller.booking_price_lbl = (Label) root.lookup("#price_seat_pnl");
        controller.booking_departure_lbl = (Label) root.lookup("#from_info_seat_pnl");
        controller.booking_departure_extra_lbl = (Label) root.lookup("#from_d_info_seat_pnl");
        controller.booking_destination_lbl = (Label) root.lookup("#to_info_seat_pnl");
        controller.booking_destination_extra_lbl = (Label) root.lookup("#to_d_info_seat_pnl");
        controller.booking_is_retur_lbl = (Label) root.lookup("#isTur_seat_pnl");

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

}
