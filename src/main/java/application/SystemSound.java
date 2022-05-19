package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class runs on its thread to play a system sound when button clicked.
 * @author Sossio.
 */
public class SystemSound implements Runnable {
    private MediaPlayer mediaPlayer;
    private Thread thread;
    private boolean playing = false;
    private Controller controller;

    private String soundName;
    private String src;
    private boolean playSystemSound = false;

    /**
     * Class constructor
     * @param controller to connects methods and variables.
     */
    public SystemSound(Controller controller) {
        this.controller = controller;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * This method plays sound on button actions.
     * @param soundName takes the name of the sound as a string to print in console.
     * @param src is file path name.
     * @author Sossio.
     */
    public void playSystemSound(String soundName, String src) {

        thread = new Thread(this);
        thread.start();

        this.soundName = soundName;
        this.src = src;

        System.out.println("=== playSystemSound method!");

        playSystemSound = true;
    }

    @Override
    public void run() {

        System.out.println("=== Not true!");

        while(playSystemSound) {
            System.out.println("=== playSystemSound is true!");
            System.out.println(src);

            Media buzzer = new Media(getClass().getResource(src).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(buzzer);
            mediaPlayer.play();
            System.out.println("'" + soundName + "' fx played!");
            mediaPlayer.play();

            playSystemSound = false;
        }
    }
}
