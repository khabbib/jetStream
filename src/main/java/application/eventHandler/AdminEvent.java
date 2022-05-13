package application.eventHandler;

import application.Controller;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 */
public class AdminEvent {
    /**
     * Administrator dev.
     * @param e
     * @throws IOException
     * @autor Obed.
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

        else if(e.getSource() == controller.admin_flights_button) {
            controller.admin_flights_anchorpane.toFront();
        }

        else if(e.getSource() == controller.admin_tickets_button) {
            controller.admin_tickets_anchorpane.toFront();
        }

        else if(e.getSource() == controller.admin_members_button) {
            controller.admin_members_anchorpane.toFront();
        }
    }
}
