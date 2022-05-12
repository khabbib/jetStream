package application.Components;

import javafx.event.ActionEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
public class MusicHandler implements Runnable {
    private MediaPlayer mediaPlayer;
    private Thread thread;
    private File[] files;
    private boolean playing = false;
    private int i = 0;
    public MusicHandler() {
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
            playing=true;
            thread = new Thread(this);
            thread.start();
        } else {
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
            mediaPlayer.play();
        } while (playing);
        mediaPlayer.pause();
    }
}
