package application.games;

import application.api.Db;
import application.components.flight.CountryList;
import application.components.flight.CreateWorld;
import application.games.geographyFiles.GeographyCountries;
import application.games.geographyFiles.HighScoreUser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import worldMapAPI.World;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
/***
 * Geography Quiz class. Allows for game with guesses on world map etc.
 * @author Kasper.
 */
public class Geography extends Application {

    @FXML
    public Pane geography_pane;
    @FXML public ScrollPane world_scrollpane;
    @FXML public StackPane stackpane;
    @FXML public ImageView imageview;
    @FXML public Pane pane;
    @FXML public Pane round_pane;
    @FXML public Pane login_pane;
    @FXML public Pane country_pane;
    @FXML public Label correct_lbl;
    @FXML public Label round_lbl;
    @FXML public Label country_lbl;
    @FXML public TextField user_textfield;

    @FXML public TableView<HighScoreUser> ranking_table;
    @FXML public TableColumn<HighScoreUser, Integer> tc1;
    @FXML public TableColumn<HighScoreUser, String> tc2;
    @FXML public TableColumn<HighScoreUser, Integer> tc3;
    @FXML public TableColumn<HighScoreUser, Long> tc4;


    public World world;
    private boolean game = false;
    private String country_to_guess;
    public Parent root;
    private ArrayList<HighScoreUser> highScoreUsers;
    private int points;
    private int rounds;
    private long score;
    private String user;
    private Thread thread;
    private static Random random;
    private static List<GeographyCountries> countries;

    private StopWatch stopWatch;

    public static void main(String[] args) {
       launch(args);
    }

    /***
     * Starts application.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Geography.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Geography Quiz");
        primaryStage.show();
        fxmlInit();
        setUp();
    }

    /***
     * Stops time.
     */
    private void stopTime(){
        stopWatch.stop();
    }

    /***
     * Starts time.
     */
    private void startTime(){
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    /***
     * Creates and sets world.
     */
    private void setWorld(){
        CreateWorld createWorld = new CreateWorld();
        world = createWorld.init(this);
        stackpane = new StackPane(world);
        stackpane.setStyle("-fx-background-color: #112;");
        world_scrollpane.setContent(stackpane);
    }

    /***
     * Starts game.
     * @param actionEvent
     */
    public void startGame(ActionEvent actionEvent) {
        if (!game) {
            round_pane.setVisible(true);
            country_pane.setVisible(true);
            startTime();
            rounds = 0;
            points = 0;
            correct_lbl.setText("Correct: " + points + "/10");
            game = true;
            newCountry();
            setWorld();
            world_scrollpane.toFront();
            country_pane.toFront();
            System.out.println(game);
        }
    }

    /***
     * Checks if username is correct.
     */
    public void continueToGame() {
        System.out.println(user_textfield.getText());
        if (user_textfield.getText().length() <= 0 || user_textfield.getText().length() >= 12) {
            System.out.println("No input");
        } else {
            user = user_textfield.getText();
            login_pane.toBack();
        }
    }

    /***
     * Checks guess.
     * @param country_name
     */
    public void guess(String country_name) {
        System.out.println(country_name);
        System.out.println(game);
        rounds++;
        if (game) {
            System.out.println(country_name + " + " + country_to_guess);

            if (country_name == country_to_guess) {
                System.out.println("hell yeah");
                points++;
                correct_lbl.setText("Correct: " + points + "/10");
            }
            newCountry();
            checkGame();
        }

    }

    /***
     * Generates random country.
     */
    public void newCountry() {
        country_to_guess = countries.get(random.nextInt(countries.size())).toString();
        country_lbl.setText("Country: " + country_to_guess.replace("_"," "));
    }

    /***
     * Keeps track of games.
     */
    private void checkGame() {
        if (rounds == 10) {
            stopTime();
            score = (points * 10000) /(100+stopWatch.getElapsedSeconds());
            System.out.println("Score: " + score);
            try {
                sendScore();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            getHighScore();
            round_pane.setVisible(false);
            country_pane.setVisible(false);
            game = false;
            world_scrollpane.toBack();
        }
    }

    /***
     * Gets high score from DB.
     */
    public void getHighScore() {
        Connection c;
        highScoreUsers = new ArrayList<HighScoreUser>();
        try {
            c = Db.getDatabaseConnection();
            Statement stmt = c.createStatement();
            stmt.executeUpdate("SET search_path TO jetstream;");
            String SQL = "select * from canberra order by c_score desc;";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            int i = 1;
            while (rs.next()) {
                highScoreUsers.add(new HighScoreUser(i,rs.getString(1),rs.getInt(2),rs.getLong(4)));
                i++;
            }
            ObservableList<HighScoreUser> observableList = FXCollections.observableArrayList(highScoreUsers);
            ranking_table.setItems(observableList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /***
     * Sends score to DB.
     * @throws SQLException
     */
    public void sendScore() throws SQLException {
        Connection con = Db.getDatabaseConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET search_path TO jetstream;");
        stmt.executeUpdate("INSERT into canberra VALUES ('" + user + "'," + score + ",default," + stopWatch.getElapsedSeconds() + ");");
        con.close();
    }

    /***
     * Initializes FXML.
     */
    public void fxmlInit(){
        geography_pane = (Pane) root.lookup("#geography_pane");
        world_scrollpane = (ScrollPane) root.lookup("#world_scrollpane");
        stackpane = (StackPane) world_scrollpane.getContent();
        login_pane = (Pane) root.lookup("#login_pane");
        ranking_table = (TableView<HighScoreUser>) root.lookup("#ranking_table");
        imageview = (ImageView) root.lookup("#imageview");
        correct_lbl = (Label) root.lookup("#correct_lbl");
        round_lbl = (Label) root.lookup("#round_lbl");
        country_lbl = (Label) root.lookup("#country_lbl");
        round_pane = (Pane) root.lookup("#round_pane");
        country_pane = (Pane) root.lookup("#country_pane");
        user_textfield = (TextField) root.lookup("#user_textfield");
    }

    /***
     * Setup for variables.
     */
    public void setUp(){
        imageview.setImage(new Image("application/image/canberra/canberra.png"));
        tc1 = (TableColumn<HighScoreUser, Integer>) ranking_table.getColumns().get(0);
        tc1.setStyle("-fx-alignment: CENTER;");
        world_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        world_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        world_scrollpane.setFitToHeight(true);
        world_scrollpane.setFitToWidth(true);
        round_pane.setVisible(false);
        country_pane.setVisible(false);
        world_scrollpane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        world_scrollpane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");

        countries = Arrays.asList(GeographyCountries.values());
        random = new Random();

        ranking_table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("rank"));
        ranking_table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("user"));
        ranking_table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("time"));
        ranking_table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("score"));

        getHighScore();
    }

}
