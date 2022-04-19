package application.model;

/**
 *
 */
public class User {
    private String firstName, lastName, address, email, phoneNumber, password, userId;
    private boolean isAdmin;

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
    public User(String userId, String firstName, String lastName, String address, String email, String phoneNumber, String password, boolean isAdmin){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // ----- GETTER AND SETTERS ----- //
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
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setAddress(String address) {this.address = address;}public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setPassword(String password) {this.password = password;}

    // ----- INFO ----- //
    public String getToString(){
        return "info: " + "name: "+ firstName + " \n" +
                  "last name: " + lastName + " \n" +
                  "address: "+ address + " \n" +
                  "email: "+email + " \n"+
                  "number: "+ phoneNumber;
    }
}

