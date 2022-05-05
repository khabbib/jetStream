package application.model;

import javafx.scene.control.CheckBox;

/**
 * This class saves data to a user.
 */
public class User {
    private int size = 1;
    private String firstName, lastName, address, email, phoneNumber, password, userId;
    private boolean isadmin;
    private CheckBox box;

    /**
     * @param userId
     * @param firstName
     * @param lastName
     * @param address
     * @param email
     * @param phoneNumber
     * @param password
     * @param isAdmin
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
    public int getSize(){return size;}
    public String getUserId() {
        return userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPassword() {
        return this.password;
    }
    public boolean isIsadmin() {
        return isadmin;
    }
    public CheckBox getBox(){
        return box;
    }


    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setAddress(String address) {this.address = address;}public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setPassword(String password) {this.password = password;}


    /**
     * @return full user information.
     */
    public String getToString(){
        return "info: " + "name: "+ firstName + " \n" +
                  "last name: " + lastName + " \n" +
                  "address: "+ address + " \n" +
                  "email: "+email + " \n"+
                  "number: "+ phoneNumber;
    }
}

