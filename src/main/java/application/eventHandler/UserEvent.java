package application.eventHandler;

import application.Controller;
import application.ErrorHandler;
import javafx.event.ActionEvent;

/**
 * This class handle user events when clicking on a button.
 */
public class UserEvent {

    private ErrorHandler errorHandler;

    public UserEvent(Controller controller) {
        errorHandler = new ErrorHandler(controller);
    }

    /**
     * This method handles actions when a button is clicked.
     * @param e actionevent.
     * @param controller to connect methods and variables.
     * @author Khabib. Developed by Sossio.
     */
    public void userDashboardEventHandler(ActionEvent e, Controller controller){
        controller.systemSound.pauseButton();
        if (e.getSource() == controller.menu_profile_btn) {
            if(!controller.exploreMode) {
                controller.profile_anchorpane.toFront();
                controller.toggleMenuColor();
                controller.playSound("Next page", "sounds/next_page.wav");
            } else {
                controller.playSound("Error", "sounds/error.wav");
                errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
            }
        }
        else if(e.getSource() == controller.booking_close_btn || e.getSource() == controller.booking_close_second_page_btn){
            controller.systemSound.pauseButton();
            controller.booking_seat_anchorpane.toBack();
            controller.restore_psgr_info();
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if (e.getSource() == controller.menu_flight_btn) {
            controller.systemSound.pauseButton();
            controller.flight_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_flight.setVisible(true);
            controller.map_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
        }
        else if (e.getSource() == controller.menu_history_btn) {
            controller.systemSound.pauseButton();
            if(!controller.exploreMode) {
                controller.history_anchorpane.toFront();
                controller.toggleMenuColor();
                controller.menu_highlight_color_history.setVisible(true);
                controller.history_menu_user_image.setOpacity(1);
                controller.playSound("Next page", "sounds/next_page.wav");
            } else {
                controller.playSound("Error", "sounds/error.wav");
                errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
            }
        }
        else if (e.getSource() == controller.menu_entertainment_btn) {
            controller.systemSound.pauseButton();
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
            controller.systemSound.pauseButton();
        }
        else if(e.getSource() == controller.menu_ceo_btn){
            controller.ceo_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_ceo.setVisible(true);
            controller.ceo_menu_user_image.setOpacity(1);
            controller.playSound("Next page", "sounds/next_page.wav");
            controller.systemSound.playButton();
        }
        else if(e.getSource() == controller.menu_my_tickets_btn){
            controller.systemSound.pauseButton();

            if (!controller.exploreMode) {
                controller.my_ticket_anchorpane.toFront();
                controller.toggleMenuColor();
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
    }
}
