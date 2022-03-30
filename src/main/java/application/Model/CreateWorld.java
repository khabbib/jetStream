package application.Model;

import eu.hansolo.fx.world.World;
import eu.hansolo.fx.world.WorldBuilder;

public class CreateWorld {
    static World world;
    // build the world
    public static  World init() {
        world = WorldBuilder.create()
                .resolution(World.Resolution.HI_RES)
                //.backgroundColor(Color.web("#4aa9d7"))
                //.fillColor(Color.web("#dcb36c"))
                //.strokeColor(Color.web("#987028"))
                //.hoverColor(Color.web("#fec47e"))
                //.pressedColor(Color.web("#6cee85"))
                //.locationColor(Color.web("#0000ff"))
                //.selectedColor(Color.MAGENTA)
                .zoomEnabled(true)
                .selectionEnabled(true)
                .build();

        return world;
    }
}
