package application.games;
/***
 * Stopwatch class. Keeps track of time for Geography Quiz game.
 * @author Kasper.
 */
public class StopWatch {
    private final long nanoSecondsPerMillisecond = 1000000;
    private final long nanoSecondsPerSecond = 1000000000;
    private final long nanoSecondsPerMinute = 60000000000L;
    private final long nanoSecondsPerHour = 3600000000000L;

    private long stopWatchStartTime = 0;
    private long stopWatchStopTime = 0;
    private boolean stopWatchRunning = false;

    /***
     * Starts the timer.
     */
    public void start() {
        this.stopWatchStartTime = System.nanoTime();
        this.stopWatchRunning = true;
    }

    /***
     * Stops the timer.
     */
    public void stop() {
        this.stopWatchStopTime = System.nanoTime();
        this.stopWatchRunning = false;
    }

    /***
     * Gets time in seconds.
     * @return seconds.
     */
    public long getElapsedSeconds() {
        long elapsedTime;

        if (stopWatchRunning)
            elapsedTime = (System.nanoTime() - stopWatchStartTime);
        else
            elapsedTime = (stopWatchStopTime - stopWatchStartTime);

        return elapsedTime / nanoSecondsPerSecond;
    }
}
