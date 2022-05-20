package application.components.flight;

import application.Controller;
import application.ErrorHandler;
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
 * This class made for handling fetching flights information from database and handling all algorithms for solving flights seats.
 * @author Khabib developed by Kasper and Sossio
 */
public class FlightsViewManager {
    private ErrorHandler errorHandler;
    private Controller controller;

    /**
     * Constructor to FlightsViewManager.
     * @param controller instance of control class.
     * @author Khabib.
     */
    public FlightsViewManager(Controller controller) {
        this.controller = controller;
        errorHandler = new ErrorHandler(controller);
    }

    /**
     * The method reset advanced search text filed.
     * @author Sossio.
     */
    public void resetSearchFromTo() {
        controller.from_input_flight_textfield.clear();
        controller.display_input_flights.setText(null);
        controller.search_list_appear_second.setVisible(false);
        controller.search_list_appear_third.setVisible(false);
    }

    /**
     * The method reset search by country search field.
     * @author Sossio
     */
    public void resetSearchCountry() {
        controller.search_f_name.setText(null);
        controller.search_list_suggestion.setVisible(false);
    }


    /**
     * The method will show the flights list on the right side of the dashboard when a user choose a country
     * @param flights list of flights
     * @author Khabib
     */
    public void fetchFlights(ArrayList<Flight> flights) {
        boolean isFlight = checkFlightExistance(flights);
        if (isFlight){
            controller.flight_display_vbox.getChildren().clear();
            controller.flights_scrollpane.setVvalue(0);
            controller.nbr_of_available_flights.setText(String.valueOf(flights.size()));

            if(!controller.explore_mode) {
                try {
                    controller.booking_profile_image.setImage(controller.db.getProfilePicture(controller.user));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } // update profile picture
            }

            for (int i = 0; i < flights.size();i++){
                HBox hbox = createFlightsContent(flights, i);
                StackPane stackholer = new StackPane();
                stackholer.getChildren().add(hbox);
                stackholer.setAlignment(Pos.TOP_LEFT);
                controller.flight_display_vbox.getChildren().addAll(hbox); // the box
                controller.flight_display_vbox.setAlignment(Pos.TOP_LEFT);
                if (flights.get(i).isrTur()){
                    controller.round_trip_flights.add(flights.get(i));
                }
                int finalI1 = i;
                    hbox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                        if (!controller.explore_mode) {
                            boolean ready = preperBeforeCreatingSeats();
                            if (ready){
                                if (flights.get(finalI1).isrTur()){ // chose two-way
                                    System.out.println("A tur flight from event handler");
                                    fillInfoSeatPnl(flights, finalI1);
                                    createThisSeat(flights, finalI1);
                                    if(!controller.round_trip_flights.isEmpty()){
                                        controller.round_trip_flights.remove(finalI1); // remove one-way flight
                                        controller.has_return_flight = true; // set to true if there is more flight
                                    }
                                }else { // chose one-way
                                    fillInfoSeatPnl(flights, finalI1);
                                    createThisSeat(flights, finalI1);
                                }
                                controller.booking_seat_anchorpane.toFront();
                                controller.playSystemSound("Next page", "sounds/next_page.wav");
                            }

                        } else {
                            controller.playSystemSound("Error", "sounds/error.wav");
                            errorHandler.confirmThisAction("Information", "You must log in to go further!", "");
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
    }



    /**
     * The method take a specific flight's seats and display it in the application.
     * @param flights list of flights.
     * @param finalI1 index of each flight.
     * @author Khabib.
     */
    public void createThisSeat(ArrayList<Flight> flights, int finalI1) {
        controller.economy_seat_gridpane.getChildren().clear();
        controller.business_seat_gridpane.getChildren().clear();
        try {
            int[] amountSeats = controller.db.getSeatNumber(flights.get(finalI1).getId());
            boolean buildSeatsSuccess = chooseSeat(amountSeats[0], amountSeats[1]);
            if(buildSeatsSuccess){
                ArrayList<String> bookedS = controller.db.getBookedSeats(flights.get(finalI1).getId());
                if (!bookedS.isEmpty()){
                    for (String seat : bookedS){
                        if (seat.contains("E")){
                            controller.taken_seat_economy.add(seat);
                            disableBookedSeats(seat, controller.economy_seat_gridpane);
                        }
                        if(seat.contains("B")){
                            controller.taken_seat_business.add(seat);
                            disableBookedSeats(seat, controller.business_seat_gridpane);
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * The method proper panels and elements in gui before creating flight's seats.
     * @return value of true or false.
     * @author Khabib.
     */
    public boolean preperBeforeCreatingSeats() {
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


    /**
     * The method check for flight's existence.
     * @param flights list of flights.
     * @return value of true or false.
     * @author Khabib.
     */
    public boolean checkFlightExistance(ArrayList<Flight> flights) {
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


    /**
     * The method fill seat information into the seat panel window.
     * @param flights list of flights.
     * @param finalI1 index of specific flight.
     * @author Khabib.
     */
    public void fillInfoSeatPnl(ArrayList<Flight> flights, int finalI1) {
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
     * the method disable those seats that already booked.
     * @param seats amount of already booked seats.
     * @param grid javaFX element.
     * @author Khabib.
     */
    private void disableBookedSeats(String seats, GridPane grid) {
        for (int c = 0; c < grid.getChildren().size(); c++){
            if (seats.contains(grid.getChildren().get(c).getId())) {
                grid.getChildren().get(c).setDisable(true);
                grid.getChildren().get(c).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5; -fx-opacity: 1;");
                break;
            }
        }
    }


    /**
     * The method take both economy and business seats and validate.
     * @param econonySeats economy seats
     * @param businessSeats business seats
     * @author Khabib
     */
    public boolean chooseSeat(int econonySeats, int businessSeats) throws InterruptedException {
        controller.economy_seat_gridpane.getChildren().removeAll();
        controller.business_seat_gridpane.getChildren().removeAll();
        // 72/6 = 12
        // 12 row
        // 6 column
        //
        if (econonySeats%6 == 0){
            for(int row = 0;row < econonySeats/6; row++){ // cal
                for(int col = 0;col < 6; col++){ // row
                    build_eco_seats(row,col, false); // business is false for now
                }
            }
        }

        if (businessSeats%6 == 0){
            for(int row = 0;row < businessSeats/6; row++){ // cal
                for(int col = 0;col < 6; col++){ // row
                    build_eco_seats(row,col, true); // business is false for now
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
    }


    /**
     * The method create content of flights list
     * @param flights list of flights
     * @param i index of specific flight
     * @return return a javaFX element : HBox
     * @author Khabib
     */
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


    /**
     * The method display business and economy seats on seat panel.
     * @param rowIndex row index
     * @param columnIndex column index
     * @param business value of true or false
     * @author Khabib
     */
    public void build_eco_seats(int rowIndex, int columnIndex, boolean business) {
        Label label = createSeatItem();
        if(!business) {
            //<editor-fold desc="short">
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
                toggleSeatColor(); // restore seats
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
                toggleSeatColor(); // restore seats


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


    /**
     * The method is used for each time creating a label on each seat number.
     * @return return a label with seat id.
     * @author Khabib
     */
    public Label createSeatItem(){
        Label label = new Label();
        label.setMinWidth(30);
        label.setMinHeight(30);
        label.setText(label.getId());
        label.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(0))));
        return label;
    }


    /**
     * The method handle changing color when a seat clicked.
     * @author Khabib
     */
    public void toggleSeatColor() {
        for (int ge = 0; ge < controller.economy_seat_gridpane.getChildren().size(); ge++){ // ge store for grid-economy
            if (!controller.taken_seat_economy.contains(controller.economy_seat_gridpane.getChildren().get(ge).getId())){
                controller.economy_seat_gridpane.getChildren().get(ge).setOpacity(1);
                //gridE.getChildren().get(ge).setStyle("-fx-background-color: #AEFF47; -fx-background-radius: 5; -fx-opacity: 1;"); // restore all seats
            }
        }
        for (int gb = 0; gb < controller.business_seat_gridpane.getChildren().size(); gb++){ // gb stor for grid-business
            if (!controller.taken_seat_business.contains(controller.business_seat_gridpane.getChildren().get(gb).getId())){
                controller.business_seat_gridpane.getChildren().get(gb).setOpacity(1);
                //gridB.getChildren().get(gb).setStyle("-fx-background-color: #AEFF47; -fx-background-radius: 5; -fx-opacity: 1;"); // restore all seats
            }
        }
    }
}
