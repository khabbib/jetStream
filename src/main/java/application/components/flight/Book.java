package application.components.flight;

import javafx.scene.control.CheckBox;

/**
 * This class refer to booked ticket.
 * @author Habib
 */
public class Book {
    private int counter = 1;
    private String user_id, flight_id, seatNbr, b_date;
    private boolean business;
    private CheckBox box_flight;

    /**
     * Constructor to Book.
     * @param user_id user's id : string
     * @param flight_id flight's id : string
     * @param seatNbr seat number : string
     * @param b_date purchased date : string
     * @param business business seat : boolean
     * @param counter ticket's counter : int
     * @author Habib
     */
    public Book(String user_id, String flight_id, String seatNbr, String b_date, boolean business, int counter){
        this.box_flight = new CheckBox();
        this.flight_id = flight_id;
        this.business = business;
        this.counter = counter;
        this.seatNbr = seatNbr;
        this.user_id = user_id;
        this.b_date = b_date;
    }

    // ----- GETTER AND SETTERS ----- //
    public int getCounter()
    {
        return counter;
    }
    public String getUser_id() {
        return user_id;
    }
    public String getFlight_id() {
        return flight_id;
    }
    public String getSeatNbr() {
        return seatNbr;
    }
    public String getDate(){
        return b_date;
    }
    public boolean isBusiness() {
        return business;
    }
    public CheckBox getBox_flight()
    {
        return box_flight;
    }

}
