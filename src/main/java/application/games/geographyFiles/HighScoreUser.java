package application.games.geographyFiles;
/***
 * Creates Geography Quiz high scores for database storage.
 * @author Kasper.
 */
public class HighScoreUser {

    private int rank;
    private String user;
    private int score;
    private Long time;

    public HighScoreUser(int rank, String user, int score, Long time) {
        this.rank = rank;
        this.user = user;
        this.score = score;
        this.time = time;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
