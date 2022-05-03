package application.database;

import application.Controller;
import application.model.*;
import javafx.scene.image.Image;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class connects java to pgadmin to claim data from database.
 */
public class Connection {
    private Controller controller;
    public Connection(Controller controller) {
        this.controller = controller;
    }

    /**
     * Get the database connection.
     * @return connection of the database.
     * @author Sossio.
     */
    public static java.sql.Connection getDatabaseConnection() {

        String url = "jdbc:postgresql://pgserver.mau.se:5432/am2510";
        String user = "am2510";
        String password = "zyvl0ir7";

        java.sql.Connection con = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Register new user
     * @param
     * @return
     * @throws SQLException
     * @author Khabib.
     */
    public boolean saveUser(String first_name_reg, String last_name_reg, String address_reg, String email_reg, String phone_number_reg, String password_reg, boolean isAdmin) throws SQLException {
        boolean ok = false;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("insert into userr(u_f_name, u_l_name, u_address, u_email, u_phone_nr, u_password, u_isAdmin) values('" + first_name_reg + "' , '" + last_name_reg + "' , '" + address_reg+ "' , '" + email_reg +"' , '" + phone_number_reg + "', '" + password_reg +"', '" + isAdmin + "')");
        ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + email_reg +"'");
        while (rs.next()){
            System.out.println("User "+ first_name_reg +" registered.");
        }
        ok= true;
        con.close();
        stmt.close();
        return ok;
    }

    /**
     * @param user takes as a parameter to edit user information.
     * @throws SQLException if any sql issue occurs.
     * @author Kasper. Developed by Sossio.
     */
    public boolean updateUser(User user, String dbEmail) throws SQLException {
        boolean uniqueEmail = true;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        ResultSet rs = stmt.executeQuery("select u_email from userr");

        while(rs.next()) {
            if(rs.getString("u_email").equals(user.getEmail()) && !rs.getString("u_email").equals(dbEmail)) {
                System.out.println("Email found!");
                uniqueEmail = false;
                break;
            } else{
                System.out.println("Email not found!");
            }
        }

        if(uniqueEmail) {
            stmt.executeUpdate("UPDATE userr SET u_f_name = '" + user.getFirstName() + "', u_l_name = '" + user.getLastName() + "', u_address = '" + user.getAddress() + "', u_email = '" + user.getEmail() + "', u_phone_nr = '" + user.getPhoneNumber() + "', u_password = '" + user.getPassword() + "'  WHERE u_id = " + user.getUserId() + ";");
        } //else { System.out.println("Error message! Email is not unique!");}

        return uniqueEmail;
    }

    /**
     * @param u_id
     * @return
     * @throws SQLException
     * @author Sossio.
     */
    public String getUserDatabaseFirstName(String u_id) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");

