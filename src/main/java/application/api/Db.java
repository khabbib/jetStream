package application.api;

import application.Controller;
import application.components.reservation.Book;
import application.components.flight.Flight;
import application.components.user.User;
import application.components.ticket.UserHistory;
import javafx.scene.image.Image;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class connects java to pgadmin to claim data from database.
 */
public class Db {
    private Controller controller;
    public Db(Controller controller) {
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
     * Main point is to encrypt and save in db.
     * To compare password, same algorithm is provided to use, because it's not possible to decrypt it --> main point of hash password.
     * @param password is taken as a plain-text password.
     * @return encrypted password using MD5.
     * @author Sossio.
     */
    public String hashPassword(String password) {
        /* Plain-text password initialization. */
        String encryptedpassword = null;
        try{

            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(password.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < bytes.length ; i++) {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        /* Display the unencrypted and encrypted passwords. */
        //System.out.println("Plain-text password: " + password);
        //System.out.println("Encrypted password using MD5: " + encryptedpassword);

        return encryptedpassword;
    }

    /**
     * A test method. This method is used to check hashed password!
     * @param email  email
     * @param password password
     * @return true or false
     */
    public boolean hashAuthTest(String email, String password) {
        boolean ok = false;

        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from test_user_password_hash where email = '" + email +"' and pwd = '"+ hashPassword(password) + "'");

            while (rs.next()){
                //System.out.println(hashPassword(password));
                if (rs.getString("email").equals(email) && rs.getString("pwd").equals(hashPassword(password))) {
                    System.out.println("User registered!");
                    ok = true;
                } else {
                    System.out.println("Not registeed!");
                    //System.out.println(hashPassword(password));
                }
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return ok;
    }

    /**
     * Registers a new user.
     * @param
     * @return
     * @throws SQLException
     * @author Khabib. Developed by Sossio.
     */
    public boolean saveUser(String first_name_reg, String last_name_reg, String address_reg, String email_reg, String phone_number_reg, String password_reg, boolean isAdmin){
        boolean okToSaveUser = false;
        boolean notFoundEmail = true;
        System.out.println(isAdmin + " check admin arg");
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");

            ResultSet rsEmail = stmt.executeQuery("select u_email from userr");
            while(rsEmail.next()) {
                if(rsEmail.getString("u_email").equals(email_reg)) {
                    System.out.println("Email found!");
                    notFoundEmail = false;
                    break;
                } else{
                    System.out.println("Email not found!");
                }
            }

            if(notFoundEmail) {
                User user;
                stmt.executeUpdate("insert into userr(u_f_name, u_l_name, u_address, u_email, u_phone_nr, u_password, u_isAdmin) values('" + first_name_reg + "' , '" + last_name_reg + "' , '" + address_reg+ "' , '" + email_reg +"' , '" + phone_number_reg + "', '" + hashPassword(password_reg) +"', '" + isAdmin + "')");
                ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + email_reg +"'");
                while (rs.next()){
                    user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"), rs.getBoolean("u_isAdmin"), 0);
                    try {
                        setProfilePictureIdk("resources/application/image/user.png", user);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println();
                    okToSaveUser = true;
                }
            }

            con.close();
            stmt.close();
        }catch (Exception e){
            okToSaveUser = false;
        }
        return okToSaveUser;
    }

    /**
     * // Old one --> stmt.executeUpdate("UPDATE userr SET u_f_name = '" + user.getFirstName() + "', u_l_name = '" + user.getLastName() + "', u_address = '" + user.getAddress() + "', u_email = '" + user.getEmail() + "', u_phone_nr = '" + user.getPhoneNumber() + "', u_password = '" + user.getPassword() + "'  WHERE u_id = " + user.getUserId() + ";");
     * @param user takes as a parameter to edit user information.
     * @throws SQLException if any sql issue occurs.
     * @author Kasper. Developed by Sossio.
     */
    public void updateUserFirstName(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_f_name = '" + user.getFirstName() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }
    public void updateUserLastName(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_l_name = '" + user.getLastName() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }
    public void updateUserAddress(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_address = '" + user.getAddress() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }
    // ====== LET BE !
    public boolean updateUserEmail(User user, String dbEmail) throws SQLException {
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
            con.close();
        }

        if(uniqueEmail) {
            stmt.executeUpdate("UPDATE userr SET u_email = '" + user.getEmail() + "' where u_id = " + user.getUserId() + ";");
        } //else { System.out.println("Error message! Email is not unique!");}

        return uniqueEmail;
    }
    public void updateUserPhoneNumber(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_phone_nr = '" + user.getPhoneNumber() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }
    public void updateUserPassword(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_password = '" + hashPassword(user.getPassword()) + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }

    /**
     * authenticate the USER with email and password.
     * @param email
     * @param password
     * @return
     */
    public User authenticationUser(String email, String password){
        User user = null;
        try {
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + email +"' and u_password = '"+ hashPassword(password) + "'");
            while (rs.next()){
                if (rs.getString("u_email").equals(email) && rs.getString("u_password").equals(hashPassword(password))){
                    user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"), rs.getBoolean("u_isAdmin"), 0);
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
            ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + email +"' and u_password = '"+ hashPassword(password) + "'");
            while (rs.next()){
                if (rs.getString("u_email").equals(email) && rs.getString("u_password").equals(hashPassword(password)) && rs.getBoolean("u_isAdmin")){
                    user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"), rs.getBoolean("u_isAdmin"), 0);
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
     * @return
     * @throws SQLException
     * @autor Khabib and Obed.
     */
    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> userlist = new ArrayList<>();
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        int counter = 1;
        stmt.executeUpdate("SET search_path TO jetstream;");
        ResultSet rs = stmt.executeQuery("select * from userr;");
        while(rs.next()) {
            String id = rs.getString(("u_id"));
            String firstName = rs.getString(("u_f_name"));
            String lname = rs.getString(("u_l_name"));
            String address = rs.getString(("u_address"));
            String email = rs.getString(("u_email"));
            String phonenbr = rs.getString(("u_phone_nr"));
            boolean isAdmin = rs.getBoolean(("u_isadmin"));
            userlist.add(new User(id, firstName, lname, address,email,phonenbr,null,isAdmin, counter));
            counter++;

        }
        con.close();
        return userlist;
    }

        public ArrayList<Book> getAllTickets() throws SQLException {
            ArrayList<Book> ticketlist = new ArrayList<>();
            java.sql.Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            int counter = 1;
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from history;");
            while (rs.next()) {
                String id = rs.getString(("u_id"));
                String flight_id = rs.getString(("f_id"));
                String refNr = rs.getString(("b_rfc"));
                String date = rs.getString(("b_date"));
                boolean seats = rs.getBoolean(("b_seat"));
                ticketlist.add(new Book(id, flight_id, refNr, date, seats, counter));
                counter++;
            }
            con.close();
            return ticketlist;
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
        con.close();
        stmt.close();
        return firstName;
    }


    public String getUserDatabaseLastName(String u_id) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");

        String lastName = null;
        ResultSet rs = stmt.executeQuery("select u_l_name from userr where u_id = " + u_id + ";");
        while(rs.next()) {
            lastName = rs.getString(("u_l_name"));
        }
        con.close();
        stmt.close();
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
        con.close();
        stmt.close();
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
        con.close();
        stmt.close();
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
        con.close();
        stmt.close();
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
        con.close();
        stmt.close();
        return password;
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
            ResultSet flightTur = null;
            ResultSet flightReturn = null;
            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            if (date != null && dateR != null){
                System.out.println("not null date");
                flightTur = stmt.executeQuery("select * from flight where f_departure_name = '" + departure + "' and f_destination_name = '"+destination+"' and f_departure_date = '"+ date+"';");
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

            if (!rTur && flight != null){
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
                    flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get, false, 0, null));
                }
            }


            if (rTur && flightTur != null){
                while (flightTur.next()){
                    String id_get = flightTur.getString("f_id");
                    String departure_name_get = flightTur.getString(("f_departure_name"));
                    String departure_date_get = flightTur.getString("f_departure_date");
                    String departure_time_get = flightTur.getString("f_departure_time");
                    String destination_name_get = flightTur.getString("f_destination_name");
                    String destination_date_get = flightTur.getString("f_destination_date");
                    String destination_time_get = flightTur.getString("f_destination_time");
                    String price_get = flightTur.getString("f_price");
                    System.out.println("id: " + id_get + ", departure: " +departure_name_get+ ", destination: " + destination_name_get);
                    flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get, true, 0, null));
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
                    flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get, true, 0, null));
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
            String searchTarget = name;//convert.substring(0, 1).toUpperCase() + convert.substring(1); // convert first character to Uppercase
            System.out.println(searchTarget);
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
                flights.add(new Flight(id_get,departure_name_get,departure_date_get,departure_time_get, destination_name_get,destination_date_get,destination_time_get,price_get, false, 0, null));
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
    public Image getProfilePicture(User user) throws SQLException {
        Image image = null;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        ResultSet result = stmt.executeQuery("select picture from profile_picture where u_id = " + user.getUserId() + ";");
        while (result.next()) {
            System.out.println(result.getString("picture"));
            image = new Image(result.getString("picture"));
        }
        con.close();
        System.out.println("=== Gets profile picture!");
        return image;
    }

    /**
     * @param src
     * @param user
     * @throws SQLException
     */
    public void setProfilePictureIdk(String src, User user) throws SQLException {
        Image image = null;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE profile_picture SET picture = '" + src + "' WHERE u_id = " + user.getUserId() + ";");
        System.out.println("=== Sets default image to user!");
        con.close();
    }

    /**
     * @param pfpImageSrc
     * @param email
     * @throws SQLException
     */
    public void setProfilePicture(String pfpImageSrc, String email) throws SQLException {
        Image image = null;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        String userId = "select u_id from userr where u_email = '" + email + "'";
        stmt.executeUpdate("INSERT INTO profile_picture(u_id, picture) values((" + userId + "), '" + pfpImageSrc + "');");
        con.close();
    }


    public ArrayList<Flight> getAllFlights()
    {
        ArrayList<Flight> flights = new ArrayList<>();
        try {

            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            ResultSet flight;

            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            flight = stmt.executeQuery("select * from flight");
            int i = 1;
            while (flight.next()){
                String id_get = flight.getString("f_id");
                String departure_name_get = flight.getString(("f_departure_name"));
                String departure_date_get = flight.getString("f_departure_date");
                String departure_time_get = flight.getString("f_departure_time");
                String destination_name_get = flight.getString("f_destination_name");
                String destination_date_get = flight.getString("f_destination_date");
                String destination_time_get = flight.getString("f_destination_time");
                String price_get = flight.getString("f_price");
                String p_id = flight.getString("p_id");
                flights.add(new Flight(id_get, departure_name_get, departure_date_get,departure_time_get, destination_name_get, destination_date_get, destination_time_get, price_get, false, i, p_id));
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
     * @return
     */
    public ArrayList<Book> searchTicket() {
        ArrayList<Book> flights = new ArrayList<>();
        try {

            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            ResultSet flight;

            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            flight = stmt.executeQuery("select * from booked");

            while (flight.next()){
                String f_id = flight.getString("f_id");
                String u_id = flight.getString(("u_id"));
                String b_seat = flight.getString("b_seat");

                flights.add(new Book(f_id, u_id, b_seat, null, false, 0));

            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flights;
    }



    /**
     * @param userID
     * @return
     */
    //////// fyl table history ///////////
    public ArrayList<UserHistory> searchDataForTableHistory(int userID, String rfc, boolean isAdmin) {
        ArrayList<UserHistory> flights = new ArrayList<>();
        try {

            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            ResultSet flight;

            flights.clear();
            stmt.executeUpdate("SET search_path TO jetstream;");
            if(isAdmin)
            {
                flight = stmt.executeQuery("select * from History");
            }else if(rfc != null && userID == -1){
                flight = stmt.executeQuery("select * from History where b_rfc = '"+ rfc +"';");
            }
            else{
                flight = stmt.executeQuery("select * from History where u_id = '"+ userID +"';");
            }
            int i = 1;
            while (flight.next()){
                int userId = flight.getInt("u_id");
                String compnay = flight.getString("p_company");
                String model = flight.getString("p_model");
                String referenceNo = flight.getString("b_rfc"); // most create a column in booked table for b_id/...
                int f_id = flight.getInt("f_id");
                String from = flight.getString("f_departure_name");
                String dep_time = flight.getString("f_departure_time");
                String des_time = flight.getString("f_destination_time");
                String dep_date = flight.getString("f_departure_date");
                String des_date = flight.getString("f_destination_date");
                String to = flight.getString("f_destination_name");
                String seat = flight.getString("b_seat");
                String date_purchased_ticket = flight.getString("b_date"); // temporary can be the destination date later it should be changed to real date from booked table
                double price = Double.parseDouble(flight.getString("f_price"));
                boolean isChecked = flight.getBoolean("checkin");
                flights.add(new UserHistory(userId, i, compnay, model, referenceNo, f_id, from, to, seat, date_purchased_ticket, price, dep_time, des_time, dep_date, des_date, isChecked));
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
     * @param rfc
     * @return
     */
    public boolean deleteHistoryByRFC(String rfc) {
        boolean deleted = false;
        try {
            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            System.out.println("reference number to delete: " +rfc);
            stmt.executeUpdate("SET search_path TO jetstream;");
            stmt.executeUpdate("delete from booked where b_rfc = '" + rfc+"';");
            deleted = true;
            System.out.println("WEnet to this point!");
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return deleted;
    }


    /**
     * This metod deletes member at the column that has the request id
     * @param id_col_mbr_admin
     * @return
     * @author Obed
     */
    public boolean deleteMember(String id_col_mbr_admin) {
        boolean deleted = false;
        try {
            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            System.out.println("UserID to delete: " +id_col_mbr_admin);
            stmt.executeUpdate("SET search_path TO jetstream;");
            stmt.executeUpdate("delete from userr where u_id = '" + id_col_mbr_admin + "';");
            deleted = true;
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return deleted;
    }


    public boolean deleteFlight(String f_id) {
        boolean deleted = false;
        try {
            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            System.out.println("FlightID to delete: " + f_id);
            stmt.executeUpdate("SET search_path TO jetstream;");
            stmt.executeUpdate("delete from flight where f_id = '" + f_id + "';");
            deleted = true;
            con.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    public boolean deleteTicket(String b_rfc) {
        boolean deleted = false;
        try {
            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            System.out.println("Reference number to delete: " + b_rfc);
            stmt.executeUpdate("SET search_path TO jetstream;");
            stmt.executeUpdate("delete from booked where b_rfc = '" + b_rfc + "';");
            deleted = true;
            con.close();
            stmt.close();
        } catch (SQLException e) {
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
            java.sql.Connection con = Db.getDatabaseConnection();
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

    /**
     * This method will fetch all seats that already booked in a particular flight.
     * @param id will be a reference to find the booked seats in database
     * @return it will return a list of String with already booked seats
     * @author Habib Mohammadi
     */
    public ArrayList<String> getBookedSeats(String id) {
        ArrayList<String> seat = new ArrayList<>();
        try {
            java.sql.Connection con = Db.getDatabaseConnection();
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

    /**
     * This method will be called when the used want to check in its ticket
     * @param rfc is a reference number to booked ticket which is unique.
     * @return will return a flag true or false to check if checking went threw or not.
     * @author Habib Mohammadi
     */
    public boolean checking(String rfc) {
        boolean checked = false;
        try {
            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            int rs = stmt.executeUpdate("update booked set checkin = true where b_rfc = '"+ rfc +"'");
            if (rs != -1){
                checked = true;
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            checked = false;
            e.printStackTrace();
        }
        return checked;
    }
}
