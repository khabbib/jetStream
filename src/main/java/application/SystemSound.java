package application;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SystemSound implements Runnable {
    private MediaPlayer mediaPlayer;
    private Thread thread;
    private boolean playing = false;
    private Controller controller;

    public SystemSound(Controller controller) {
        this.controller = controller;
        thread = new Thread(this);
        mediaPlayer = new MediaPlayer(new Media(getClass().getResource("sounds/jetstream_intro_beat.mp3").toExternalForm()));
    }

    public void playButton() {
        playing=true;
        thread = new Thread(this);
        thread.start();
    }

    public void pauseButton() {
        playing = false;
    }

    @Override
    public void run() {
        do {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer = new MediaPlayer(new Media(getClass().getResource("sounds/jetstream_intro_beat.mp3").toExternalForm()));
                mediaPlayer.play();
            });
            mediaPlayer.play();
        } while (playing);
        mediaPlayer.pause();
    }
}
