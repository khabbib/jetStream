package application.components.flight;

import application.Controller;
import application.ErrorHandler;
import application.api.Db;
import javafx.event.ActionEvent;

import java.time.LocalDate;

/**
 * This class made for handling the search terms in application.
 */
public class Search {
    private ErrorHandler errorHandler;
    private Controller controller;
    private Db db;

    /**
     * Constructor to Search.
     * @param controller instance of control class.
     * @param db instance of Db (database) class.
     * @param errorHandler instance of ErrorHandler class.
     * @author Khabib.
     */
    public Search(Controller controller, Db db, ErrorHandler errorHandler){
        this.errorHandler = errorHandler;
        this.controller = controller;
        this.db = db;
    }

    /**
     * The method check for return trip.
     * @param e event.
     * @author Khabib.
     */
    public void checkboxEvent(ActionEvent e){
        if (controller.round_trip_checkbox.isSelected()){
            controller.return_date_pick_hbox.setDisable(false);
            controller.date_next_day_return_button.setDisable(false);
            controller.date_previous_day_return_button.setDisable(false);
        }else {
            controller.return_date_pick_hbox.setDisable(true);
            controller.date_next_day_return_button.setDisable(true);
            controller.date_previous_day_return_button.setDisable(true);
        }

    }

    /**
     * The method search flights based on date and trip.
     * @author Khabib.
     */
    public void advanceSearch() {
        String from = controller.from_input_flight_textfield.getText();
        LocalDate Rd = controller.dateR_input_flight.getValue();
        String to = controller.display_input_flights.getText();
        LocalDate d = controller.date_input_flight.getValue();

        if (!controller.round_trip_checkbox.isSelected()){
            if (!(from.isEmpty()) && !(to.isEmpty())){
                if (d == null) {
                    boolean ok = errorHandler.confirmThisAction("Confirm action", "Search without date?", "To continue search without specifying the date confirm.");
                    if (ok) {
                        controller.available_flights_list = db.searchFlight(from, to, null, null);
                    }
                } else {
                    controller.available_flights_list = db.searchFlight(from, to, String.valueOf(d), null);
                }
            }
        }else {
            if (!from.isEmpty() && !to.isEmpty()){
                if (!(d == null) && (!(Rd ==null))){
                    controller.available_flights_list = db.searchFlight(from, to, String.valueOf(d), String.valueOf(Rd));
                }else
                    controller.available_flights_list = db.searchFlight(from, to, null, null);
            }
        }

        // fill out the flights on  screen
        if (controller.available_flights_list.isEmpty()){
            controller.fetchFlights(null);
        }else {
            System.out.println(controller.available_flights_list.size() );
            controller.fetchFlights(controller.available_flights_list);
        }
    }


    /**
     * The method called from "Search field" in GUI. It fetches flights from Db based on search term.
     * @author Khabib.
     */
    public void searchHit() {
        if (!controller.search_f_name.getText().isEmpty()){
            controller.available_flights_list.clear();
            controller.available_flights_list = db.seachFlightFromSearchField(controller.search_f_name.getText());
            if (!controller.available_flights_list.isEmpty()){
                controller.fetchFlights(controller.available_flights_list);
            }
        }
    }
}
