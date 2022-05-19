package application.components.ticket;
import javafx.scene.control.CheckBox;

/**
 *
 * This class saves data to user history of booked tickets.
 * @author Khabib
 */
public class UserHistory {
    private int         no_col_table_historik,
                        flightid_col_table_historik, userId;
    private String      company_col_table_historik,model_col_table_historik,
                        rfc_col_table_historik, from_col_table_historik, to_col_table_historik,
                        date_col_table_historik, seatno_col_table_historik, dep_date, des_date , dep_time, des_time;
    private double      price_col_table_historik;
    private CheckBox    select_col_table_historik;
    private boolean     checkedIn;

    /**
     *
     * @param userId user's id : int
     * @param no_col_table_historik user's counter : int
     * @param company_col_table_historik airline name : string
     * @param model_col_table_historik plane model : string
     * @param rfc_col_table_historik reference number to ticket : string
     * @param flightid_col_table_historik flight's id : int
     * @param from_col_table_historik departure name : string
     * @param to_col_table_historik destination name : string
     * @param seatno_col_table_historik seat number : string
     * @param date_col_table_historik date of ticket : string
     * @param price_col_table_historik price of ticket : double
     * @param dep_time departure time : string
     * @param des_time destination time : string
     * @param dep_date departure date : string
     * @param des_date destination date : string
     * @param checkedIn check in status : boolean
     * @author Khabib
     */
    public UserHistory(int userId, int no_col_table_historik, String company_col_table_historik, String model_col_table_historik, String rfc_col_table_historik,
                       int flightid_col_table_historik, String from_col_table_historik, String to_col_table_historik,
                       String seatno_col_table_historik, String date_col_table_historik, double price_col_table_historik, String dep_time, String des_time, String dep_date, String des_date, boolean checkedIn){

        this.userId                         = userId;
        this.no_col_table_historik          = no_col_table_historik;
        this.company_col_table_historik     = company_col_table_historik;
        this.model_col_table_historik       = model_col_table_historik;
        this.rfc_col_table_historik         = rfc_col_table_historik;
        this.flightid_col_table_historik    = flightid_col_table_historik;
        this.from_col_table_historik        = from_col_table_historik;
        this.to_col_table_historik          = to_col_table_historik;
        this.seatno_col_table_historik      = seatno_col_table_historik;
        this.date_col_table_historik        = date_col_table_historik;
        this.price_col_table_historik       = price_col_table_historik;
        this.select_col_table_historik      = new CheckBox();
        this.dep_time                       = dep_time;
        this.des_time                       = des_time;
        this.dep_date                       = dep_date;
        this.des_date                       = des_date;
        this.checkedIn                      = checkedIn;
    }


    // ----- GETTER AND SETTERS ----- //
    public String getSeatno_col_table_historik() {
        return seatno_col_table_historik;
    }

    public int getFlightid_col_table_historik() {
        return flightid_col_table_historik;
    }

    public String getModel_col_table_historik() {
        return model_col_table_historik;
    }

    public String getRfc_col_table_historik() {
        return rfc_col_table_historik;
    }

    public String getFrom_col_table_historik() {
        return from_col_table_historik;
    }

    public String getTo_col_table_historik() {
        return to_col_table_historik;
    }


    public double getPrice_col_table_historik() {
        return price_col_table_historik;
    }

    public CheckBox getSelect_col_table_historik() {
        return select_col_table_historik;
    }

    public String getDep_time() {
        return dep_time;
    }

    public String getDes_time() {
        return des_time;
    }

    public String getDep_date() {
        return dep_date;
    }

    public String getDes_date() {
        return des_date;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

}
