package application.model;

/**
 * #comment (comment this class and create javadoc to every method)
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
