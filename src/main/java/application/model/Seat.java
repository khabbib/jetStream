package application.model;

/**
 * This class saves data to seat.
 */
public class Seat {
    private String id;
    private boolean business;

    /**
     * @param id
     * @param business
     */
    public void Seat(String id, boolean business){
        this.id = id;
        this.business = business;
    }

    // ----- GETTER AND SETTERS ----- //
    public String getId(){
        return this.id;
    }
    public boolean getBusiness(){
        return this.business;
    }
}
