package application.Database;

import application.Model.User;

import java.sql.*;

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
            stmt.executeUpdate("insert into userr(u_f_name, u_l_name, u_address, u_email, u_phone_nr, u_password) values('" + user.getName() + "' , '" + user.getLname() + "' , '" + user.getAdress()+ "' , '" + user.getEmail() +"' , '" + user.getNumber() + "', '" + user.getPassword() +"')");
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

    // authenticate the user with email and password
    public static User authenticationUser(String email, String password){
        User user = null;
        try {
            Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            ResultSet rs = stmt.executeQuery("select * from userr where u_email = '" + email +"' and u_password = '"+ password+ "'");
            while (rs.next()){
                if (rs.getString("u_email").equals(email) && rs.getString("u_password").equals(password)){
                    user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"));
                    System.out.println(user.getId() + " from db");
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
                user = new User(rs.getString("u_id"), rs.getString("u_l_name"), rs.getString("u_f_name"), rs.getString("u_address"), rs.getString("u_email"), rs.getString("u_phone_nr"), rs.getString("u_password"));
                System.out.println(rs.getString("u_password") + " from database");
            }
            con.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }



}
