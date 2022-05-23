package application.components.flight;
import javafx.scene.control.CheckBox;

/**
 * This class saves data as an object of flight ticket.
 * @author Khabib
 */
public class Flight {
    private int size = 1;
    private String id, departure_name, destination_name, departure_date, destination_date,  departure_time, destination_time, price, p_id;
    private boolean rTur = false;
    private CheckBox select_col_flight_admin;

    /**
     * Constructor to Flight
     * @param id id refer to database table's row. : string
     * @param departure_name departure name : string
     * @param departure_date departure date : string
     * @param departure_time departure time : string
     * @param destination_name destination name : string
     * @param destination_date destination date : string
     * @param destination_time destination time : string
     * @param price price of ticket : string
     * @param rTur return flight : boolean
     * @param size flight's counter : int
     * @param p_id airplane's id : string
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
        this.select_col_flight_admin = new CheckBox();
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
        return select_col_flight_admin;
    }
    public boolean isrTur() {
        return rTur;
    }
}