package application.Components;

import application.Controller;
import application.database.Connection;
import application.model.ConfirmActions;
import javafx.event.ActionEvent;

import java.time.LocalDate;

public class Search {
    private Controller controller;
    private Connection connection;
    private ConfirmActions confirmActions;
    public Search(Controller controller, Connection connection, ConfirmActions confirmActions){
        this.controller = controller;
        this.connection = connection;
        this.confirmActions = confirmActions;
    }

    public void checkboxEvent(ActionEvent e){
        if (controller.turR_checkBox_flight.isSelected()){
            controller.rtur_date_pick.setDisable(false);
            controller.next_rtur_date_flight.setDisable(false);
            controller.prev_rtur_date_flight.setDisable(false);
        }else {
            controller.rtur_date_pick.setDisable(true);
            controller.next_rtur_date_flight.setDisable(true);
            controller.prev_rtur_date_flight.setDisable(true);
        }

    }

    public void searchFlight() {
        System.out.println(controller.turR_checkBox_flight.isSelected() + " checkbox");

        String from = controller.from_input_flight.getText();
        String to = controller.disc_input_flight.getText();
        LocalDate d = controller.date_input_flight.getValue();
        LocalDate Rd = controller.dateR_input_flight.getValue();

        if (!controller.turR_checkBox_flight.isSelected()){
            if (!(from.isEmpty()) && !(to.isEmpty())){
                if (d == null) {
                    boolean ok = confirmActions.confirmThisAction("Confirm action", "Search without date?", "To continue search without specifying the date confirm.");
                    if (ok) {
                        controller.avalibleFlights = connection.searchFlight(from, to, null, null);
                    }
                } else {
                    controller.avalibleFlights = connection.searchFlight(from, to, String.valueOf(d), null);
                }
            }
        }else {
            if (!from.isEmpty() && !to.isEmpty()){
                if (!(d == null) && (!(Rd ==null))){
                    controller.avalibleFlights = connection.searchFlight(from, to, String.valueOf(d), String.valueOf(Rd));
                }else
                    controller.avalibleFlights = connection.searchFlight(from, to, null, null);
            }
        }

        // fill out the flights on  screen
        if (controller.avalibleFlights.isEmpty()){
            controller.fillFlights(null);
        }else {
            System.out.println(controller.avalibleFlights.size() );
            controller.fillFlights(controller.avalibleFlights);
        }
    }



    public void searchHit() {
        if (!controller.search_f_name.getText().isEmpty()){
            controller.avalibleFlights.clear();
            System.out.println("search from controll " + controller.search_f_name.getText());
            controller.avalibleFlights = connection.seachFlightFromSearchField(controller.search_f_name.getText());
            if (!controller.avalibleFlights.isEmpty()){
                controller.fillFlights(controller.avalibleFlights);
            }
        }
    }
}
