package application.model;

import application.Controller;
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
    public World init(Controller controller) {
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
                .build(controller);

        return world;
    }
}
