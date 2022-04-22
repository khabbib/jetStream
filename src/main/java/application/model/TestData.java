package application.model;

public class TestData {
    private String no_col_table_historik, bookid_col_table_historik,
            flightid_col_table_historik,from_col_table_historik, to_col_table_historik,
            seatno_col_table_historik, date_col_table_historik;

    public TestData(String no_col_table_historik, String bookid_col_table_historik,
                    String flightid_col_table_historik,String from_col_table_historik, String to_col_table_historik,
                    String seatno_col_table_historik, String date_col_table_historik){

        this.no_col_table_historik = no_col_table_historik;
        this.bookid_col_table_historik = bookid_col_table_historik;
        this.flightid_col_table_historik = flightid_col_table_historik;
        this.from_col_table_historik = from_col_table_historik;
        this.to_col_table_historik = to_col_table_historik;
        this.seatno_col_table_historik = seatno_col_table_historik;
        this.date_col_table_historik = date_col_table_historik;


    }


    public String getNo_col_table_historik() {
        return no_col_table_historik;
    }

    public String getBookid_col_table_historik() {
        return bookid_col_table_historik;
    }

    public String getFlightid_col_table_historik() {
        return flightid_col_table_historik;
    }

    public String getFrom_col_table_historik() {
        return from_col_table_historik;
    }

    public String getTo_col_table_historik() {
        return to_col_table_historik;
    }

    public String getSeatno_col_table_historik() {
        return seatno_col_table_historik;
    }

    public String getDate_col_table_historik() {
        return date_col_table_historik;
    }
}
