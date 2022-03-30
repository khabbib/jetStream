package application.Model;

public class User {
    private String name, lname, adress, email, number, password, id;

    // Constructor
    public User(String id,String name, String lname, String adress, String email, String number, String password){
        this.id = id;
        this.name = name;
        this.lname = lname;
        this.adress = adress;
        this.email = email;
        this.number = number;
        this.password = password;
    }





    // Getter and setter


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLname() {
        return lname;
    }

    public String getAdress() {
        return adress;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return this.password;
    }
    // info

    public String getToString(){
        return "info: " + "name: "+name + " \n" +
                  "last name: " +lname + " \n" +
                  "address: "+adress + " \n" +
                  "email: "+email + " \n"+
                  "number: "+number ;
    }
}

