package application.Components;

import application.Controller;
import application.model.Flight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

/**
 * #comment (comment this class and create javadoc to every method)
 */
public class FlightsViewManager {

    public void resetSearchFromTo(Controller controller) {
        controller.from_input_flight_textfield.setText("");
        controller.display_input_flights.setText("");
        controller.search_list_appear_second.setVisible(false);
        controller.search_list_appear_third.setVisible(false);
    }
    public void resetSearchCountry(Controller controller) {
        controller.search_f_name.setText("");
        controller.search_list_appear.setVisible(false);
    }

    public void fillFlights (ArrayList<Flight> flights, Controller controller) {
        boolean isFlight = controller.checkFlightExistance(flights);
        if (isFlight){
            controller.flight_display_vbox.getChildren().clear();
            controller.flights_scrollpane.setVvalue(0);
            controller.nbr_of_available_flights.setText(String.valueOf(flights.size()));
            try {
                controller.booking_profile_image.setImage(controller.connection.getProfilePicture(controller.user));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } // update profile picture

            for (int i = 0; i < flights.size();i++){
                HBox hbox = controller.createFlightsContent(flights, i);
                StackPane stackholer = new StackPane();
                stackholer.getChildren().add(hbox);
                stackholer.setAlignment(Pos.TOP_LEFT);
                controller.flight_display_vbox.getChildren().addAll(hbox); // the box
                controller.flight_display_vbox.setAlignment(Pos.TOP_LEFT);
                if (flights.get(i).isrTur()){
                    controller.round_trip_flights.add(flights.get(i));
                }

                int finalI1 = i;
                // to click
                hbox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    boolean ready = controller.preperBeforeCreatingSeats();
                    if (ready){
                        if (flights.get(finalI1).isrTur()){ // chose two-way
                            System.out.println("A tur flight from event handler");
                            controller.fillInfoSeatPnl(flights, finalI1);
                            controller.createThisSeat(flights, finalI1);
                            if(!controller.round_trip_flights.isEmpty()){
                                controller.round_trip_flights.remove(finalI1); // remove one-way flight
                                controller.has_return_flight = true; // set to true if there is more flight
                            }
                        }else { // chose one-way
                            controller.fillInfoSeatPnl(flights, finalI1);
                            controller.createThisSeat(flights, finalI1);
                        }
                        controller.booking_seat_anchorpane.toFront();
                    }
                });
                // to hover
                hbox.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                    hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#112"), CornerRadii.EMPTY, Insets.EMPTY)));
                });
                hbox.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
                });
            }
        }else {
            controller.nbr_of_available_flights.setText("0");
        }
    } // the method will show the flights list on the right side of the dashboard when a user choose a country

    public void createThisSeat(ArrayList<Flight> flights, int finalI1, Controller controller) {

        controller.economy_seat_gridpane.getChildren().clear();
        controller.business_seat_gridpane.getChildren().clear();

        try {
            int[] amountSeats = controller.connection.getSeatNumber(flights.get(finalI1).getId());
            boolean buildSeatsSuccess = controller.chooseSeat(amountSeats[0], amountSeats[1]);
            if(buildSeatsSuccess){
                ArrayList<String> bookedS = controller.connection.getBookedSeats(flights.get(finalI1).getId());
                if (!bookedS.isEmpty()){
                    for (String seat : bookedS){
                        if (seat.contains("E")){
                            controller.taken_seat_economy.add(seat);
                            showTakenS(seat, controller.economy_seat_gridpane);
                        }
                        if(seat.contains("B")){
                            controller.taken_seat_business.add(seat);
                            showTakenS(seat, controller.business_seat_gridpane);
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public boolean preperBeforeCreatingSeats(Controller controller) {
        controller.taken_seat_economy.clear();
        controller.taken_seat_business.clear();

        controller.flight_seats_eco_anchorpane.getChildren().clear();
        controller.flights_seats_business_anchorpane.getChildren().clear();
        // Seat window
        controller.economy_seat_gridpane.setHgap(5);
        controller.economy_seat_gridpane.setVgap(5);
        controller.business_seat_gridpane.setHgap(5);
        controller.business_seat_gridpane.setVgap(5);
        HBox hboxLR_seat = new HBox();
        hboxLR_seat.getChildren().addAll(controller.economy_seat_gridpane);
        HBox hboxTLR_seat = new HBox();
        hboxTLR_seat.getChildren().add(controller.business_seat_gridpane);
        hboxTLR_seat.setAlignment(Pos.TOP_CENTER);
        controller.flight_seats_eco_anchorpane.getChildren().add(hboxLR_seat);
        controller.flights_seats_business_anchorpane.getChildren().add(hboxTLR_seat);
        return true;
    }



    // check if there is any flight for searched name
    public boolean checkFlightExistance(ArrayList<Flight> flights,Controller controller) {
        boolean flight = false;
        if (flights == null){
            Label lable = new Label("No flight available!");
            lable.setStyle("-fx-text-fill: #999; -fx-padding: 20px");
            if (!controller.flight_display_vbox.getChildren().isEmpty()){
                controller.flight_display_vbox.getChildren().clear();
                controller.flight_display_vbox.getChildren().add(lable);
            }else {
                controller.flight_display_vbox.getChildren().add(lable);
            }
        }else
            flight = true;

        return flight;
    }

    // create content of the flights list
    public HBox createFlightsContent(ArrayList<Flight> flights, int i) {
        HBox hbox = new HBox(1);

        VBox vBoxLeft = new VBox();
        VBox vBoxCenter = new VBox();
        VBox vBoxRight = new VBox();

        Font font = Font.font("Futura", FontWeight.BOLD, 12);
        Font font2 = Font.font("Futura", FontWeight.NORMAL, 10);

        Label departure = new Label("Departure:");
        departure.setPadding(new Insets(0,0,10,0));
        departure.setFont(font2);
        Label country = new Label(flights.get(i).getDeparture_name().replace("_"," "));
        if (country.getText().length() >= 15) {
            country.setWrapText(true);
            country.setMinHeight(35);
        }
        country.setFont(font);
        Label date = new Label(flights.get(i).getDeparture_time().substring(0,5) + " | " + flights.get(i).getDeparture_date());
        date.setFont(font2);
        vBoxLeft.getChildren().addAll(departure,country,date);

        Label destination = new Label("Destination:");
        destination.setPadding(new Insets(0,0,10,0));
        destination.setFont(font2);
        Label country2 = new Label(flights.get(i).getDestination_name().replace("_"," "));
        if (country2.getText().length() >= 15) {
            country2.setWrapText(true);
            country2.setMinHeight(35);
        }
        country2.setFont(font);
        Label date2 = new Label(flights.get(i).getDestination_time().substring(0,5) + " | " + flights.get(i).getDestination_date());
        date2.setFont(font2);
        vBoxCenter.getChildren().addAll(destination,country2,date2);

        Button button = new Button(flights.get(i).getPrice() + " SEK");

        button.setFont(font);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #ff7000");
        vBoxRight.getChildren().add(button);


                /* // match tur and return flights

                // Change tur and return flights to a different color
                for (Flight flight : flights) {
                    if (flights.get(i).getDeparture_name().equals(flight.getDestination_name())) {
                        hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#fb3584"), CornerRadii.EMPTY, Insets.EMPTY)));
                    }else {
                        hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
                 */ // match tur and return flights

        vBoxLeft.setMaxWidth(105);
        vBoxLeft.setMinWidth(105);
        vBoxCenter.setMaxWidth(105);
        vBoxCenter.setMinWidth(105);

        hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
        hbox.getChildren().addAll(vBoxLeft,vBoxCenter,vBoxRight);
        hbox.setPadding(new Insets(5));
        hbox.setMargin(vBoxLeft,new Insets(10,0,10,10));
        hbox.setMargin(vBoxCenter,new Insets(10,0,10,0));
        hbox.setMargin(vBoxRight,new Insets(30,15,10,0));
        hbox.setEffect(new DropShadow(2.0, Color.BLACK));
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setSpacing(30);

        return hbox;
    }

    // fill information to seat pnl.
    public void fillInfoSeatPnl(ArrayList<Flight> flights, int finalI1, Controller controller) {
        controller.booking_first_name_textfield.setText(controller.user.getFirstName());
        controller.booking_last_name_textfield.setText(controller.user.getLastName());
        controller.booking_email_textfield.setText(controller.user.getEmail());
        controller.booking_departure_lbl.setText(flights.get(finalI1).getDeparture_name());
        controller.booking_destination_lbl.setText(flights.get(finalI1).getDestination_name());
        controller.booking_departure_extra_lbl.setText(flights.get(finalI1).getDeparture_date());
        controller.booking_destination_extra_lbl.setText(flights.get(finalI1).getDestination_date());
        controller.booking_flight_number_lbl.setText(flights.get(finalI1).getId());
        controller.seat_price = Double.parseDouble(flights.get(finalI1).getPrice());
        controller.booking_is_retur_lbl.setText(String.valueOf(flights.get(finalI1).isrTur()));
        controller.booking_price_lbl.setText(String.valueOf(controller.seat_price));
        // profile picture

        // chosen flights color
        for (int g = 0; g < controller.flight_display_vbox.getChildren().size(); g++){
            controller.flight_display_vbox.getChildren().get(g).setOpacity(1);
        }
        controller.flight_display_vbox.getChildren().get(finalI1).setOpacity(0.95);

    }

    /**
     * @param seats
     * @param grid
     */
    private void showTakenS(String seats, GridPane grid) {
        System.out.println(seats + " booked");
        for (int c = 0; c < grid.getChildren().size(); c++){
            if (seats.contains(grid.getChildren().get(c).getId())) {
                grid.getChildren().get(c).setDisable(true);
                grid.getChildren().get(c).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5; -fx-opacity: 1;");
                break;
            }
        }
    }

    public boolean chooseSeat(int econonySeats, int businessSeats, Controller controller) throws InterruptedException {
        controller.economy_seat_gridpane.getChildren().removeAll();
        controller.business_seat_gridpane.getChildren().removeAll();
        // 72/6 = 12
        // 12 row
        // 6 column
        //
        if (econonySeats%6 == 0){
            for(int row = 0;row < econonySeats/6; row++){ // cal
                for(int col = 0;col < 6; col++){ // row
                    controller.build_eco_seats(row,col, false); // business is false for now
                }
            }
        }

        if (businessSeats%6 == 0){
            for(int row = 0;row < businessSeats/6; row++){ // cal
                for(int col = 0;col < 6; col++){ // row
                    controller.build_eco_seats(row,col, true); // business is false for now
                }
            }
        }

        /*
        for(int i = 0;i < businessSeats/3; i++){ // cal
            for(int j = 0;j <businessSeats/3; j++){ // row
                business = true;
                build_eco_seats(i,j, business);
            }
        }

         */

        Instant start = Instant.now();
        Thread.sleep(1000);
        Instant end = Instant.now();
        System.out.println("timer: " + start + " end: " + end); // prints PT1M3.553S
        return true;
    }// the method will show the chosen seat on the screen

    public void build_eco_seats(int rowIndex, int columnIndex, boolean business, Controller controller) {

        //grid_left.setColumnIndex(label, columnIndex);
        /*
        if (business){
            System.out.println("business: " + business);
            grid_business.add(label, columnIndex,rowIndex);

        }
            */
        if(!business) {
            //<editor-fold desc="short">
            Label label = controller.createSeatItem();
            label.setId("E" + rowIndex+ columnIndex);
            if (controller.economy_seat_gridpane.getColumnCount() == 2 && controller.economy_seat_gridpane.getRowCount() == 1){
                controller.economy_seat_gridpane.add(label, columnIndex, rowIndex);
                controller.economy_seat_gridpane.setMargin(label, new Insets(0, 20, 0, 0));
            } else if (controller.economy_seat_gridpane.getColumnCount() == 3 && controller.economy_seat_gridpane.getRowCount() > 1) {
                System.out.println("column 4");
                controller.economy_seat_gridpane.setMargin(label, new Insets(0, 0, 0, 20));
                controller.economy_seat_gridpane.add(label, columnIndex, rowIndex);
            }
            else {
                controller.economy_seat_gridpane.add(label, columnIndex, rowIndex);
            }
            //</editor-fold>
            //grid_left.getColumnCount();
            label.setOnMouseClicked(e ->{
                controller.booking_price_lbl.setText(String.valueOf(controller.seat_price));

                controller.booking_seat_number_lbl.setText(label.getId());
                controller.toggleSeatColor(); // restore seats
                // seat color change
                for (int i = 0; i < controller.economy_seat_gridpane.getChildren().size(); i++){
                    controller.economy_seat_gridpane.getChildren().get(i).setOpacity(1);
                    if (!Objects.equals(controller.economy_seat_gridpane.getChildren().get(i).getId(), label.getId())){
                        controller.economy_seat_gridpane.getChildren().get(i).setOpacity(0.5);
                        for (String taken : controller.taken_seat_economy){
                            if (taken.equals(controller.economy_seat_gridpane.getChildren().get(i).getId())){
                                controller.economy_seat_gridpane.getChildren().get(i).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5;");
                            }
                        }
                    }
                }
            });
        }else {

            //<editor-fold desc="short">
            Label label = controller.createSeatItem();
            label.setId("B" + rowIndex+ columnIndex);
            if (controller.business_seat_gridpane.getColumnCount() == 2 && controller.business_seat_gridpane.getRowCount() == 1){
                System.out.println("column 3");
                controller.business_seat_gridpane.add(label, columnIndex, rowIndex);
                controller.business_seat_gridpane.setMargin(label, new Insets(0, 20, 0, 0));
            } else if (controller.business_seat_gridpane.getColumnCount() == 3 && controller.business_seat_gridpane.getRowCount() > 1) {
                System.out.println("column 4");
                controller.business_seat_gridpane.setMargin(label, new Insets(0, 0, 0, 20));
                controller.business_seat_gridpane.add(label, columnIndex, rowIndex);
            }
            else {
                controller.business_seat_gridpane.add(label, columnIndex, rowIndex);
            }
            //</editor-fold>
            label.setOnMouseClicked(e ->{

                controller.booking_price_lbl.setText(String.valueOf(controller.seat_price * 1.1));
                controller.booking_seat_number_lbl.setText(label.getId());
                controller.toggleSeatColor(); // restore seats


                // seat color change
                for (int i = 0; i < controller.business_seat_gridpane.getChildren().size(); i++){
                    controller.business_seat_gridpane.getChildren().get(i).setOpacity(1);
                    if (!Objects.equals(controller.business_seat_gridpane.getChildren().get(i).getId(), label.getId())){
                        controller.business_seat_gridpane.getChildren().get(i).setOpacity(0.5);
                        for (String taken : controller.taken_seat_business){
                            if (taken.equals(controller.business_seat_gridpane.getChildren().get(i).getId())){
                                System.out.println("business taken seats exist");
                                controller.business_seat_gridpane.getChildren().get(i).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5;");
                            }
                        }
                    }
                }
            });
        }
    }
}
