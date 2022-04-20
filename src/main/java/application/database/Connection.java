package application.database;

import application.model.Book;
import application.model.Flight;
import application.model.User;
import javafx.scene.image.Image;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 */
public class Connection {

    /**
     * get database connection
     * @return
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
     * register new user
     * @param
     * @return
     * @throws SQLException
     */
    public static boolean saveUser(String first_name_reg, String last_name_reg, String address_reg, String email_reg, String phone_number_reg, String password_reg, boolean isAdmin) throws SQLException {
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

    public static void updateUser(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_f_name = '" + user.getFirstName() + "', u_l_name = '" + user.getLastName() + "', u_address = '" + user.getAddress() + "', u_email = '" + user.getEmail() + "', u_phone_nr = '" + user.getPhoneNumber() + "', u_password = '" + user.getPassword() + "'  WHERE u_id = " + user.getUserId() + ";");
    }

    /**
     * authenticate the USER with email and password
     * @param email
     * @param password
     * @return
     */
    public static User authenticationUser(String email, String password){
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
     * authenticate the ADMIN with email and password
     * @param email
     * @param password
     * @return
     */
    public static User authenticationAdmin(String email, String password){
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

    /**
     * get the user with ID
     * @param user_id
     * @return
     */
    public static User getUserWithID(int user_id) {
        User user = null;
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from userr where u_id = '" + user_id +"'");
            while (rs.next()){
                user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"), rs.getBoolean("u_isAdmin"));
                System.out.println(rs.getString("u_password") + " from database");
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
     * @return
     */
    public static ArrayList<Flight> searchFlight(String departure, String destination) {
        ArrayList<Flight> flights = new ArrayList<>();
        try {

            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            ResultSet flight;

            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            flight = stmt.executeQuery("select * from flight where f_departure_name = '" + departure + "' and f_destination_name = '" + destination + "';");

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
                flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get));
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    /**
     * @param departure
     * @param destination
     * @param date
     * @return
     */
    public static ArrayList<Flight> searchFlight(String departure, String destination, String date) {
        ArrayList<Flight> flights = new ArrayList<>();
        try {

            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();

            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet flight = stmt.executeQuery("select * from flight where f_departure_name = '" + departure + "' and f_destination_name = '"+destination+"' and f_departure_date = '"+ date+"';");

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
                flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get));
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
    public static ArrayList<Flight> seachFlightFromSearchField(String name) {
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
                flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get));
            }

            con.close();
            stmt.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    // ------------------------- EXTRA THING ------------------------- //

    ///////// appear on screen when user type something in the search field

    /**
     * fetch and filter countries
     * @param name
     * @return
     */
    public static ArrayList<String> seachAppear(String name) {
        ArrayList<String> output = new ArrayList<>();
        try {
            String convert = name.toLowerCase();
            String searchTarget = convert.substring(0, 1).toUpperCase() + convert.substring(1); // convert first character to Uppercase
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            output.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet flight = stmt.executeQuery("select f_departure_name from flight where f_departure_name like '%" + searchTarget +"%';");
            while (flight.next()){
                if (output.size() >= 19){
                    break;
                }else {
                    String departure_name_get = flight.getString(("f_departure_name"));
                    output.add(departure_name_get);
                }
            }

            con.close();
            stmt.close();

        }catch (SQLException e){
            System.out.println("some problem accused");
        }
        return output;

    }

    /**
     * @return
     * @throws IOException
     */
    public static ArrayList<String> fetchLander() throws IOException {
        ArrayList<String> output = new ArrayList<>();
        FileWriter myWriter = new FileWriter("land.txt");
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select f_departure_name from flight");
            while (rs.next()){
                String name = rs.getString("f_departure_name");

                if (output.isEmpty()){
                    output.add(name);
                }else {
                    if (output.contains(name)){

                        System.out.println("har flera värde ");
                    }else {
                        output.add(name);
                        myWriter.write(name + ",\n");
                    }
                }

            }
            con.close();
            stmt.close();
            myWriter.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return output;
    }

    /**
     * @param u_id
     * @param flight_id
     * @param seatNbr
     * @param business
     * @return
     */
    public static boolean savePurchasedTicket(String u_id, String flight_id, String seatNbr, boolean business) {
        boolean saved = false;
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            int flight = stmt.executeUpdate("insert into booked values('" + u_id +"', '" +flight_id +"', '" +seatNbr +"');");
            while (flight != -1){
                System.out.println("Status: \n user: " + u_id + " has booked flight: " + flight_id);
                saved = true;
                //System.out.println("Fetched info: \nid: " + id_get + "\nfrom: " + departure_name_get + "\ndestination: " + destination_name_get);
                Book booked = new Book(u_id, flight_id, seatNbr, business);
                break;
            }

            con.close();
            stmt.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return saved;
    }

    public static Image getProfilePicture(User user) throws SQLException {
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

    public static void setProfilePicture(String string, User user) throws SQLException {
        Image image = null;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE profile_picture SET picture = '" + string + "' WHERE u_id = " + user.getUserId() + ";");

    }

    public static ArrayList<Book> searchTicket() {
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

    public static ArrayList<User> searchMember() {
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


}
