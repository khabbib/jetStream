package application.components.ticket;

import application.Controller;
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * #comment (comment this class and create javadoc to every method)
 */
public class PurchaseHandler {

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

    /**
     * @param rfc
     * @param controller
     * @author Khabib.
     */
    public void confirmPurchase(String rfc, Controller controller) {
        System.out.println(controller.booking_email_textfield.getText() + " Your email!!!");
        if (!controller.booking_email_textfield.getText().isEmpty()){
            boolean sentMail = Purchase.sendEmail(controller.booking_email_textfield.getText(), controller.booking_first_name_textfield.getText(), controller.booking_flight_number_lbl.getText(), controller.booking_seat_number_lbl.getText(), controller.booking_price_lbl.getText());
            if (sentMail){
                if (controller.departure_seat != null){
                    System.out.println("Tur is reached here");
                    controller.history_reference_number_lbl.setText(rfc.toString());
                    controller.success_purchase_anchorpane.toFront();
                    controller.departure_seat = null;
                    System.out.println("Email successfully sent!");
                }else {
                    System.out.println("Returne has been reached here");
                    controller.history_reference_number_lbl.setText(rfc.toString());
                    controller.success_purchase_anchorpane.toFront();
                }
            }else {
                System.out.println("The email addrss is not correct");
                //JOptionPane.showMessageDialog(null, "The email address is not correct!");
            }
        }
        System.out.println("saved information in database");
    }

    /**
     * #comment
     * @param e
     * @param controller
     * @autor Khabib. Developed by Sossio.
     */
    public void purchaseHandle(ActionEvent e, Controller controller){
        if (e.getSource() == controller.card_prev_btn){
            controller.booking_passenger_anchorpane.toFront();
            controller.payment_anchorpane.toBack();
            controller.playSound("Next page", "sounds/next_page.wav");

        }else if(e.getSource() == controller.card_purchase_btn){
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

                                    boolean validCard = Purchase.purchaseTicket(nbr, name, lname, month, year, cvc);
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

        }else if(e.getSource() == controller.seat_next_btn){
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
        }else if(e.getSource() == controller.success_purchase_to_dashboard_button){
            controller.updateDashboardInfo();
            controller.flight_anchorpane.toFront();
            controller.restore_psgr_info();
            controller.success_purchase_anchorpane.toBack();
            controller.payment_anchorpane.toBack();
            controller.playSound("Next page", "sounds/next_page.wav");
        }
    }
}
