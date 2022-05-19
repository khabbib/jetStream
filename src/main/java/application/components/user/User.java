package application.components.user;

import javafx.scene.control.CheckBox;

/**
 * This class saves data to a user which then is used to auth in database.
 */
public class User {
    private int size = 1;
    private String firstName, lastName, address, email, phoneNumber, password, userId;
    private boolean isadmin;
    private CheckBox box;

    /**
     * Constructor to User
     * @param userId user's id : string
     * @param firstName user's first name : string
     * @param lastName user's last name : string
     * @param address user's address : string
     * @param email user's email address : string
     * @param phoneNumber user's phone number : string
     * @param password user's password (Hashed one) : string
     * @param isAdmin user's status (Admin or not) : boolean
     * @param size user's counter : int
     */
    public User(String userId, String firstName, String lastName, String address, String email, String phoneNumber, String password, boolean isAdmin, int size){
        this.size = size;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isadmin = isAdmin;
        this.box = new CheckBox();
    }

    // ----- GETTER AND SETTERS ----- //
    public String getPhoneNumber() {return phoneNumber;}
    public String getPassword() {return this.password;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getAddress() {return address;}
    public String getUserId() {return userId;}
    public String getEmail() {return email;}
    public CheckBox getBox(){return box;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setPassword(String password) {this.password = password;}
    public void setAddress(String address) {this.address = address;}
    public void setEmail(String email) {this.email = email;}
    public int getSize(){return size;}
    public boolean isIsadmin() {return isadmin;}

    /**
     * @return full user information.
     * @author Habib.
     */
    public String getToString(){
        return "info: " + "name: "+ firstName + " \n" +
                  "last name: " + lastName + " \n" +
                  "address: "+ address + " \n" +
                  "email: "+email + " \n"+
                  "number: "+ phoneNumber;
    }
}

