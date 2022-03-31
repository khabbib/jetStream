package application.Model;

public class FlygResa {
    private String from, distination, date;

    public FlygResa(){}

    public FlygResa(String from, String distination, String date){
        this.from = from;
        this.distination = distination;
        this.date = date;
    }


    public String getFrom() {
        return from;
    }

    public String getDistination() {
        return distination;
    }

    public String getDate() {
        return date;
    }
}
