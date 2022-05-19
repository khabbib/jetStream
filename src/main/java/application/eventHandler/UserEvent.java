package application.eventHandler;

import application.Controller;
import application.components.Email.TicketEmail;
import application.ErrorHandler;
import application.components.ticket.CardValidation;
import javafx.event.ActionEvent;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *This class is used to handle all events in user dashboard
 */
public class UserEvent {
    public String rfc;
    private ErrorHandler errorHandler;

    /**
     * Constructor for UserEvent
     * @param controller refer to control class to know each other
     */
    public UserEvent(Controller controller) {
        errorHandler = new ErrorHandler(controller);
    }

    /**
     * The method will handle the events
     * @param e event which passes here from GUI and control class
     * @param controller refer to control class as an instance
     * @author Habib developed by Alla
     */
    public void userDashboardEventHandler(ActionEvent e, Controller controller) throws ParseException {
        if (e.getSource() == controller.menu_profile_btn) {
            if(!controller.exploreMode) {
                controller.profile_anchorpane.toFront();
                toggleMenuColor(controller);
                controller.playSound("Next page", "sounds/next_page.wav");
            } else {
                controller.playSound("Error", "sounds/error.wav");
                errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
            }
        }
        else if(e.getSource() == controller.booking_close_btn || e.getSource() == controller.booking_close_second_page_btn){
            controller.booking_seat_anchorpane.toBack();
            restore_psgr_info(controller);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_flight_btn) {
            controller.flight_anchorpane.toFront();
            toggleMenuColor(controller);
            controller.menu_highlight_color_flight.setVisible(true);
            controller.map_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_history_btn) {
            if(!controller.exploreMode) {
                controller.history_anchorpane.toFront();
                toggleMenuColor(controller);
                controller.menu_highlight_color_history.setVisible(true);
                controller.history_menu_user_image.setOpacity(1);
                controller.playSound("Next page", "sounds/next_page.wav");
            } else {
                controller.playSound("Error", "sounds/error.wav");
                errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
            }
        }
        else if(e.getSource() == controller.menu_entertainment_btn) {
            controller.entertainment_anchorpane.toFront();
            toggleMenuColor(controller);
            controller.menu_highlight_color_entertainment.setVisible(true);
            controller.entertainment_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_support_btn){

            if (!controller.exploreMode) {
                controller.support_anchorpane.toFront();
                toggleMenuColor(controller);
                controller.menu_highlight_color_support.setVisible(true);
                controller.support_menu_user_image.setOpacity(1);
                controller.playSound("Next page", "sounds/next_page.wav");
            } else {
                controller.playSound("Error", "sounds/error.wav");
                errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
            }
        }
        else if(e.getSource() == controller.menu_ceo_btn){

            if (!controller.exploreMode) {
                controller.ceo_anchorpane.toFront();
                toggleMenuColor(controller);
                controller.menu_highlight_color_ceo.setVisible(true);
                controller.ceo_menu_user_image.setOpacity(1);
                controller.playSound("Next page", "sounds/next_page.wav");
            } else {
                controller.playSound("Error", "sounds/error.wav");
                errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
            }
        }
        else if(e.getSource() == controller.menu_my_tickets_btn){

            if (!controller.exploreMode) {
                controller.my_ticket_anchorpane.toFront();
                toggleMenuColor(controller);
                controller.menu_highlight_color_my_ticket.setVisible(true);
                controller.my_tickets_menu_user_image.setOpacity(1);
                controller.playSound("Next page", "sounds/next_page.wav");
            } else {
                controller.playSound("Error", "sounds/error.wav");
                errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
            }

        }
        // navigating av
        else if(e.getSource() == controller.date_previous_day_button){
            controller.date_input_flight.setValue(controller.date_input_flight.getValue().minusDays(1));
        }
        else if(e.getSource() == controller.date_next_day_button){
            controller.date_input_flight.setValue(controller.date_input_flight.getValue().plusDays(1));
        }
        else if(e.getSource() == controller.date_previous_day_return_button){
            controller.dateR_input_flight.setValue(controller.dateR_input_flight.getValue().minusDays(1));
        }
        else if(e.getSource() == controller.date_next_day_return_button){
            controller.dateR_input_flight.setValue(controller.dateR_input_flight.getValue().plusDays(1));
        }
        // Cancel and check in buttons
        else if(e.getSource() == controller.cancel_btn_my_ticket){
            System.out.println("Cancel clicked");
            if (controller.rfc_muticket.getText() != null){
                String rfc = controller.rfc_muticket.getText();
                boolean confirmed = controller.errorHandler.confirmThisAction("Cancel ticket" + rfc, "Confirm your cancellation", rfc);
                if (confirmed){
                    boolean canceled = controller.db.deleteHistoryByRFC(rfc);
                    if (canceled){
                        System.out.println("Deleted");
                        controller.errorHandler.displayMessage(controller.err_msg_myticket, "Cancellation done successfully!", false);
                        controller.updateDashboardInfo();
                    }else {
                        controller.errorHandler.displayMessage(controller.err_msg_myticket, "Cancellation didn't went through!", true);
                    }
                }
            }else {
                controller.errorHandler.displayMessage(controller.err_msg_myticket, "RFC didn't match!", true);
            }
        }
        else if(e.getSource() == controller.checka_btn_myticket){
            if (controller.rfc_muticket != null){
                String rfc = controller.rfc_muticket.getText();
                java.sql.Date depDate = java.sql.Date.valueOf(controller.dep_date_myticket.getText());
                if (depDate.toLocalDate().getMonth() == LocalDate.now().getMonth().plus(1)){
                    System.out.println(LocalDate.now().plusMonths(1) + " month");
                    System.out.println("Check point 24 hours for checking");
                    controller.errorHandler.displayMessage(controller.err_msg_myticket, "Date: " + depDate.toString() +" date now: " + LocalDate.now().toString(), false);
                    boolean checking = controller.db.checking(rfc);
                    if (checking){
                        controller.checkning_pnl.toFront();
                        controller.updateDashboardInfo();
                    }
                }else {
                    controller.errorHandler.displayMessage(controller.err_msg_myticket, "Date: " + depDate.toString() +" date now: " + LocalDate.now().toString(), true);
                }
            }
        }
        else if(e.getSource() == controller.returnD_btn_checking){
            controller.my_ticket_anchorpane.toBack();
            controller.flight_anchorpane.toFront();
            controller.checkning_pnl.toBack();

        }
        // navigate to my ticket
        else if(e.getSource() == controller.detailes_btn_histroy){
            //controller.updateDashboardInfo();
            System.out.println("Rfc = " + controller.rfc);
            controller.pickTicketForDetailes();
        }
        // ticket purchase events
        else if(e.getSource() == controller.card_prev_btn){
            controller.booking_passenger_anchorpane.toFront();
            controller.payment_anchorpane.toBack();
            controller.playSound("Next page", "sounds/next_page.wav");

        }
        else if(e.getSource() == controller.card_purchase_btn){
            controller.playSound("Next page", "sounds/next_page.wav");

            String nbr = controller.card_nbr.getText();
            String name = controller.card_fname.getText();
            String lname = controller.card_lname.getText();
            String month = controller.card_month.getText();
            String year = controller.card_year.getText();
            String cvc = controller.card_cvc.getText();

            if (!nbr.isEmpty()) {
                if (!name.isEmpty()) {
                    if (!lname.isEmpty()) {
                        if (!year.isEmpty()) {
                            if (!month.isEmpty()) {
                                if (!cvc.isEmpty()) {

                                    boolean validCard = CardValidation.purchaseTicket(nbr, name, lname, month, year, cvc);
                                    if (validCard) {
                                        System.out.println("Card is valid!");
                                        boolean purchaseDone1 = false;
                                        boolean purchaseDone2 = false;
                                        String rfc1 = "", rfc2 = "";
                                        controller.playSound("Success", "sounds/success.wav");

                                        if (controller.departure_seat != null) {
                                            for (int i = 0; i <= 1; i++) {
                                                System.out.println("Loop is running...");

                                                if (controller.departure_seat != null && controller.return_seat != null) { // saving tur flight
                                                    System.out.println("First condition");
                                                    StringBuilder rfc = controller.db.generateRandomRFC();
                                                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                                    boolean saveTicket = controller.db.savePurchasedTicket(controller.user.getUserId(), controller.return_seat, String.valueOf(rfc), date, controller.departure_seat, false);
                                                    if (saveTicket) {
                                                        rfc1 = String.valueOf(rfc);
                                                        purchaseDone1 = true;
                                                    }
                                                    controller.departure_seat = null;
                                                    controller.return_seat = null;
                                                } else {
                                                    System.out.println("Second condition 2");
                                                    StringBuilder rfc = controller.db.generateRandomRFC();
                                                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                                    boolean saveTicket = controller.db.savePurchasedTicket(controller.user.getUserId(), controller.booking_flight_number_lbl.getText(), String.valueOf(rfc), date, controller.booking_seat_number_lbl.getText(), false);
                                                    if (saveTicket) {
                                                        purchaseDone1 = true;
                                                        rfc1 = String.valueOf(rfc);
                                                        //confirmPurchase(String.valueOf(rfc));
                                                    } else {
                                                        System.out.println("Did not saved the purchase in database");
                                                    }
                                                }
                                            }
                                        } else {
                                            System.out.println("Tur seat is null.");

                                            System.out.println("Second condition 2");
                                            StringBuilder rfc = controller.db.generateRandomRFC();
                                            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                            boolean saveTicket = controller.db.savePurchasedTicket(controller.user.getUserId(), controller.booking_flight_number_lbl.getText(), String.valueOf(rfc), date, controller.booking_seat_number_lbl.getText(), false);
                                            if (saveTicket){
                                                purchaseDone1 = true;
                                                rfc1 = String.valueOf(rfc);
                                                //confirmPurchase(String.valueOf(rfc));
                                            }else {
                                                System.out.println("Did not saved the purchase in database");
                                            }
                                        }

                                        if (purchaseDone1 && purchaseDone2) {
                                            System.out.println("Booked two-ways flights");
                                            controller.confirmPurchase(rfc2); // can send more information form here.
                                        } else if (purchaseDone1) {
                                            System.out.println("Booked only one-way flight");
                                            controller.confirmPurchase(rfc1);
                                        }

                                    }else{
                                        System.out.println("Card not valid");
                                        controller.errorHandler.displayMessage(controller.payment_err_msg, "Card is not valid.", true);
                                    }
                                } else {
                                    controller.errorHandler.displayMessage(controller.payment_err_msg, "CVC is empty!", true);
                                }
                            } else {
                                controller.errorHandler.displayMessage(controller.payment_err_msg, "Month is empty!", true);
                            }
                        } else {
                            controller.errorHandler.displayMessage(controller.payment_err_msg, "Year is empty!", true);
                        }
                    } else {
                        controller.errorHandler.displayMessage(controller.payment_err_msg, "Last name is empty!", true);
                    }
                } else {
                    controller.errorHandler.displayMessage(controller.payment_err_msg, "First name is empty!", true);
                }
            }else {
                controller.errorHandler.displayMessage(controller.payment_err_msg, "Card numbers is empty!", true);
            }

        }
        else if(e.getSource() == controller.seat_next_btn){
            //<editor-fold desc="file">
            String name_s = controller.booking_first_name_textfield.getText();
            String lname_s = controller.booking_last_name_textfield.getText();
            String fourdigit = controller.booking_four_digits_textfield.getText();
            String email = controller.booking_email_textfield.getText();
            String isTur = controller.booking_is_retur_lbl.getText();
            String seat = controller.booking_seat_number_lbl.getText();
            //</editor-fold>
            if (name_s.length() >= 3 && name_s.length() <= 30){
                if (lname_s.length() >= 3 && lname_s.length() <= 30) {
                    if (fourdigit.length() == 12) {
                        if (email.length() >= 6 && email.length() <= 60 && email.contains("@") && email.contains("gmail")) {
                            if (!seat.isEmpty()) {
                                controller.playSound("Next page", "sounds/next_page.wav");
                                if (controller.round_trip_flights.size() == 1){
                                    System.out.println("Active tur");
                                    controller.departure_seat = controller.booking_seat_number_lbl.getText();
                                    controller.return_seat = controller.booking_flight_number_lbl.getText();

                                    controller.booking_flight_number_lbl.setText(null);
                                    controller.booking_seat_number_lbl.setText(null);
                                    controller.booking_price_lbl.setText(null);

                                    // clear the operation - preperBeforeCreatingSeats
                                    boolean build = controller.preperBeforeCreatingSeats();
                                    if (build){
                                        controller.booking_departure_lbl.setText(controller.round_trip_flights.get(0).getDeparture_name());
                                        controller.booking_destination_lbl.setText(controller.round_trip_flights.get(0).getDestination_name());
                                        controller.booking_departure_extra_lbl.setText(controller.round_trip_flights.get(0).getDeparture_date());
                                        controller.booking_destination_extra_lbl.setText(controller.round_trip_flights.get(0).getDestination_date());
                                        controller.booking_flight_number_lbl.setText(controller.round_trip_flights.get(0).getId());
                                        controller.seat_price = Double.parseDouble(controller.round_trip_flights.get(0).getPrice());
                                        controller.booking_price_lbl.setText(String.valueOf(controller.seat_price));
                                        controller.createThisSeat(controller.round_trip_flights, 0);
                                        controller.round_trip_flights.clear();
                                    }
                                } else {
                                    System.out.println("To front");
                                    controller.payment_anchorpane.toFront();
                                }

                            } else {
                                controller.errorHandler.displayMessage(controller.booking_msg_lbl, "Please choose a seat!", true);
                            }
                        } else {
                            controller.errorHandler.displayMessage(controller.booking_msg_lbl, "Email char 6-30 or format issue!", true);
                        }
                    } else {
                        controller.errorHandler.displayMessage(controller.booking_msg_lbl, "SSN shall be 12 chars!", true);
                    }
                } else {
                    controller.errorHandler.displayMessage(controller.booking_msg_lbl, "Lastname shall be 3-30 chars!", true);
                }
            }else {
                controller.errorHandler.displayMessage(controller.booking_msg_lbl, "Firstname shall be 3-30 chars!", true);
            }
        }
        else if(e.getSource() == controller.success_purchase_to_dashboard_button){
            controller.updateDashboardInfo();
            controller.flight_anchorpane.toFront();
            arangeTheNavigation(controller);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.show_ticket_purchase_btn){
            controller.success_purchase_anchorpane.toBack();
            arangeTheNavigation(controller);
        }
        else if(e.getSource() == controller.logout_ticket_purchase_btn){
            arangeTheNavigation(controller);
            controller.switchToLogin(e);
        }
    }

