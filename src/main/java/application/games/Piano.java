package application.games;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Objects;

public class Piano extends Application {
    private Parent root;
    private boolean[] keyDown = new boolean[250];
    private HashMap<Character, Integer> notes = new HashMap<Character, Integer>();
    @FXML private Slider instrumentSlider;

    Synthesizer syn;
    MidiChannel[] midChannel;
    Instrument[] instrument;
    int instNum = 88;
    int midiChannel = 7;

    @Override
    public void start(Stage primaryStage) throws Exception {

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("piano.fxml")));

        instrumentSlider = (Slider) root.lookup("#instrumentSlider");
        instrumentSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    setInstrument((int) instrumentSlider.getValue());
                    System.out.println((int) instrumentSlider.getValue());
                }
            }
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Synthesizer");
        primaryStage.show();

        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {

            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                Character x = event.getCode().getChar().charAt(0);
                if (notes.containsKey(x) && !keyDown[event.getCode().getCode()]){
                switch (x) {
                    case 'A':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'W':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'S':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'E':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'D':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'R':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'F':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'G':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'Y':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'H':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'U':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'J':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'K':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'O':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'L':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case 'P':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    case '.':
                        makeSound(notes.get(x), 550);
                        keyDown[event.getCode().getCode()] = true;
                        break;
                    }
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {

            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                Character x = event.getCode().getChar().charAt(0);
                if (notes.containsKey(x)) {
                    keyDown[event.getCode().getCode()] = false;

                    switch (x) {
                        case 'A':
                                stopSound(notes.get(x));
                                break;
                        case 'W':
                            stopSound(notes.get(x));
                            break;
                        case 'S':
                            stopSound(notes.get(x));
                            break;
                        case 'E':
                            stopSound(notes.get(x));
                            break;
                        case 'D':
                            stopSound(notes.get(x));
                            break;
                        case 'R':
                            stopSound(notes.get(x));
                            break;
                        case 'F':
                            stopSound(notes.get(x));
                            break;
                        case 'G':
                            stopSound(notes.get(x));
                            break;
                        case 'Y':
                            stopSound(notes.get(x));
                            break;
                        case 'H':
                            stopSound(notes.get(x));
                            break;
                        case 'U':
                            stopSound(notes.get(x));
                            break;
                        case 'J':
                            stopSound(notes.get(x));
                            break;
                        case 'K':
                            stopSound(notes.get(x));
                            break;
                        case 'O':
                            stopSound(notes.get(x));
                            break;
                        case 'L':
                            stopSound(notes.get(x));
                            break;
                        case 'P':
                            stopSound(notes.get(x));
                            break;
                        case '.':
                            stopSound(notes.get(x));
                            break;
                    }
                }
            }
        });

    }

    public Piano() {
        setKeys(notes);
        try {
            syn = MidiSystem.getSynthesizer();
            syn.open();
            midChannel = syn.getChannels();
            midChannel[midiChannel].programChange(instNum);
            instrument = syn.getAvailableInstruments();
            syn.loadInstrument(instrument[50]);
        } catch (MidiUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public void makeSound(int noteNumber, int velocity) {
        //Plays a note with specified note number
        this.midChannel[midiChannel].noteOn(noteNumber, velocity);
    }

    public void stopSound(int noteNumber) {
        //Stops specified note number
        this.midChannel[midiChannel].noteOff(noteNumber);
    }

    public void setKeys(HashMap<Character, Integer> map) {
        // Maps notes to keys
        map.put('A', 48);
        map.put('W', 49);
        map.put('S', 50);
        map.put('E', 51);
        map.put('D', 52);
        map.put('R', 53);
        map.put('F', 54);
        map.put('G', 55);   //middle C
        map.put('Y', 56);
        map.put('H', 57);
        map.put('U', 58);
        map.put('J', 59);
        map.put('K', 60);
        map.put('O', 61);
        map.put('L', 62);
        map.put('P', 63);
        map.put('.', 64);
    }

    public void setInstrument(int value) {
        midChannel[midiChannel].programChange(value);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
