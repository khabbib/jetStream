package application.components.ticket;

import javafx.scene.control.CheckBox;

/**
 * #comment (comment this class and create javadoc to every method)
 * This class saves data to user history of booked tickets.
 */
public class UserHistory {
    private int         no_col_table_historik,
                        flightid_col_table_historik, userId;
    private String      company_col_table_historik,model_col_table_historik,
                        rfc_col_table_historik, from_col_table_historik, to_col_table_historik,
                        date_col_table_historik, seatno_col_table_historik, dep_date, des_date , dep_time, des_time;
    private double      price_col_table_historik;
    private CheckBox select_col_table_historik;
    private boolean checkedIn;

    /**
     * @param no_col_table_historik
     * @param company_col_table_historik
     * @param model_col_table_historik
     * @param rfc_col_table_historik
     * @param flightid_col_table_historik
     * @param from_col_table_historik
     * @param to_col_table_historik
     * @param seatno_col_table_historik
     * @param date_col_table_historik
     * @param price_col_table_historik
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
    public int getNo_col_table_historik() {
        return no_col_table_historik;
    }
    public void setNo_col_table_historik(int no_col_table_historik) {
        this.no_col_table_historik = no_col_table_historik;
    }

    public String getSeatno_col_table_historik() {
        return seatno_col_table_historik;
    }
    public void setSeatno_col_table_historik(String seatno_col_table_historik) {
        this.seatno_col_table_historik = seatno_col_table_historik;
    }

    public int getFlightid_col_table_historik() {
        return flightid_col_table_historik;
    }
    public void setFlightid_col_table_historik(int flightid_col_table_historik) {
        this.flightid_col_table_historik = flightid_col_table_historik;
    }

    public String getCompany_col_table_historik() {
        return company_col_table_historik;
    }
    public void setCompany_col_table_historik(String company_col_table_historik) {
        this.company_col_table_historik = company_col_table_historik;
    }

    public String getModel_col_table_historik() {
        return model_col_table_historik;
    }
    public void setModel_col_table_historik(String model_col_table_historik) {
        this.model_col_table_historik = model_col_table_historik;
    }

    public String getRfc_col_table_historik() {
        return rfc_col_table_historik;
    }
    public void setRfc_col_table_historik(String rfc_col_table_historik) {
        this.rfc_col_table_historik = rfc_col_table_historik;
    }

    public String getFrom_col_table_historik() {
        return from_col_table_historik;
    }
    public void setFrom_col_table_historik(String from_col_table_historik) {
        this.from_col_table_historik = from_col_table_historik;
    }

    public String getTo_col_table_historik() {
        return to_col_table_historik;
    }
    public void setTo_col_table_historik(String to_col_table_historik) {
        this.to_col_table_historik = to_col_table_historik;
    }

    public String getDate_col_table_historik() {
        return date_col_table_historik;
    }
    public void setDate_col_table_historik(String date_col_table_historik) {
        this.date_col_table_historik = date_col_table_historik;
    }

    public double getPrice_col_table_historik() {
        return price_col_table_historik;
    }
    public void setPrice_col_table_historik(double price_col_table_historik) {
        this.price_col_table_historik = price_col_table_historik;
    }

    public CheckBox getSelect_col_table_historik() {
        return select_col_table_historik;
    }
    public void setSelect_col_table_historik(CheckBox select_col_table_historik) {
        this.select_col_table_historik = select_col_table_historik;
    }

    public String getDep_time() {
        return dep_time;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public String getDes_time() {
        return des_time;
    }

    public void setDes_time(String des_time) {
        this.des_time = des_time;
    }

    public String getDep_date() {
        return dep_date;
    }

    public void setDep_date(String dep_date) {
        this.dep_date = dep_date;
    }

    public String getDes_date() {
        return des_date;
    }

    public void setDes_date(String des_date) {
        this.des_date = des_date;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public int getUserId() {
        return userId;
    }
}
