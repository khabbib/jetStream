package application.api;

import application.components.flight.Flight;
import application.components.user.User;
import application.components.ticket.UserHistory;
import javafx.scene.image.Image;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;


/**
 * This class is head class to communicate with database. Important!
 */
public class Db {

    /**
     * This method gets the database connection.
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
     * This method registers a new user.
     * @param first_name_reg first name of user.
     * @param last_name_reg last name of user.
     * @param address_reg address.
     * @param email_reg email.
     * @param phone_number_reg phone number.
     * @param password_reg password (which then will be crypted).
     * @param isAdmin boolean (default is false).
     * @return boolean statement if its true or either false.
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
                        updateProfilePicture("resources/application/image/user.png", user);
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
     * This method updates user firstname in database.
     * @param user takes as a parameter to edit user information.
     * @throws SQLException if any sql issue occurs.
     * @author Sossio.
     */
    public void updateUserFirstName(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_f_name = '" + user.getFirstName() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }

    /**
     * This method updates user lastname in database.
     * @param user takes as a parameter to edit user information.
     * @throws SQLException if any sql issue occurs.
     * @author Sossio.
     */
    public void updateUserLastName(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_l_name = '" + user.getLastName() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }

    /**
     * This method updates user address in database.
     * @param user takes as a parameter to edit user information.
     * @throws SQLException if any sql issue occurs.
     * @author Sossio.
     */
    public void updateUserAddress(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_address = '" + user.getAddress() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }

    /**
     * This method updates user phone number in database.
     * @param user takes as a parameter to edit user information.
     * @throws SQLException if any sql issue occurs.
     * @author Sossio.
     */
    public void updateUserPhoneNumber(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_phone_nr = '" + user.getPhoneNumber() + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }

    /**
     * This method updates user password in database.
     * @param user takes as a parameter to edit user information.
     * @throws SQLException if any sql issue occurs.
     * @author Sossio.
     */
    public void updateUserPassword(User user) throws SQLException {
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE userr SET u_password = '" + hashPassword(user.getPassword()) + "' where u_id = " + user.getUserId() + ";");
        con.close();
        stmt.close();
    }

    /**
     * This method authenticate the USER with email and password.
     * @param email user email.
     * @param password user password.
     * @return user.
     * @author Khabib. Developed by Sossio.
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
     * This method authenticate the ADMIN with email and password.
     * @param email admin email.
     * @param password admin password.
     * @return user.
     * @author Sossio.
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
     * The method fetch a list of all users in database in admin page.
     * @throws SQLException Sql exception.
     * @return return a list of users.
     * @autor Obed.
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

    /**
     * This method gets user firstname to show in application user edit.
     * @param u_id is to bring current user data.
     * @return data of string.
     * @throws SQLException if any sql error occurs.
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

    /**
     * This method gets user lastname to show in application user edit.
     * @param u_id is to bring current user data.
     * @return data of string.
     * @throws SQLException if any sql error occurs.
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
        con.close();
        stmt.close();
        return lastName;
    }

    /**
     * This method gets user address to show in application user edit.
     * @param u_id is to bring current user data.
     * @return data of string.
     * @throws SQLException if any sql error occurs.
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
     * This method gets user phone number to show in application user edit.
     * @param u_id is to bring current user data.
     * @return data of string.
     * @throws SQLException if any sql error occurs.
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
     * This method gets user password to show in application user edit.
     * @param u_id is to bring current user data.
     * @return data of string.
     * @throws SQLException if any sql error occurs.
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
     * The method fetch flights and return a list of flights based on advanced search.
     * @param departure departure name.
     * @param destination destination name.
     * @param date tur date.
     * @param dateR return date.
     * @return list of flights.
     * @author Khabib.
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
     * The method fetch flights based on country name.
     * @param name country name.
     * @return a list of flights.
     * @author Khabib.
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
     * The method will save purchased ticket in database.
     * @param u_id user's id.
     * @param flight_id flight's id.
     * @param seatNbr seat number.
     * @param business is business?
     * @return return status of saved database.
     * @author Khabib.
     */
    public boolean savePurchasedTicket(String u_id, String flight_id, String rfc, String date, String seatNbr, boolean business) {
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
     * This method returns image of user from database when user/admin is logged in.
     * @param user to get user id.
     * @return image of user/admin.
     * @throws SQLException if any sql error occurs.
     * @author Kasper. Developed by Sossio.
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
     * This method updated users image when user wants to change it.
     * @param src path to image.
     * @param user user.
     * @throws SQLException if any sql error occurs.
     * @author Sossio.
     */
    public void updateProfilePicture(String src, User user) throws SQLException {
        Image image = null;
        java.sql.Connection con = getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("UPDATE profile_picture SET picture = '" + src + "' WHERE u_id = " + user.getUserId() + ";");
        System.out.println("=== Sets default image to user!");
        con.close();
    }

    /**
     * This method sets a default profile picture when a new user is registered.
     * @param pfpImageSrc path to image.
     * @param email which is unique.
     * @throws SQLException if any sql error occurs.
     * @author Sossio.
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

    /**
     * The method fetch all flights.
     * @return return a list of all flights that exist.
     * @author Obed.
     */
    public ArrayList<Flight> getAllFlights() {
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
     * The method fetch specific information about booked tickets for specific user.
     * @param userID user's id.
     * @return return a list of flights.
     * @author Khabib & Kasper.
     */
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
     * The method delete a specific booked ticket.
     * @param rfc reference number to ticket.
     * @return return status of delete.
     * @author Khabib.
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
     * This method deletes member at the column that has the request id.
     * @param id_col_mbr_admin
     * @return return status of delete.
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

    /**
     * The method will delete a flight based on flight's id by an Admin.
     * @param f_id flight's id.
     * @return return status of delete.
     * @author Obed.
     */
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

    /**
     * The method will delete a ticket based on reference number by an Admin.
     * @param b_rfc reference number.
     * @return return status of delete.
     * @author Obed.
     */
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
     * The method generate a random unique reference number for each ticket.
     * @return return value of generated RFC.
     * @author Khabib & Sossio.
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


    /**
     * The method fetch all seats available for a specific flight based on flight's id.
     * @param flightId flight's id.
     * @return return list of business and economy seats.
     * @author Khabib.
     */
    public int[] getSeatNumber(String flightId) {
        int[] seats = new int[2];
        try {
            java.sql.Connection con = Db.getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from seats where f_id = '"+ flightId+"'");
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
     * @author Habib
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
     * @author Habib
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
