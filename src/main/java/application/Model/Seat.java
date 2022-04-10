package application.Model;

public class Seat {
    private String id;
    private boolean business;
    public void Seat(String id, boolean business){
        this.id = id;
        this.business = business;
    }


    

    public String getId(){
        return this.id;
    }

    public boolean getBusiness(){
        return this.business;
    }


}
