package application.components.flight;

import javafx.scene.control.CheckBox;

/**
 * This class saves data to flight.
 */
public class Flight {
    private int size = 1;
    private String id, departure_name, destination_name, departure_date, destination_date,  departure_time, destination_time, price, p_id;
    private boolean rTur = false;
    private CheckBox box;
    /**
     * @param id of flight.
     * @param departure_name text.
     * @param departure_date date.
     * @param departure_time time.
     * @param destination_name text.
     * @param destination_date date.
     * @param destination_time time.
     * @param price of flight.
     */
    public Flight( String id, String departure_name, String departure_date, String departure_time,String destination_name, String destination_date, String destination_time, String price, boolean rTur, int size, String p_id){
        this.size = size;
        this.id = id;
        this.rTur = rTur;
        this.departure_name = departure_name;
        this.departure_date = departure_date;
        this.departure_time = departure_time;
        this.destination_name = destination_name;
        this.destination_date = destination_date;
        this.destination_time = destination_time;
        this.price = price;
        this.p_id = p_id;
        this.box = new CheckBox();
    }

    // ----- GETTER AND SETTERS ----- //
    public int getSize()
    {
        return size;
    }
    public String getDeparture_time() {
        return departure_time;
    }
    public String getDeparture_name() {
        return departure_name;
    }
    public String getDestination_name() {
        return destination_name;
    }
    public String getDeparture_date() {
        return departure_date;
    }
    public String getId() {
        return id;
    }
    public String getDestination_date() {
        return destination_date;
    }
    public String getDestination_time() {
        return destination_time;
    }
    public String getPrice() {
        return price;
    }
    public String getP_id()
    {
        return p_id;
    }
    public CheckBox getFlightBox()
    {
        return box;
    }
    public boolean isrTur() {
        return rTur;
    }
}