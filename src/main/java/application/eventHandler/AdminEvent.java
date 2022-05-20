package application.eventHandler;

import application.Controller;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;


/**
 * This class is has alla control over every event button clicked.
 */
public class AdminEvent {
    /**
     * This metod is responsible for alla the button clicks in admin-dashboard plus every other anchorpane
     * @param e
     * @throws IOException
     * @autor Obed. Developed by Sossio.
     */
    public void adminDashboardEventHandler(ActionEvent e, Controller controller) throws SQLException {
        if(e.getSource() == controller.admin_logout_button) {
            controller.switchToLogin(e);
        }

        else if(e.getSource() == controller.refreshMembersBtn_admin){
            controller.adminControl.updateMemberTable();
        }

        else if(e.getSource() == controller.deletS_btn_mbr_admin){
            controller.adminControl.updateMemberTable();
        }

        else if(e.getSource() == controller.returnToMemberListBtn_admin) {
            controller.admin_members_anchorpane.toFront();
        }

        else if(e.getSource() == controller.registerMemberBtn_admin) {
            controller.admin_register_anchorpane.toFront();
        }

        else if(e.getSource() == controller.admin_members_button) {
            controller.admin_members_anchorpane.toFront();
            toggleAdminMenuColor(controller);
            controller.menu_admin_highlight_customers.setVisible(true);
            controller.customers_menu_admin_img.setOpacity(1);
            controller.customers_menu_admin_lbl.setOpacity(1);
            controller.playSystemSound("Next page", "sounds/next_page.wav");
        }

        else if(e.getSource() == controller.admin_flights_button) {
            controller.admin_flights_anchorpane.toFront();
            toggleAdminMenuColor(controller);
            controller.menu_admin_highlight_flights.setVisible(true);
            controller.flights_menu_admin_img.setOpacity(1);
            controller.flights_menu_admin_lbl.setOpacity(1);
            controller.playSystemSound("Next page", "sounds/next_page.wav");
        }

        else if(e.getSource() == controller.admin_tickets_button) {
            controller.admin_tickets_anchorpane.toFront();
            toggleAdminMenuColor(controller);
            controller.menu_admin_highlight_tickets.setVisible(true);
            controller.tickets_menu_admin_img.setOpacity(1);
            controller.tickets_menu_admin_lbl.setOpacity(1);
            controller.playSystemSound("Next page", "sounds/next_page.wav");
        }

    }

    public void toggleAdminMenuColor(Controller controller) {
        controller.menu_admin_highlight_customers.setVisible(false);
        controller.menu_admin_highlight_flights.setVisible(false);
        controller.menu_admin_highlight_tickets.setVisible(false);

        controller.customers_menu_admin_img.setOpacity(0.5);
        controller.flights_menu_admin_img.setOpacity(0.5);
        controller.tickets_menu_admin_img.setOpacity(0.5);

        controller.customers_menu_admin_lbl.setOpacity(0.5);
        controller.flights_menu_admin_lbl.setOpacity(0.5);
        controller.tickets_menu_admin_lbl.setOpacity(0.5);
    }
}
