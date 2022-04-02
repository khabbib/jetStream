

package eu.hansolo.fx.world;

import eu.hansolo.fx.world.World.Resolution;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;

import static eu.hansolo.fx.world.Country.*;
import static eu.hansolo.fx.world.Country.GB;
import static eu.hansolo.fx.world.Country.PL;

public class Main extends Application {
    private static final Random        RND = new Random();
    private              World         world;
    private              CountryRegion europeanUnion;


    @Override public void init() {


    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(world);
        pane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);
        //scene.getStylesheets().add(Main.class.getResource("custom-styles.css").toExternalForm());

        stage.setTitle("World Map");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