    /**
     * This method shows which menu is active on user dashboard.
     * @author Habib.
     */
    public void toggleMenuColor(Controller controller) {
        controller.menu_highlight_color_flight.setVisible(false);
        controller.menu_highlight_color_history.setVisible(false);
        controller.menu_highlight_color_entertainment.setVisible(false);
        controller.menu_highlight_color_support.setVisible(false);
        controller.menu_highlight_color_ceo.setVisible(false);
        controller.menu_highlight_color_my_ticket.setVisible(false);

        controller.map_menu_user_image.setOpacity(0.5);
        controller.history_menu_user_image.setOpacity(0.5);
        controller.entertainment_menu_user_image.setOpacity(0.5);
        controller.support_menu_user_image.setOpacity(0.5);
        controller.ceo_menu_user_image.setOpacity(0.5);
        controller.my_tickets_menu_user_image.setOpacity(0.5);
    }

    /**
     * This method will reset the navigation setting
     * @param controller refer to the control class
     * @author Habib Mohammadi
     */
    private void arangeTheNavigation(Controller controller) {
        controller.updateDashboardInfo();
        restore_psgr_info(controller);
        controller.my_ticket_anchorpane.toFront();
        controller.payment_anchorpane.toBack();
    }

    /**
     * The method will reset some values that have been set during the purchase process
     * @param controller refer to control class
     * @autho Habib
     */
    public void restore_psgr_info(Controller controller){
        controller.booking_first_name_textfield.clear();
        controller.booking_last_name_textfield.clear();
        controller.booking_four_digits_textfield.clear();
        controller.booking_email_textfield.clear();
        controller.booking_seat_number_lbl.setText(null);
        controller.booking_flight_number_lbl.setText(null);
        controller.booking_price_lbl.setText(null);
        controller.card_nbr.clear();
        controller.card_fname.clear();
        controller.card_lname.clear();
        controller.card_month.clear();
        controller.card_year.clear();
        controller.card_cvc.clear();
        controller.taken_seat_economy.clear();
        controller.taken_seat_business.clear();
        controller.economy_seat_gridpane.getChildren().clear();
        controller.business_seat_gridpane.getChildren().clear();
        controller.round_trip_flights.clear();
        controller.departure_seat = null;
        controller.return_seat = null;
    }

}
