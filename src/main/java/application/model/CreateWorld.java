package application.model;

import application.Controller;
import application.database.Connection;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import worldMapAPI.*;
import javafx.scene.paint.Color;

/**
 * This class creates world map to application were the user can search for flight trips.
 */
public class CreateWorld {
    static World world;
    private Controller controller;

    /**
     * Build the world
     * @param controller
     * @return
     */
    public World init(Controller controller, Connection connection) {
        this.controller = controller;
        world = WorldBuilder.create()
                .resolution(World.Resolution.HI_RES)
                .backgroundColor(Color.web("#0c0c1a"))
                //.fillColor(Color.web("#dcb36c"))
                //.strokeColor(Color.web("#987028"))
                //.hoverColor(Color.web("#fec47e"))
                //.pressedColor(Color.web("#6cee85"))
                //.locationColor(Color.web("#0000ff"))
                //.selectedColor(Color.MAGENTA)
                .zoomEnabled(true)
                .selectionEnabled(true)
                .build(controller, connection);

        return world;
    }


    public void addWorldInMap(ScrollPane scrollPane, User user) {
        StackPane stackPane = new StackPane(world);
        stackPane.setStyle("-fx-background-color: #0E0E1B;");
        scrollPane.setContent(stackPane);
        scrollPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        controller.u_name.setText(user.getFirstName());
        //controller.u_id.setText(user.getUserId());

    }
}
