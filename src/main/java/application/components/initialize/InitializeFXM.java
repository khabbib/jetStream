package application.components.initialize;

import application.Controller;
import application.api.Db;
import application.components.user.User;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * #comment (comment this class and create javadoc to every method)
 */
public class InitializeFXM {
    private Controller controller;
    private Db db;

    public InitializeFXM(Controller controller, Db db){
        this.controller = controller;
        this.db = db;
    }

    public void initializeMusic(Parent root) {
        controller.play_button_image = (ImageView) root.lookup("#play_button_image");
    }
    public void initializeWeather(Parent root) {
        controller.lblForecastA = (Label) root.lookup("#lblForecastA");
        controller.lblForecastB = (Label) root.lookup("#lblForecastB");
        controller.lblForecastC = (Label) root.lookup("#lblForecastC");
        controller.lblForecastD = (Label) root.lookup("#lblForecastD");
        controller.lblForecastE = (Label) root.lookup("#lblForecastE");
        controller.lblForecastF = (Label) root.lookup("#lblForecastF");
        controller.weatherIcon = (ImageView) root.lookup("#weatherIcon");
        controller.weatherPane = (Pane) root.lookup("#weatherPane");
        controller.weatherPaneBase = (Pane) root.lookup("#weatherPaneBase");
        controller.weatherPaneBase.setPickOnBounds(false);
        controller.weatherPane.setVisible(false);
        controller.weatherPane.setPickOnBounds(false);
        controller.weatherMenu = false;
    }
    public void initializeProfile(Parent root, User user){
        //Profile info
        if (user != null) {
            controller.profile_image_imageview = (ImageView) root.lookup("#profile_image_imageview");
            controller.profile_image_preview_imageview = (ImageView) root.lookup("#profile_image_preview_imageview");
            controller.profile_first_name_lbl = (TextField) root.lookup("#profile_first_name_lbl");
            controller.profile_last_name_lbl = (TextField) root.lookup("#profile_last_name_lbl");
            controller.profile_email_lbl = (TextField) root.lookup("#profile_email_lbl");
            controller.profile_address_lbl = (TextField) root.lookup("#profile_address_lbl");
            controller.profile_phone_lbl = (TextField) root.lookup("#profile_phone_lbl");
            controller.profile_old_password_passwordfield = (PasswordField) root.lookup("#profile_old_password_passwordfield");
            controller.profile_new_password_textfield = (TextField) root.lookup("#profile_new_password_textfield");
            controller.profile_confirm_password_textfield = (TextField) root.lookup("#profile_confirm_password_textfield");
            controller.profile_image_preview_imageview = (ImageView) root.lookup("#profile_image_preview_imageview");
            controller.profile_profile_image_gridpane = (GridPane) root.lookup("#profile_profile_image_gridpane");
            controller.profile_edit_btn = (Button) root.lookup("#profile_edit_btn");

            controller.pfp_display_msg = (Label) root.lookup("#pfp_edit_error_msg");

            controller.profile_cancel_btn = (Button) root.lookup("#profile_cancel_btn");
            controller.profile_cancel_btn.setDisable(true);

            controller.edit_pfp_fname_issue = (Label) root.lookup("#edit_pfp_fname_issue");
            controller.edit_pfp_lname_issue = (Label) root.lookup("#edit_pfp_lname_issue");
            controller.edit_pfp_address_issue = (Label) root.lookup("#edit_pfp_address_issue");
            controller.edit_pfp_email_issue = (Label) root.lookup("#edit_pfp_email_issue");
            controller.edit_pfp_phone_issue = (Label) root.lookup("#edit_pfp_phone_issue");
            controller.edit_pfp_old_pwd_issue = (Label) root.lookup("#edit_pfp_old_pwd_issue");
            controller.edit_pfp_new_pwd_issue = (Label) root.lookup("#edit_pfp_new_pwd_issue");
            controller.edit_pfp_new_c_pwd_issue = (Label) root.lookup("#edit_pfp_new_c_pwd_issue");

            controller.profile_first_name_lbl.setText(user.getLastName()); // DO NOT EDIT, IT SHOULD BE LIKE THAT! /Sossio
            controller.profile_last_name_lbl.setText(user.getFirstName()); // DO NOT EDIT, IT SHOULD BE LIKE THAT! /Sossio
            controller.profile_email_lbl.setText(user.getEmail());
            controller.profile_address_lbl.setText(user.getAddress());
            controller.profile_phone_lbl.setText(user.getPhoneNumber());

            try {
                Image image = db.getProfilePicture(user);
                controller.profile_image_imageview.setImage(image);
                controller.profile_image_preview_imageview.setImage(image);
                controller.profile_image_preview_imageview.setImage(db.getProfilePicture(user));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            controller.profile_first_name_lbl.setDisable(true);
            controller.profile_last_name_lbl.setDisable(true);
            controller.profile_email_lbl.setDisable(true);
            controller.profile_address_lbl.setDisable(true);
            controller.profile_phone_lbl.setDisable(true);
            //controller.profilePassword.setDisable(true);
            controller.profile_old_password_passwordfield.setDisable(true);
            controller.profile_new_password_textfield.setDisable(true);
            controller.profile_confirm_password_textfield.setDisable(true);
            LocalDate date = LocalDate.now();
            controller.date_input_flight = (DatePicker) root.lookup("#date_input_flight");
            controller.dateR_input_flight = (DatePicker) root.lookup("#dateR_input_flight");
            controller.date_input_flight.setValue(date);
            controller.dateR_input_flight.setValue(date.plusWeeks(1));
        }
    }

    /**
     * This method initialize FXML components to user variables.
     * @param root ir root.
     * @param user is user.
     * @author Khabib. Developed by Kasper and Sossio.
     */
    public void userInitializeFXML(Parent root, User user){
        // flight list related variables
        controller.nbr_of_available_flights = (Label) root.lookup("#nbr_of_available_flights");

        // global error message for user dashboard
        controller.err_advanced_search = (Label) root.lookup("#err_advanced_search");
        // login success message

        // scroll pane seats
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
        controller.ceo_menu_user_image = (ImageView) root.lookup("#ceo_menu_user_image");
        controller.my_tickets_menu_user_image = (ImageView) root.lookup("#my_ticket_menu_user_image");

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
}
