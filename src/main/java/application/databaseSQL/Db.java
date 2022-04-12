package application.databaseSQL;

import application.Model.Flight;
import application.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class Db {
    // get database connection
    public static Connection getDatabaseConnection() {

        String url = "jdbc:postgresql://pgserver.mau.se:5432/am2510";
        String user = "am2510";
        String password = "zyvl0ir7";

        Connection con = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // register new user
    public static boolean saveUser(User user) throws SQLException {
        boolean ok = false;
        if(user != null){
            Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            String bool = "false";
            stmt.executeUpdate("insert into userr(u_f_name, u_l_name, u_address, u_email, u_phone_nr, u_password, u_isAdmin) values('" + user.getName() + "' , '" + user.getLname() + "' , '" + user.getAdress()+ "' , '" + user.getEmail() +"' , '" + user.getNumber() + "', '" + user.getPassword() +"', '" + bool + "')");
            ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + user.getEmail() +"'");
            while (rs.next()){
                System.out.println("User saved not from db");
                System.out.println(rs);
            }
            ok= true;
            con.close();
            stmt.close();
        }
        return ok;
    }

    // authenticate the USER with email and password
    public static User authenticationUser(String email, String password){
        User user = null;
        try {
            Connection con = getDatabaseConnection();
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

    // authenticate the ADMIN with email and password
    public static User authenticationAdmin(String email, String password){
        User user = null;
        try {
            Connection con = getDatabaseConnection();
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

    // get the user with ID
    public static User getUserWithID(int user_id) {
        User user = null;
        try {
            Connection con = getDatabaseConnection();
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




    //////  SEARCH FLIGHTS  ///////

    public static ArrayList<Flight> seachFlight(String departure, String destination, String date) {
        ArrayList<Flight> flights = new ArrayList<>();
        try {

            Connection con = getDatabaseConnection();
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
                System.out.println("Fetched info: \nid: " + id_get + "\nfrom: " + departure_name_get + "\ndestination: " + destination_name_get);
                flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get));
            }

            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }


    public static ArrayList<Flight> seachFlightFromSearchField(String name) {
        ArrayList<Flight> flights = new ArrayList<>();

        try {
            String convert = name.toLowerCase();
            String searchTarget = convert.substring(0, 1).toUpperCase() + convert.substring(1); // convert first character to Uppercase
            Connection con = getDatabaseConnection();
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
                System.out.println("Fetched info: \nid: " + id_get + "\nfrom: " + departure_name_get + "\ndestination: " + destination_name_get);
                flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get));
            }

            con.close();
            stmt.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }

    ///////// apear on screen when user type something in the search field
    public static ArrayList<String> seachAppear(String name) {
        ArrayList<String> output = new ArrayList<>();
        try {
            String convert = name.toLowerCase();
            String searchTarget = convert.substring(0, 1).toUpperCase() + convert.substring(1); // convert first character to Uppercase
            Connection con = getDatabaseConnection();
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
}
