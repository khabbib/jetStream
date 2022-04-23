package application.model;

import javafx.scene.control.CheckBox;

import java.awt.*;

public class UserHistory {
    private int         no_col_table_historik,
                        seatno_col_table_historik, flightid_col_table_historik;
    private String      company_col_table_historik,model_col_table_historik,
                        rfc_col_table_historik, from_col_table_historik, to_col_table_historik,
                        date_col_table_historik;
    private double      price_col_table_historik;
    private CheckBox select_col_table_historik;

    public UserHistory(int no_col_table_historik, String company_col_table_historik, String model_col_table_historik, String rfc_col_table_historik,
                       int flightid_col_table_historik, String from_col_table_historik, String to_col_table_historik,
                       int seatno_col_table_historik, String date_col_table_historik, double price_col_table_historik){

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

    }


    public int getNo_col_table_historik() {
        return no_col_table_historik;
    }

    public void setNo_col_table_historik(int no_col_table_historik) {
        this.no_col_table_historik = no_col_table_historik;
    }

    public int getSeatno_col_table_historik() {
        return seatno_col_table_historik;
    }

    public void setSeatno_col_table_historik(int seatno_col_table_historik) {
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
}
