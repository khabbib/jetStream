package application.components.ticket;

import application.Controller;
import application.components.Email.TicketEmail;

/**
 * This class control the ticket's purchasing process.
 */
public class PurchaseHandler {
    /**
     * The method will control everything so that it is ok to finish the purchase
     * @param rfc reference number to control
     * @param controller instance of control class
     * @author Habib
     */
    public void confirmPurchase(String rfc, Controller controller) {
        System.out.println(controller.booking_email_textfield.getText() + " Your email!!!");
        if (!controller.booking_email_textfield.getText().isEmpty()){
            boolean sentMail = TicketEmail.send(controller.booking_email_textfield.getText(), controller.booking_first_name_textfield.getText(), controller.booking_flight_number_lbl.getText(), controller.booking_seat_number_lbl.getText(), controller.booking_price_lbl.getText());
            if (sentMail){
                if (controller.departure_seat != null){
                    System.out.println("Tur is reached here");
                    controller.history_reference_number_lbl.setText(rfc);
                    controller.success_purchase_anchorpane.toFront();
                    controller.departure_seat = null;
                    System.out.println("Email successfully sent!");
                }else {
                    System.out.println("Returne has been reached here");
                    controller.history_reference_number_lbl.setText(rfc);
                    controller.success_purchase_anchorpane.toFront();
                }
            }else {
                System.out.println("The email addrss is not correct");
            }
        }
        System.out.println("saved information in database");
    }
}
