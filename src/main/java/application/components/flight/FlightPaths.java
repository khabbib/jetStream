package application.components.flight;

import application.Controller;
import javafx.animation.*;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/***
 * Creates flight paths for dashboard world map.
 * @author Kasper.
 */
public class FlightPaths {

    private Controller controller;
    private List<PathTransition> list;
    private Pane pane;

    public FlightPaths(Controller controller) {
        this.controller = controller;
    }

    /***
     * Starts the process of creating flight paths.
     * @Author Kasper.
     */
    public void start(){
        resetPaths();
        pane = new Pane();
        createVariables();
        flightsInHistory();
    }

    /***
     * Setup for the appropriate javaFX/needed variables.
     */
    public void createVariables(){
        pane.setPickOnBounds(false);
        controller.world_map.getChildren().add(pane);
        pane.prefWidthProperty().bind(controller.world_map_scrollpane.widthProperty());
        pane.prefHeightProperty().bind(controller.world_map_scrollpane.heightProperty());
        list = new ArrayList<PathTransition>();
    }

    /***
     * Calculates appropriate duration as to synchronize speed for all flight paths.
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @return the amount of duration from a to b.
     * @Author Kasper.
     */
    public Duration calculateDuration(double x, double y, double x2, double y2){
        if (x >= x2){ x = x - x2; } else { x = x2 - x; }
        if (y >= y2){ y = y - y2; } else { y = y2 - y; }

        Duration duration = Duration.millis((x+y)*78);

        return duration;
    }

    /***
     * Fetches all selected flights from history list.
     * @Author Kasper.
     */
    public void flightsInHistory(){
        Coordinates coordinates = new Coordinates();
        for (int i = 0; i < controller.history_tableview.getItems().size(); i++) {
            System.out.println(controller.history_tableview.getColumns().get(10).getCellData(i));
            CheckBox checkBox = (CheckBox) controller.history_tableview.getColumns().get(10).getCellData(i);
            if (checkBox.isSelected()) {
                System.out.println("yeeaahash");
                createAnimation(coordinates.convert(controller.from_col_table_historik.getCellData(i)), coordinates.convert(controller.to_col_table_historik.getCellData(i)));
            }
        }
    }

    /***
     * Resets all flight paths.
     * @Author Kasper.
     */
    public void resetPaths() {
        if (list!=null) {
            for (int a = 0; a < list.size(); a++) {
                list.get(a).stop();
            }
            controller.world_map.getChildren().remove(pane);
        }
    }

    /***
     * Creates animations.
     * @param from starting ccountries.
     * @param to destination countries.
     * @Author Kasper.
     */
    public void createAnimation(double[] from, double[] to){
        Line line = createPaths(from, to);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(line.strokeDashOffsetProperty(), -3.7, Interpolator.LINEAR)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Image image = new Image("application/image/aeroplane.png");
        ImageView imagePlane = new ImageView(image);
        imagePlane.setFitWidth(10);
        imagePlane.setFitHeight(10);

        PathTransition pt = new PathTransition(calculateDuration(from[0],from[1],to[0],to[1]), line, imagePlane);
        pt.setDelay(Duration.seconds(1));
        pt.setCycleCount(Animation.INDEFINITE);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.play();

        pane.getChildren().add(line);
        pane.getChildren().add(imagePlane);
        list.add(pt);
    }

    /***
     * Creates the path between countries.
     * @param to destination countries.
     * @param from starting countries.
     * @return Line.
     */
    public Line createPaths(double[] to, double[] from) {
        if (to != null && from != null) {

            Circle departureCircle = new Circle(to[0], to[1], 2);
            Circle destinationCircle = new Circle(from[0], from[1], 2);
            departureCircle.setFill(Color.DARKORANGE);
            destinationCircle.setFill(Color.DARKORANGE);

            Line line = new Line(to[0], to[1],from[0], from[1]);
            line.getStrokeDashArray().addAll(2d);
            line.setStroke(Color.DARKORANGE);
            line.setOpacity(0.5);

            pane.getChildren().add(departureCircle);
            pane.getChildren().add(destinationCircle);

            return line;
        }
        return null;
    }

    /***
     * Gets pane.
     * @return Pane.
     * @Author Kasper.
     */
    public Pane getPane() {
        return pane;
    }
}
