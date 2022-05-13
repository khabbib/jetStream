package application.components.reservation;

import javafx.scene.control.CheckBox;

/**
 * #comment (comment this class and create javadoc to every method)
 * This class saves data to book.
 */
public class Book {
    private int counter = 1;
    private String valdeResa;
    private String user_id, flight_id, seatNbr, b_date;
    private boolean business;
    private CheckBox box_flight;
    /**
     * @param user_id
     * @param flight_id
     * @param seatNbr
     * @param b_date
     * @param business
     */
    public Book(String user_id, String flight_id, String seatNbr, String b_date, boolean business, int counter){
        this.counter = counter;
        this.business = business;
        this.user_id = user_id;
        this.flight_id = flight_id;
        this.seatNbr = seatNbr;
        this.b_date = b_date;
        this.box_flight = new CheckBox();
    }

    // ----- GETTER AND SETTERS ----- //
    public int getCounter()
    {
        return counter;
    }
    public String getValdeResa() {
        return valdeResa;
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
