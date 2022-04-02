package application.Model;

public class FlygResa {
    private String from, distination, date, time;
    public FlygResa(String from, String distination, String date, String time){
        this.from = from;
        this.distination = distination;
        this.date = date;
        this.time = time;
    }


    public String getTime() {
        return time;
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
