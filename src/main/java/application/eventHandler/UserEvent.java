package application.eventHandler;

import application.Controller;
import javafx.event.ActionEvent;
import java.time.LocalDate;

/**
 *
 */
public class UserEvent {

    /**
     * @param e
     * @param controller
     */
    public void userDashboardEventHandler(ActionEvent e, Controller controller){
        if (e.getSource() == controller.menu_profile_btn) {
            controller.profile_anchorpane.toFront();
            controller.toggleMenuColor();
        }
        else if(e.getSource() == controller.booking_close_btn || e.getSource() == controller.booking_close_second_page_btn){
            controller.booking_seat_anchorpane.toBack();
            controller.restore_psgr_info();
        }
        else if (e.getSource() == controller.menu_flight_btn) {
            controller.flight_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_flight.setVisible(true);
            controller.map_menu_user_image.setOpacity(1);
        }
        else if (e.getSource() == controller.menu_history_btn) {
            controller.history_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_history.setVisible(true);
            controller.history_menu_user_image.setOpacity(1);
        }
        else if (e.getSource() == controller.menu_entertainment_btn) {
            controller.entertainment_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_entertainment.setVisible(true);
            controller.entertainment_menu_user_image.setOpacity(1);

        }
        else if(e.getSource() == controller.menu_support_btn){
            controller.support_anchorpane.toFront();
            controller.toggleMenuColor();
            controller.menu_highlight_color_support.setVisible(true);
            controller.support_menu_user_image.setOpacity(1);
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