        String firstName = null;
        ResultSet rs = stmt.executeQuery("select u_f_name from userr where u_id = " + u_id + ";");
        while(rs.next()) {
            firstName = rs.getString(("u_f_name"));
        }
        return firstName;
    }

    /**
     * @param u_id
     * @return
     * @throws SQLException
     * @author Sossio.
     */
    public String getUserDatabaseLastName(String u_id) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");

        String lastName = null;
        ResultSet rs = stmt.executeQuery("select u_l_name from userr where u_id = " + u_id + ";");
        while(rs.next()) {
            lastName = rs.getString(("u_l_name"));
        }
        return lastName;
    }

    /**
     * @param u_id
     * @return
     * @throws SQLException
     * @author Sossio.
     */
    public String getUserDatabaseAddress(String u_id) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");

        String address = null;
        ResultSet rs = stmt.executeQuery("select u_address from userr where u_id = " + u_id + ";");
        while(rs.next()) {
            address = rs.getString(("u_address"));
        }
        return address;
    }

    /**
     * @param u_id gets userId from user.
     * @return 'old' email.
     * @throws SQLException if any sql issue occurs.
     * @author Sossio.
     */
    public String getUserDatabaseEmail(String u_id) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");

        String email = null;
        ResultSet rs = stmt.executeQuery("select u_email from userr where u_id = " + u_id + ";");
        while(rs.next()) {
            email = rs.getString(("u_email"));
        }
        return email;
    }

    /**
     * @param u_id
     * @return
     * @throws SQLException
     * @author Sossio.
     */
    public String getUserDatabasePhoneNumber(String u_id) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");

        String phoneNumber = null;
        ResultSet rs = stmt.executeQuery("select u_phone_nr from userr where u_id = " + u_id + ";");
        while(rs.next()) {
            phoneNumber = rs.getString(("u_phone_nr"));
        }
        return phoneNumber;
    }

    /**
     * @param u_id
     * @return
     * @throws SQLException
     * @author Sossio.
     */
    public String getUserDatabasePassword(String u_id) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");

        String password = null;
        ResultSet rs = stmt.executeQuery("select u_password from userr where u_id = " + u_id + ";");
        while(rs.next()) {
            password = rs.getString(("u_password"));
        }
        return password;
    }

    /**
     * authenticate the USER with email and password
     * @param email
     * @param password
     * @return
     */
    public  User authenticationUser(String email, String password){
        User user = null;
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + email +"' and u_password = '"+ password+ "'");
            while (rs.next()){
                if (rs.getString("u_email").equals(email) && rs.getString("u_password").equals(password)){
                    user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"), rs.getBoolean("u_isAdmin"));
                    System.out.println("[Is User]");
                }
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Authenticate the ADMIN with email and password
     * @param email
     * @param password
     * @return
     */
    public  User authenticationAdmin(String email, String password){
        User user = null;
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + email +"' and u_password = '"+ password+ "'");
            while (rs.next()){
                if (rs.getString("u_email").equals(email) && rs.getString("u_password").equals(password) && rs.getBoolean("u_isAdmin")){
                    user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"), rs.getBoolean("u_isAdmin"));
                    System.out.println("[Is User]");
                }
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    // ------------------------- SEARCH FLIGHTS ------------------------- //

    /**
     * @param departure
     * @param destination
     * @param date
     * @return
     */
    public ArrayList<Flight> searchFlight(String departure, String destination, String date, String dateR) {
        ArrayList<Flight> flights = new ArrayList<>();
        boolean rTur = false;
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            Statement stmt1 = con.createStatement();
            ResultSet flight = null;
            ResultSet flightReturn = null;
            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            if (date != null && dateR != null){
                System.out.println("not null date");
                flight = stmt.executeQuery("select * from flight where f_departure_name = '" + departure + "' and f_destination_name = '"+destination+"' and f_departure_date = '"+ date+"';");
                flightReturn = stmt1.executeQuery("select * from flight where f_departure_name = '" + destination + "' and f_destination_name = '"+departure+"' and f_departure_date = '"+ dateR+"';");
                rTur = true;
            }else if (date != null){
                flight = stmt.executeQuery("select * from flight where f_departure_name = '" + departure + "' and f_destination_name = '"+destination+"' and f_departure_date = '"+ date+"';");
                System.out.println("not null dateR");
            }
            else {
                System.out.println("no date provided");
                flight = stmt.executeQuery("select * from flight where f_departure_name = '" + departure + "' and f_destination_name = '"+destination+"';");
            }
            if (flight != null){
                while (flight.next()){
                    String id_get = flight.getString("f_id");
                    String departure_name_get = flight.getString(("f_departure_name"));
                    String departure_date_get = flight.getString("f_departure_date");
                    String departure_time_get = flight.getString("f_departure_time");
                    String destination_name_get = flight.getString("f_destination_name");
                    String destination_date_get = flight.getString("f_destination_date");
                    String destination_time_get = flight.getString("f_destination_time");
                    String price_get = flight.getString("f_price");
                    System.out.println("id: " + id_get + ", departure: " +departure_name_get+ ", destination: " + destination_name_get);
                    flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get, false));
                }
            }
            if (rTur && flightReturn != null){
                while (flightReturn.next()){
                    String id_get = flightReturn.getString("f_id");
                    String departure_name_get = flightReturn.getString(("f_departure_name"));
                    String departure_date_get = flightReturn.getString("f_departure_date");
                    String departure_time_get = flightReturn.getString("f_departure_time");
                    String destination_name_get = flightReturn.getString("f_destination_name");
                    String destination_date_get = flightReturn.getString("f_destination_date");
                    String destination_time_get = flightReturn.getString("f_destination_time");
                    String price_get = flightReturn.getString("f_price");
                    System.out.println("id: " + id_get + ", departure: " +departure_name_get+ ", destination: " + destination_name_get);
                    flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get, true));
                }
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    /**
     * @param name
     * @return
     */
    public ArrayList<Flight> seachFlightFromSearchField(String name) {
        ArrayList<Flight> flights = new ArrayList<>();

        try {
            String convert = name.toLowerCase();
            String searchTarget = convert.substring(0, 1).toUpperCase() + convert.substring(1); // convert first character to Uppercase
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet flight = stmt.executeQuery("select * from flight where f_departure_name like '%" + searchTarget +"%';");
            while (flight.next()){
                String id_get = flight.getString("f_id");
                String departure_name_get = flight.getString(("f_departure_name"));
                String departure_date_get = flight.getString("f_departure_date");
                String departure_time_get = flight.getString("f_departure_time");
                String destination_name_get = flight.getString("f_destination_name");
                String destination_date_get = flight.getString("f_destination_date");
                String destination_time_get = flight.getString("f_destination_time");
                String price_get = flight.getString("f_price");
                //System.out.println("Fetched info: \nid: " + id_get + "\nfrom: " + departure_name_get + "\ndestination: " + destination_name_get);
                flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get, false));
            }

            con.close();
            stmt.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    /**
     * @param u_id
     * @param flight_id
     * @param seatNbr
     * @param business
     * @return
     */
    public  boolean savePurchasedTicket(String u_id, String flight_id, String rfc, String date, String seatNbr, boolean business) {
        boolean saved = false;
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            int flight = stmt.executeUpdate("insert into booked values('" + u_id +"', '" +flight_id +"', '" +rfc +"', '" +date +"', '" +seatNbr +"');");
            while (flight != -1){
                System.out.println("Status: \n user: " + u_id + " has booked flight: " + flight_id);
                saved = true;
                //System.out.println("Fetched info: \nid: " + id_get + "\nfrom: " + departure_name_get + "\ndestination: " + destination_name_get);
                //Book booked = new Book(u_id, flight_id, seatNbr, business);
                break;
            }

            con.close();
            stmt.close();

        }catch (SQLException e){
            saved = false;
            e.printStackTrace();
        }
        return saved;
    }

    /**
     * @param user
     * @return
     * @throws SQLException
     */
    public  Image getProfilePicture(User user) throws SQLException {
        Image image = null;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        ResultSet result = stmt.executeQuery("select picture from profile_picture where u_id = " + user.getUserId() + ";");
        while (result.next()) {
            System.out.println(result.getString("picture"));
            image = new Image(result.getString("picture"));
        }
        return image;
    }

    /**
     * @param string
     * @param user
     * @throws SQLException
     */
    public  void setProfilePicture(String string, User user) throws SQLException {
        Image image = null;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE profile_picture SET picture = '" + string + "' WHERE u_id = " + user.getUserId() + ";");
    }

    /**
     * @return
     */
    public  ArrayList<Book> searchTicket() {
        ArrayList<Book> flights = new ArrayList<>();
        try {

            java.sql.Connection con = Connection.getDatabaseConnection();
            Statement stmt = con.createStatement();
            ResultSet flight;

            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            flight = stmt.executeQuery("select * from booked");

            while (flight.next()){
                String f_id = flight.getString("f_id");
                String u_id = flight.getString(("u_id"));
                String b_seat = flight.getString("b_seat");

                flights.add(new Book(f_id, u_id, b_seat, false));

            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    /**
     * @return
     */
    public  ArrayList<User> searchMember() {
        ArrayList<User> members = new ArrayList<>();
        try {

            java.sql.Connection con = Connection.getDatabaseConnection();
            Statement stmt = con.createStatement();
            ResultSet user;

            members.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            user = stmt.executeQuery("select * from userr where u_isadmin = 'false'");

            while (user.next()){

                String u_id = user.getString(("u_id"));
                String u_f_name = user.getString(("u_f_name"));
                String u_l_name = user.getString(("u_l_name"));
                String u_address = user.getString(("u_address"));
                String u_password = user.getString(("u_password"));
                String u_email = user.getString(("u_email"));
                String u_phone_nr = user.getString(("u_phone_nr"));
                boolean u_isAdmin = user.getBoolean(("u_isadmin"));


                members.add(new User(u_id, u_f_name, u_l_name,  u_address, u_email, u_phone_nr, u_password,u_isAdmin));

            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return members;
    }


    /**
     * @param userID
     * @return
     */
    //////// fyl table history ///////////
    public ArrayList<UserHistory> searchDataForTableHistory(int userID) {
        ArrayList<UserHistory> flights = new ArrayList<>();
        try {

            java.sql.Connection con = Connection.getDatabaseConnection();
            Statement stmt = con.createStatement();
            ResultSet flight;

            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            flight = stmt.executeQuery("select * from History where u_id = '"+ userID +"';");
            int i = 1;
            while (flight.next()){
                String compnay = flight.getString("p_company");
                String model = flight.getString("p_model");
                String referenceNo = flight.getString("b_rfc"); // most create a column in booked table for b_id/...
                int f_id = flight.getInt("f_id");
                String from = flight.getString("f_departure_name");
                String to = flight.getString("f_destination_name");
                String seat = flight.getString("b_seat");
                String date_purchased_ticket = flight.getString("b_date"); // temporary can be the destination date later it should be changed to real date from booked table
                double price = Double.parseDouble(flight.getString("f_price"));
                flights.add(new UserHistory(i, compnay, model, referenceNo, f_id, from, to, seat, date_purchased_ticket, price));
                i++;
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    /**
     * @param rfc_col_table_historik
     * @return
     */
    public boolean deleteHistoryByRFC(String rfc_col_table_historik) {
        boolean deleted = false;
        try {
            java.sql.Connection con = Connection.getDatabaseConnection();
            Statement stmt = con.createStatement();
            System.out.println("reference number to delete: " +rfc_col_table_historik);
            stmt.executeUpdate("SET search_path TO jetstream;");
            stmt.executeUpdate("delete from booked where b_rfc = '" + rfc_col_table_historik+"';");
            deleted = true;
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return deleted;
    }

    /**
     * @return
     */
    public static StringBuilder generateRandomRFC() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char ch = (char) (Math.random() * 26 + 'A');
            s.append(ch);
        }
        for (int i = 0; i < 9; i++) {
            char digit1 = (char) (Math.random() * 10 + '0');
            s.append(digit1);
        }
        System.out.println("Random vehicle plate number: " + s);
        return s;
    }

    // not used

    /**
     * To fetch seats number
     * @param id
     * @return
     */
    public  int[] getSeatNumber(String id) {
        int[] seats = new int[2];
        try {
            java.sql.Connection con = Connection.getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from seats where f_id = '"+ id+"'");
            while (rs.next()){
                int eco_seats = rs.getInt("p_seat_business");
                int bus_seats = rs.getInt("p_seat_economy");
                seats[0] = eco_seats;
                seats[1] = bus_seats;


            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return seats;
    }

    public ArrayList<String> getBookedSeats(String id) {
        ArrayList<String> seat = new ArrayList<>();
        try {
            java.sql.Connection con = Connection.getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from booked where f_id = '"+ id+"'");
            while (rs.next()){
                String seats = rs.getString("b_seat");
                seat.add(seats);
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return seat;
    }

}
