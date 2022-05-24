package application.components.user;

import application.Controller;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/***
 * Background music class. Runs thread with music for dashboard.
 * @author Kasper. Comented by Sossio.
 */
public class BgMusic implements Runnable {

    private MediaPlayer mediaPlayer;
    private Thread thread;
    private File[] files;
    private boolean playing = false;
    private Controller controller;
    private int i = 0;

    /**
     * Class constructor.
     * @param controller connects all methods and variables
     */
    public BgMusic(Controller controller) {
        this.controller = controller;
        files = new File("music").listFiles();
        thread = new Thread(this);
        mediaPlayer = new MediaPlayer(new Media(files[0].toURI().toString()));
    }

    /**
     * This method plays next music when button is clicked.
     */
    public void nextButton() {
        playing = false;
        mediaPlayer = new MediaPlayer(new Media(files[nextSong()].toURI().toString()));
        controller.play_button_image.setImage(new Image("application/image/media/pause.png"));
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * This method plays previous music when button is clicked.
     */
    public void prevButton() {
        playing = false;
        mediaPlayer = new MediaPlayer(new Media(files[prevSong()].toURI().toString()));
        controller.play_button_image.setImage(new Image("application/image/media/pause.png"));
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        playing = false;
    }

    /**
     * This method returs length of the music file to next music.
     */
    private int nextSong() {
        i = (i + 1) % files.length;
        return i;
    }

    /**
     * This method returs length of the music file to previous music.
     */
    private int prevSong() {
        i = (i + files.length - 1) % files.length;
        return i;
    }

    /**
     * This method plays a music when the button is clicked.
     */
    public void playButton() {
        if (!playing) {
            controller.play_button_image.setImage(new Image("application/image/media/pause.png"));
            playing=true;
            thread = new Thread(this);
            thread.start();
        } else {
            controller.play_button_image.setImage(new Image("application/image/media/play.png"));
            playing=false;
        }
    }

    /**
     * @param e is listening when a button key is pressed.
     */
    public void mediaHandler(ActionEvent e) {
        if (e.getSource().toString().contains("play")) {
            playButton();
        } else if (e.getSource().toString().contains("next")) {
            nextButton();
        } else if (e.getSource().toString().contains("prev")) {
            prevButton();
        }
        System.out.println(e.getSource());
    }

    /**
     * This method run its own thread to handle background music.
     */
    @Override
    public void run() {
        do {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer = new MediaPlayer(new Media(files[nextSong()].toURI().toString()));
                mediaPlayer.play();
            });
            mediaPlayer.play();
        } while (playing);
        mediaPlayer.pause();
    }
}
