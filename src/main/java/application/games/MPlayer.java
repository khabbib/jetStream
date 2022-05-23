package application.games;

import application.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/***
 * Music Quiz class. Runs thread with music and keeps count of guesses etc.
 * @author Kasper.
 */
public class MPlayer extends Application implements Runnable {

    @FXML
    private ChoiceBox alternatives;
    @FXML Button closeButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ChoiceBox rounds;
    @FXML
    private Label titleLabel;

    private File song;
    private ArrayList alternativesList;
    private MediaPlayer mediaPlayer;
    private Controller controller;
    private boolean secondGame;
    private boolean waitGuess;
    private boolean newSong;
    private double maxRounds;
    private int correct;
    private double round;
    private Stage stage;
    private Parent root;
    private Thread thread;

    /***
     * Starts the application.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MPlayer.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Music Quiz");
        primaryStage.show();
        progressBar = (ProgressBar) root.lookup("#progressBar");
        initRounds();
    }

    /***
     * Starts the game.
     * @throws InterruptedException
     */
    public void startGame() throws InterruptedException {
        if (secondGame) {
            titleLabel.setText("Game already started.");
        } else {
            titleLabel.setText("Start guessing!");
            correct = 0;
            maxRounds = Integer.parseInt(rounds.getSelectionModel().getSelectedItem().toString());
            secondGame = true;
            game();
            thread = new Thread(this);
            thread.start();
        }
    }

    /***
     * Closes the application.
     * @param event
     */
    public void closeApplication(ActionEvent event){
        secondGame = false;
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /***
     * Keeps track of ongoing game.
     * @throws InterruptedException
     */
    private void game() throws InterruptedException {
        if (round == maxRounds) {
            reset();
            progressBar.setProgress(0.0);
            round = 0;
            titleLabel.setText("You got: " + correct + "/" + (int) maxRounds + " correct");
            secondGame = false;
        } else if (!newSong) {
            System.out.println(round);
            nextSong(randomSong());
            progressBar.setProgress((1 / maxRounds) * round);
            System.out.println((1 / maxRounds) * round);
            round++;
        }
    }

    /***
     * Adds rounds.
     */
    private void initRounds() {
        ArrayList roundsArray = new ArrayList();
        for (int i = 1; i < 11; i++) {
            roundsArray.add(i);
        }
        rounds = (ChoiceBox) root.lookup("#rounds");
        rounds.getItems().addAll(roundsArray);
        rounds.getSelectionModel().select(0);
    }

    /***
     * Resets after game is complete.
     */
    private void reset() {
        mediaPlayer.stop();
        song = null;
        alternatives.getItems().removeAll(alternativesList);
    }

    /***
     * Fills alternatives list.
     * @param correct
     */
    public void fillAlternatives(File correct) {
        alternativesList = new ArrayList();
        alternativesList.add(correct.getName().substring(0,correct.getName().length()-4));
        for (int i = 0; i < 4; i++) {
            String randomSong = randomSong().getName();
            randomSong = randomSong.substring(0,randomSong.length() - 4);
            while (alternativesList.contains(randomSong)) {
                randomSong = randomSong().getName();
            }
            alternativesList.add(randomSong);
        }

        Collections.shuffle(alternativesList);

        alternatives.getItems().addAll(alternativesList);
    }

    /***
     * Generates random song.
     * @return song file.
     */
    public File randomSong() {
        File[] files = new File("music").listFiles();
        Random rand = new Random();
        File song = files[rand.nextInt(files.length)];
        return song;
    }

    /***
     * Gets next song.
     * @param currentSong
     */
    public void nextSong(File currentSong) {
        song = currentSong;
        newSong = true;
        fillAlternatives(currentSong);
    }

    /***
     * Checks guess.
     * @throws InterruptedException
     */
    public void guess() throws InterruptedException {
        if (alternatives.getSelectionModel().getSelectedItem().toString().substring(0,alternatives.getSelectionModel().getSelectedItem().toString().length() - 4).equals(song.getName().substring(0,alternatives.getSelectionModel().getSelectedItem().toString().length() - 4))) {
            System.out.println(alternatives.getSelectionModel().getSelectedItem().toString().substring(0,alternatives.getSelectionModel().getSelectedItem().toString().length() - 4));
            System.out.println(song.getName().substring(0,alternatives.getSelectionModel().getSelectedItem().toString().length() - 4));
            titleLabel.setText("Correct answer!");
            correct++;
        } else {
            titleLabel.setText("Wrong answer!");
        }
        reset();
        game();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /***
     * Runs the music player.
     */
    @Override
    public void run() {
        while (secondGame) {
            if (newSong) {
                System.out.println("jieiowre");
                if (song != null) {
                    System.out.println(song.getName());
                    Media media = new Media(song.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    newSong = false;
                }
            }
            try {
                thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mediaPlayer.stop();
    }
}