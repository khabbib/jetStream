package application.Components;

import application.Controller;
import application.database.Connection;
import application.model.User;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.w3c.dom.Text;

import java.sql.SQLException;

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
        controller.msgBox_user_dashboard = (Pane)root.lookup("#msgBox_user_dashboard");
        controller.notify_user_dashboard = (Label) root.lookup("#notify_user_dashboard");
        // login success message
        //
        // scrollpane seats
        controller.eco_scrollpane = (ScrollPane) root.lookup("#eco_scrollpane");
        controller.business_scrollpane = (ScrollPane) root.lookup("#business_scrollpane");
        controller.eco_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controller.business_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        controller.flight_seats_eco = (AnchorPane) controller.eco_scrollpane.getContent();
        controller.flights_seats_business = (AnchorPane) controller.business_scrollpane.getContent();

        // user menu images
        controller.map_menu_user = (ImageView)root.lookup("#map_menu_user");
        controller.game_menu_user = (ImageView)root.lookup("#game_menu_user");
        controller.historik_menu_user = (ImageView)root.lookup("#historik_menu_user");
        controller.support_menu_user = (ImageView)root.lookup("#support_menu_user");

        // Success page
        controller.rfc_no_sucesspnl = (Label) root.lookup("#rfc_no_sucesspnl");

        // Support page
        controller.sup_report_error_msg = (Label) root.lookup("#sup_report_error_msg");
        controller.sup_contact_error_msg = (Label) root.lookup("#sup_contact_error_msg");
        controller.sup_feedback_error_msg = (Label) root.lookup("#sup_feedback_error_msg");

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

        // Config scrollbar

        // Passenger info
        controller.first_name_seat_pnl = (TextField) root.lookup("#name_seat_pnl");
        controller.last_name_seat_pnl = (TextField) root.lookup("#lname_seat_pnl");
        controller.four_digit_seat_pnl = (TextField) root.lookup("#fourdigit_seat_pnl");
        controller.email_seat_pnl = (TextField) root.lookup("#email_seat_pnl");
        controller.seat_nbr_seat_pnl = (Label) root.lookup("#seat_nbr_seat_pnl");
        controller.rtur_seat_nbr_seat_pnl = (Label) root.lookup("#rtur_seat_nbr_seat_pnl");
        controller.msg_seat_pnl = (Label) root.lookup("#msg_seat_pnl");
        controller.flight_nbr_seat_pnl = (Label) root.lookup("#flight_nbr_seat_pnl");
        controller.price_seat_pnl = (Label) root.lookup("#price_seat_pnl");
        controller.from_info_seat_pnl = (Label) root.lookup("#from_info_seat_pnl");
        controller.from_d_info_seat_pnl = (Label) root.lookup("#from_d_info_seat_pnl");
        controller.to_info_seat_pnl = (Label) root.lookup("#to_info_seat_pnl");
        controller.to_d_info_seat_pnl = (Label) root.lookup("#to_d_info_seat_pnl");
        controller.isTur_seat_pnl = (Label) root.lookup("#isTur_seat_pnl");

        // look up for global variables
        controller.u_name = (Label) root.lookup("#u_name");
        controller.first_name_seat_pnl = (TextField) root.lookup("#first_name_seat_pnl");
        controller.last_name_seat_pnl = (TextField) root.lookup("#last_name_seat_pnl");
        controller.email_seat_pnl = (TextField) root.lookup("#email_seat_pnl");
        //controller.u_id = (Label) root.lookup("#u_id");
        controller.rtur_date_pick = (HBox)root.lookup("rtur_date_pick");
        controller.searchListAppear = (ListView<String>) root.lookup("#searchListAprear");
        controller.searchListAppear2 = (ListView<String>) root.lookup("#searchListAprear2");
        controller.searchListAppear3 = (ListView<String>) root.lookup("#searchListAprear3");

        controller.pnlSeat = (AnchorPane) root.lookup("#pnlSeat");
        controller.pnlPassager = (AnchorPane) root.lookup("#pnlPassanger");
        controller.scrollFlights = (ScrollPane) root.lookup("#scrollFlights");
        controller.scrollFlights.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        controller.display_flight = (VBox) controller.scrollFlights.getContent();
        controller.scrollPane = (ScrollPane) root.lookup("#scrollPane");
        controller.search_f_name = (TextField) root.lookup("#search_f_name");
        controller.pgr_prf_seat_pnl = (ImageView) root.lookup("#pgr_prf_seat_pnl");

        // loader in login
        //controller.login_loader_flight = (ImageView) root.lookup("#login_loader_flight");
        //controller.login_loader_flight.setVisible(false); // set loader to false

    }

}
