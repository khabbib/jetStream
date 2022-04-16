package application.model;

/**
 *
 */
public class Flight {
    private String id, departure_name, destination_name, departure_date, destination_date,  departure_time, destination_time, price;

    /**
     * @param id
     * @param departure_name
     * @param departure_date
     * @param departure_time
     * @param destination_name
     * @param destination_date
     * @param destination_time
     * @param price
     */
    public Flight(String id, String departure_name, String departure_date, String departure_time,String destination_name, String destination_date, String destination_time, String price){
        this.id = id;
        this.departure_name = departure_name;
        this.departure_date = departure_date;
        this.departure_time = departure_time;
        this.destination_name = destination_name;
        this.destination_date = destination_date;
        this.destination_time = destination_time;
        this.price = price;
    }

    // ----- GETTER AND SETTERS ----- //
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
}