package application.components.user;

import application.Controller;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * #comment (comment this class and create javadoc to every method)
 */
public class BgMusic implements Runnable {
    private MediaPlayer mediaPlayer;
    private Thread thread;
    private File[] files;
    private boolean playing = false;
    private Controller controller;
    private int i = 0;
    public BgMusic(Controller controller) {
        this.controller = controller;
        files = new File("music").listFiles();
        thread = new Thread(this);
        mediaPlayer = new MediaPlayer(new Media(files[0].toURI().toString()));
    }
    public void nextButton() {
        playing = false;
        mediaPlayer = new MediaPlayer(new Media(files[nextSong()].toURI().toString()));
        playing = true;
        thread = new Thread(this);
        thread.start();
    }
    public void prevButton() {
        playing = false;
        mediaPlayer = new MediaPlayer(new Media(files[prevSong()].toURI().toString()));
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    private int nextSong() {
        i = (i + 1) % files.length;
        return i;
    }

    private int prevSong() {
        i = (i + files.length - 1) % files.length;
        return i;
    }

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
