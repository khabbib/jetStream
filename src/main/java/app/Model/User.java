package app.Model;

public class User {
    private Object message;

    // Constructor
    public User(Object msg){
        this.message = msg;
    }





    // Getter and setter
    public Object getMessage() {
        return message;
    }
    public void setMessage(Object message) {
        this.message = message;
    }

    // info
    public String getToString(){
        return "info: " + message + " \n";
    }
}

