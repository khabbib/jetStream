package application.eventHandler;

import application.Controller;
import application.components.ticket.UserHistory;
import javafx.event.ActionEvent;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 */
public class UserEvent {
    public String rfc = null;
    /**
     * @param e
     * @param controller
     */
    public void userDashboardEventHandler(ActionEvent e, Controller controller) throws ParseException {
        System.out.println("Hej");
        if (e.getSource() == controller.menu_profile_btn) {
            controller.profile_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.booking_close_btn || e.getSource() == controller.booking_close_second_page_btn){
            controller.booking_seat_anchorpane.toBack();
            controller.restore_psgr_info();
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_flight_btn) {
            controller.flight_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_flight.setVisible(true);
            controller.map_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_history_btn) {
            controller.history_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_history.setVisible(true);
            controller.history_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_entertainment_btn) {
            controller.entertainment_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_entertainment.setVisible(true);
            controller.entertainment_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_support_btn){
            controller.support_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_support.setVisible(true);
            controller.support_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_ceo_btn){
            controller.ceo_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_ceo.setVisible(true);
            controller.ceo_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if(e.getSource() == controller.menu_my_tickets_btn){
            controller.my_ticket_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_my_ticket.setVisible(true);
            controller.my_tickets_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
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
            System.out.println("Rfc = " + rfc);
            ArrayList<UserHistory> list = controller.db.searchDataForTableHistory( -1, rfc, false);
            if (list.size() >0){
                controller.userControl.fillMyTicket(list);
                controller.my_ticket_anchorpane.toFront();
            }
        }
    }


    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
}
