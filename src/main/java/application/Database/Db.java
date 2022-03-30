package application.Database;

import application.Model.User;

import java.sql.*;

public class Db {
    public static boolean saveUser(User user) throws SQLException {
        boolean ok = false;
        if(user != null){
            Connection con = getDatabaseConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            stmt.executeUpdate("insert into userr(u_f_name, u_l_name, u_address, u_email, u_phone_nr, u_password) values('" + user.getName() + "' , '" + user.getLname() + "' , '" + user.getAdress()+ "' , '" + user.getAdress() +"' , '" + user.getEmail() + "', '" + user.getNumber() +"')");
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

}
