package application.Model;

public class User {
    private String name, lname, adress, email, number;

    // Constructor
    public User(String name, String lname, String adress, String email, String number){
        this.name = name;
        this.lname = lname;
        this.adress = adress;
        this.email = email;
        this.number = number;
    }





    // Getter and setter

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

    // info
    public String getToString(){
        return "info: " + "name: "+name + " \n" +
                  "last name: " +lname + " \n" +
                  "address: "+adress + " \n" +
                  "email: "+email + " \n"+
                  "number: "+number ;
    }
}

