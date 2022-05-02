package application.Components;

import application.Controller;
import application.database.Connection;
import application.model.ConfirmActions;

import java.time.LocalDate;

public class Search {
    private Controller controller;
    private Connection connection;
    public Search(Controller controller, Connection connection){
        this.controller = controller;
        this.connection = connection;
    }


    public void searchFlight() {
        System.out.println(controller.turR_checkBox_flight.isSelected() + " checkbox");

        String from = controller.from_input_flight.getText();
        String to = controller.disc_input_flight.getText();
        LocalDate d = controller.date_input_flight.getValue();

        if (!controller.turR_checkBox_flight.isSelected()){
            if (!(from.isEmpty()) && !(to.isEmpty())){
                if (d == null) {
                    boolean ok = ConfirmActions.confirmThisAction("Confirme action", "Search without date?", "To continue search without specifying the date confirm.");
                    if (ok) {
                        controller.avalibleFlights = connection.searchFlight(controller.from_input_flight.getText(), controller.disc_input_flight.getText(), null);
                    }
                } else {
                    controller.avalibleFlights = connection.searchFlight(controller.from_input_flight.getText(), controller.disc_input_flight.getText(), String.valueOf(d));
                }
                if (controller.avalibleFlights.isEmpty()){
                    controller.fillFlights(null);
                }else {
                    controller.fillFlights(controller.avalibleFlights);
                }
            }
        }else {

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
