package application;

import application.Model.*;
import application.Model.Pong;
import application.moveScreen.MoveScreen;
import application.databaseSQL.Db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import worldMap.World;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
    //<editor-fold desc="Variables" >

    // Default variables
    private CreateWorld createWorld;
    private World world;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private User user;

    // FXML variables
    @FXML private ButtonBar logout;
    @FXML private TextField login_pass;
    @FXML private ScrollPane scrollPane;
    @FXML private ScrollPane scrollFlights;
    @FXML private TextField login_email;
    @FXML private Label error;
    @FXML private Label registration_error;
    @FXML private Label success_msg;
    @FXML private Label u_name, u_id;
    @FXML private Label chosen_seat;
    @FXML private VBox display_flight;
    @FXML private Button menuButton1;
    @FXML private Button menuButton2;
    // From game
    @FXML private StackPane game1;
    @FXML private StackPane game2;
    @FXML private Button quizButton;

    // From registration page
    @FXML private TextField name, lname, adress, email, number, password;
    // From sit
    @FXML private TextField name_sit, lname_sit, fourdigit_sit, email_sit;
    @FXML private Label sitnbr_sit;
    @FXML private AnchorPane flight_sits_eco, flights_seats_business;
    @FXML private AnchorPane pnlSit;

    // sit
    private GridPane grid_left = new GridPane(); //Layout
    private GridPane grid_right = new GridPane(); //Layout
    private GridPane grid_business = new GridPane(); //Layout
    private AnchorPane pane = new AnchorPane();
    private HBox seatHbox;
    private Label newSeat = new Label();
    private Label label = new Label();      // Label
    private Label showSeat = new Label();

    private String returnSeat;
    private boolean typeSeat = false; // false = economy, true = business
    private int height = 600;
    private int width = 600;
    private int antalSeats;

    // toggle options
    @FXML private Button iconProfile, iconFlight, iconHistorik, iconGame, iconSuport, iconCloseSit;
    @FXML private AnchorPane pnlProfile, pnlHistorik, pnlFlight, pnlGame, pnlSupport;

    //Admin panels
    @FXML private AnchorPane pnlFlights, pnlTickets, pnlMember;
    @FXML private Button flightsBtn, membersBtn, ticketsBtn, logoutButton;
    //</editor-fold>

    //<editor-fold desc="flight list">
    private ArrayList<Flight> avalibleFlights = new ArrayList<>();
    @FXML private TextField from_input_flight,disc_input_flight;
    @FXML private DatePicker date_input_flight;
    @FXML private Label no_flight_aval_msg;
    //</editor-fold>


    //<editor-fold desc="search field">
    @FXML private TextField search_f_name;
    @FXML private ListView<String> searchListAprear, searchListAprear2, searchListAprear3;

    //</editor-fold>



    //////////   Home   ///////////
    // the method will switch the user to the Home page
    public void switchToHome(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //stage.initStyle(StageStyle.TRANSPARENT);
        //MoveScreen.moveScreen(root,stage);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }

    //////////   Play games   ///////////
    public void switchToGames (ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/games/Games.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("JetStream | Games");
        stage.setScene(scene);
        stage.show();

        menuButton1 = (Button) root.lookup("#menuButton1");
        quizButton = (Button) root.lookup("#quizButton");
        game1 = (StackPane) root.lookup("#game1");
        game2 = (StackPane) root.lookup("#game2");
        ImageView imageView = new ImageView(new Image("application/gamePosters/MusicQuiz.png"));
        ImageView imageView2 = new ImageView(new Image("application/gamePosters/PONG.png"));
        game1.getChildren().add(imageView);
        game2.getChildren().add(imageView2);
    }

    public void playPong(){
        Pong pong = new Pong();
        try {
            Stage primary = new Stage();
            pong.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playQuiz(){
        MPlayer mPlayer = new MPlayer();
        try {
           Stage primary = new Stage();
            mPlayer.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playPiano(){
        Piano piano = new Piano();
        try {
            Stage primary = new Stage();
            piano.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //////////   navigate to admin pages   ///////////
    public void switchToDashboard(ActionEvent e) throws IOException {
        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            User user = Db.authenticationUser(login_email.getText(), login_pass.getText());
            if (user != null) {
                renderDashboard(e, user);
            } else {
                error.setText("Wrong email or pass!");
            }
        } else {
            renderDashboard(e, user);
        }
    } // the method will switch the user to the dashboard page
    public void renderDashboard(ActionEvent e, User user) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Dashboard.fxml")));
        this.user = user;

        lookUpForEl(); // look up for elements in javaFX
        createWorld = new CreateWorld();
        world = createWorld.init(this);

        // sit window
        HBox hboxLR_seat = new HBox();
        hboxLR_seat.getChildren().addAll(grid_left);
        grid_left.setHgap(5);
        grid_left.setVgap(5);
        grid_business.setHgap(5);
        grid_business.setVgap(5);
        HBox hboxTLR_seat = new HBox();
        hboxTLR_seat.getChildren().add(grid_business);
        hboxTLR_seat.setAlignment(Pos.TOP_CENTER);
        flight_sits_eco.getChildren().add(hboxLR_seat);
        flights_seats_business.getChildren().add(hboxTLR_seat);
        //seatBox.getChildren().addAll(hboxLR_seat);



        // world map
        scrollPane.setContent(new StackPane(world));
        scrollPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        u_name.setText(user.getName());
        u_id.setText(user.getId());
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //MoveScreen.moveScreen(root,stage);
        stage.setTitle("JetStream | Dashboard");
        stage.setScene(scene);
        stage.show();
    } // the method will render dashboard page for user
    public void lookUpForEl(){
        u_name = (Label) root.lookup("#u_name");
        u_id = (Label) root.lookup("#u_id");
        name_sit = (TextField) root.lookup("#name_sit");
        lname_sit = (TextField) root.lookup("#lname_sit");
        searchListAprear = (ListView<String>) root.lookup("#searchListAprear");
        searchListAprear2 = (ListView<String>) root.lookup("#searchListAprear2");
        searchListAprear3 = (ListView<String>) root.lookup("#searchListAprear3");
        fourdigit_sit = (TextField) root.lookup("#fourdigit_sit");
        email_sit = (TextField) root.lookup("#email_sit");
        sitnbr_sit = (Label) root.lookup("#sitnbr_sit");
        flight_sits_eco = (AnchorPane) root.lookup("#flight_sits_eco");
        flights_seats_business = (AnchorPane) root.lookup("#flights_seats_business");
        pnlSit = (AnchorPane) root.lookup("#pnlSit");

        scrollFlights = (ScrollPane) root.lookup("#scrollFlights");
        scrollFlights.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        display_flight = (VBox) scrollFlights.getContent();
        scrollPane = (ScrollPane) root.lookup("#scrollPane");
        chosen_seat = (Label) root.lookup("#chosen_seat");
        menuButton2 = (Button) root.lookup("#menuButton2");
        search_f_name = (TextField) root.lookup("#search_f_name");

    }
    public void noLoginRequired(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Dashboard.fxml")));
        scrollFlights = (ScrollPane) root.lookup("#scrollFlights");
        scrollFlights.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pnlSit = (AnchorPane) root.lookup("#pnlSit");



        display_flight = (VBox) scrollFlights.getContent();
        scrollPane = (ScrollPane) root.lookup("#scrollPane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chosen_seat = (Label) root.lookup("#chosen_seat");
        createWorld = new CreateWorld();
        world = createWorld.init(this);

        scrollPane.setContent(new StackPane(world));
        scrollPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        u_name = (Label) root.lookup("#u_name");
        u_id = (Label) root.lookup("#u_id");
        u_name.setText(null);
        u_id.setText(null);
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        MoveScreen.moveScreen(root,stage);
        stage.setTitle("Test dashboard window");
        stage.setScene(scene);
        stage.show();
    }// shortcut login to user dashboard
    public void switchToChecking(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Checking.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Checking window");
        stage.setScene(scene);
        stage.show();
    }// the method will switch the user to the checking page
    public void switchToRegistration(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Registration.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Registration window");
        stage.setScene(scene);
        stage.show();
    }// the method will switch the user to the registration page
    public void registeruser(ActionEvent e) throws SQLException, IOException {
        user = new User(null, name.getText(), lname.getText(), adress.getText(), email.getText(), number.getText(), password.getText(), false);
        System.out.println(user.getName() + "fsdfsdfsdf");
        boolean ok = Db.saveUser(user);
        if (ok) {
            renderLoginPage(e, "successfully registered the user!");
        } else {
            registration_error.setText("Couldn't register the information");
        }
    }// the method will register the user and return to the login page
    public void switchToLogin(ActionEvent e) throws IOException {
        renderLoginPage(e, null);
    }// the method will switch the user to the login page
    public void renderLoginPage(ActionEvent e, String msg) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Login.fxml")));
        success_msg = (Label) root.lookup("#success_msg");
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login window");
        stage.setScene(scene);
        success_msg.setText(msg);
        stage.show();
    }// render login page


    //////////   flight lists dashboard   ///////////
    public void fillFlights (ArrayList <Flight> flights) {
        display_flight.getChildren().clear();
        Stage infoStage = new Stage();
        AtomicBoolean openedStage = new AtomicBoolean(false);
        ArrayList<Flight> compare = new ArrayList<>();

        if (flights != null) {

        for (int i = 0; i < flights.size();i++){

            StackPane stackholer = new StackPane();
            HBox hbox = new HBox(1);
            HBox hboxChildCenter = new HBox(1);
            HBox hboxChildRight = new HBox(1);

            Image img = new Image("/application/image/jetStream.png");
            ImageView image = new ImageView(img);
            image.setFitWidth(30);
            image.setFitHeight(40);

            // flight icons
            Image onboard = new Image("/application/image/onboard.png");
            ImageView onboardIcon = new ImageView(onboard);
            onboardIcon.setFitWidth(30);
            onboardIcon.setOpacity(0.5);
            onboardIcon.setFitHeight(30);

            Image path = new Image("/application/image/path.png");
            ImageView pathIcon = new ImageView(path);
            pathIcon.setFitWidth(70);
            pathIcon.setFitHeight(30);
            pathIcon.setStyle("-fx-margin: 0 40 0 40");

            Image landing = new Image("/application/image/landing.png");
            ImageView landingIcon = new ImageView(landing);
            landingIcon.setOpacity(0.5);
            landingIcon.setFitWidth(25);
            landingIcon.setFitHeight(25);

            Label titleF = new Label();
            titleF.setMaxSize(50,40);

            titleF.setText(flights.get(i).getDeparture_name());

            Text depTime = new Text();
            String tmp = flights.get(i).getDeparture_time();
            String[] sorted = tmp.split(":");
            String s = sorted[0] + ": " + sorted[1];
            depTime.setText(s);
            depTime.setStyle("-fx-font-weight: bold");
            Text desTime = new Text();
            desTime.setText(s); // calculate arriving time
            desTime.setStyle("-fx-font-weight: bold");


            Label titleD = new Label();
            titleD.setMaxSize(50,40);
            titleD.setText(flights.get(i).getDestination_name());

            Label date = new Label();
            date.setText(flights.get(i).getDestination_time());
            // box holderx
            VBox boardingBox = new VBox();
            boardingBox.setAlignment(Pos.CENTER_LEFT);
            boardingBox.getChildren().addAll(onboardIcon, depTime, titleF);
            // box holder
            VBox landingBox = new VBox();
            landingBox.setAlignment(Pos.CENTER_LEFT);
            landingBox.getChildren().addAll(landingIcon,desTime, titleD);

            Button btn = new Button("Köpa");
            btn.setStyle("-fx-background-color:  #ff8000; -fx-text-fill: #333; -fx-padding: 10 25; ");
            int finalI1 = i;
            btn.setOnAction(e -> {
                display_flight.getChildren().get(finalI1).setOpacity(0.8);
                for (int m = 0; m < display_flight.getChildren().size(); m++){
                    if (display_flight.getChildren().get(m) != display_flight.getChildren().get(finalI1)) {
                        display_flight.getChildren().get(m).setOpacity(1);
                    }
                }
                compare.add(flights.get(finalI1));
                chooseSeat(60, 9);
                pnlSit.toFront();

                /*HBox ls = new HBox();
                ls.getChildren().add(display_filght.getChildren().get(finalI1));
                hbox.setBackground(new Background(new BackgroundFill(Color.rgb(133, 200, 138),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
                hbox.setAlignment(Pos.TOP_CENTER);
                display_filght.getChildren().clear();
                display_filght.getChildren().add(ls);*/
            });

            hboxChildCenter.getChildren().addAll(boardingBox, pathIcon, landingBox);
            hboxChildCenter.setSpacing(15);
            hboxChildCenter.setAlignment(Pos.CENTER_LEFT);

            hboxChildRight.getChildren().add(btn);
            hboxChildRight.setAlignment(Pos.CENTER_RIGHT);

            /***************  main box to hold the list  *********************/
            hbox.setBackground(new Background(new BackgroundFill(Color.rgb(247, 245, 242), CornerRadii.EMPTY, Insets.EMPTY)));
            hbox.getChildren().addAll(hboxChildCenter, hboxChildRight);
            hbox.setHgrow(hboxChildCenter, Priority.ALWAYS);
            hbox.setPadding(new Insets(5));
            hbox.setEffect(new DropShadow(2.0, Color.BLACK));
            hbox.setAlignment(Pos.TOP_LEFT);
            hbox.setSpacing(30);

            stackholer.getChildren().add(hbox);
            stackholer.setAlignment(Pos.TOP_LEFT);
            display_flight.getChildren().addAll(stackholer); // the box
            display_flight.setAlignment(Pos.TOP_LEFT);

            hbox.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                hbox.setBackground(new Background(new BackgroundFill(Color.rgb(223, 223, 222), CornerRadii.EMPTY, Insets.EMPTY)));

            });
            hbox.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                hbox.setBackground(new Background(new BackgroundFill(Color.rgb(247, 245, 242), CornerRadii.EMPTY, Insets.EMPTY)));
            });
        }

        }else {
            System.out.println("flights list is null");
        }
    } // the method will show the flights list on the right side of the dashboard when a user choose a country


    //////////   sit lists    ///////////
    public void chooseSeat(int econonySeats, int businessSeats) {
        grid_left.getChildren().removeAll();
        grid_business.getChildren().removeAll();
        this.antalSeats = econonySeats;
        // 72/6 = 12
        // 12 row
        // 6 column
        boolean business = false;
        //
        for(int i = 0;i < econonySeats/10; i++){ // cal
            for(int j = 0;j <econonySeats/6; j++){ // row
                business = false;
                build_eco_seats(i,j, business);
            }
        }
        for(int i = 0;i < businessSeats/3; i++){ // cal
            for(int j = 0;j <businessSeats/3; j++){ // row
                business = true;
                build_eco_seats(i,j, business);
            }
        }

    }// the method will show the chosen sit on the screen
    public void build_eco_seats(int columnIndex, int rowIndex, boolean business) {
        Label label = new Label();
        label.setMinWidth(30);
        label.setMinHeight(30);
        label.setText(label.getId());
        label.setBackground(new Background(new BackgroundFill(Color.rgb(223, 223, 222), new CornerRadii(5), Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(Color.rgb(247, 245, 242), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        label.setId(rowIndex+ " " + columnIndex);
        //grid_left.setColumnIndex(label, columnIndex);
        if (business){
            System.out.println("business: " + business);
            grid_business.add(label, columnIndex,rowIndex);
        }

        else if(!business) {
            System.out.println("business: " + business);
            if (grid_left.getColumnCount() == 3 && grid_left.getRowCount() == 0){
                System.out.println("column 3");
                grid_left.add(label, columnIndex, rowIndex);
                grid_left.setMargin(label, new Insets(0, 0, 0, 20));
            } else if (grid_left.getColumnCount() == 4 && grid_left.getRowCount() > 0) {
            grid_left.add(label, columnIndex, rowIndex);
            }
            
            else {
                grid_left.setMargin(label, new Insets(0, 0, 0, 0));
                grid_left.add(label, columnIndex, rowIndex);
            }
        }


        //grid_left.getColumnCount();
        label.setOnMouseClicked(e ->{
            sitnbr_sit.setText(label.getId());
            // sit color change
            for (int i = 0; i < grid_left.getChildren().size(); i++){
                grid_left.getChildren().get(i).setOpacity(1);
                if (!Objects.equals(grid_left.getChildren().get(i).getId(), label.getId())){
                    grid_left.getChildren().get(i).setOpacity(0.5);
                }
            }
        });
    }



    //////////   navigate to admin pages   ///////////
    public void switchToAdminView(ActionEvent e) {

        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            try {
                User user = Db.authenticationAdmin(login_email.getText(), login_pass.getText());
                if (user != null) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin/AdminView.fxml")));
                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setTitle("Admin window");
                    stage.setScene(scene);
                    stage.show();
                } else {
                    error.setText("Wrong email or pass!");
                }
            }catch (IOException io){
                io.printStackTrace();
            }
        } else {
            error.setText("Fill the field!");
        }
    }





    //////////   getters and setters   ///////////
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }


    //////  DEV TEST  ///////
    public void testDev(ActionEvent e){
        if (e.getSource() == iconProfile) {
            pnlProfile.toFront();
        }
        else if(e.getSource() == iconCloseSit){
            pnlSit.toBack();
        }
        else if (e.getSource() == iconFlight) {
            pnlFlight.toFront();
        }
        else if (e.getSource() == iconHistorik) {
            pnlHistorik.toFront();
        }
        else if (e.getSource() == iconGame) {
            pnlGame.toFront();
        }
        else if(e.getSource() == iconSuport){
            pnlSupport.toFront();
        }

    }

    /////// ADMIN DEV ///////
    public void adminDev(ActionEvent e) throws IOException {
        if(e.getSource() == logoutButton)
        {
            switchToLogin(e);
        }

        else if(e.getSource() == flightsBtn)
        {
            pnlFlights.toFront();
        }

        else if(e.getSource() == ticketsBtn)
        {
            pnlTickets.toFront();
        }

        else if(e.getSource() == membersBtn)
        {
            pnlMember.toFront();
        }

    }

    //////  SEARCH FLIGHTS  ///////

    public void seachFlights(ActionEvent e) {
        LocalDate d = date_input_flight.getValue();
        System.out.println("Date: " +d);
        if (!(from_input_flight.getText().isEmpty()) && !(disc_input_flight.getText().isEmpty())){
            if (d != null) {
                avalibleFlights = Db.searchFlight(from_input_flight.getText(), disc_input_flight.getText(), String.valueOf(d));
            } else {
                avalibleFlights = Db.searchFlight(from_input_flight.getText(), disc_input_flight.getText());
            }
            if (avalibleFlights.isEmpty()){
                System.out.println("no flights available");
                fillFlights(null);
                //no_flight_aval_msg.setText("No flights available!");
            }else {
                //no_flight_aval_msg.setText("sf");
                fillFlights(avalibleFlights);
            }
        }
    }


    //////  SEARCH FIELD  ///////
    public void searchHit(){
        if (!search_f_name.getText().isEmpty()){
            avalibleFlights.clear();
            avalibleFlights = Db.seachFlightFromSearchField(search_f_name.getText());
            if (!avalibleFlights.isEmpty()){
                fillFlights(avalibleFlights);
            }else {
                JOptionPane.showMessageDialog(null, "No flight with: " + search_f_name.getText());
            }
        }else
            JOptionPane.showMessageDialog(null, "empty search field!");
    }
    public void searchAppear(){ // on key pressed search and show name
        if (search_f_name != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(search_f_name.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAprear != null){
                    searchListAprear.getItems().removeAll();
                }
                searchListAprear.setVisible(true);
                searchListAprear.setItems(searchAprear);
                searchListAprear.getSelectionModel().selectedItemProperty().addListener(e ->{
                    search_f_name.setText(searchListAprear.getSelectionModel().getSelectedItem());
                        searchListAprear.setVisible(false);
            });
            }
        }
    }
    public void departureNameAppear(){// on key pressed search and show name
        if (from_input_flight != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(from_input_flight.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAprear2 != null){
                    searchListAprear2.getItems().removeAll();
                }
                searchListAprear2.setVisible(true);
                searchListAprear2.setItems(searchAprear);
                searchListAprear2.getSelectionModel().selectedItemProperty().addListener(e ->{
                    from_input_flight.setText(searchListAprear2.getSelectionModel().getSelectedItem());
                        searchListAprear2.setVisible(false);
            });
            }
        }
    }
    public void destinationNameAppear(){// on key pressed search and show name
        if (disc_input_flight != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(disc_input_flight.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAprear3 != null){
                    searchListAprear3.getItems().removeAll();
                }
                searchListAprear3.setVisible(true);
                searchListAprear3.setItems(searchAprear);
                searchListAprear3.getSelectionModel().selectedItemProperty().addListener(e ->{
                    disc_input_flight.setText(searchListAprear3.getSelectionModel().getSelectedItem());
                        searchListAprear3.setVisible(false);
            });
            }
        }
    }
    private ArrayList<String> propareSearchTerm(String srch){
        ArrayList<String> obs;
        if (srch.length() > 1){
            String searchTarget = srch.substring(0, 1).toUpperCase() + srch.substring(1); // convert first character to Uppercase
            obs = compareSearchKey(searchTarget);
        }else {
            String searchTarget = srch.toUpperCase();
            obs = compareSearchKey(searchTarget);
        }
        return obs;
    }
    private ArrayList<String> compareSearchKey(String searchTargetKey) {
        ArrayList<String> obs  = new ArrayList<>();
        for (Enum item : Countrylist.values()) {
            if (item.toString().contains(searchTargetKey)){
                obs.add(item.toString());
                System.out.println(item + " /" + searchTargetKey);
            }
        }

        return obs;
    }



    //////  MENU  /////// none used methods
    public void openMenu() {
        logout.setLayoutX(0); // this will move in menu from outside the window
        System.out.println("Menu opened");
    } // the method will open the menu once the user clicked on his profile
    public void closeMenu() {
        logout.setLayoutX(-84); // this will move out the menu outside the window
        System.out.println("Menu closed");
    } // the method will close the menu
    public void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are about to Exit!");
        alert.setContentText("Do you really want to Exit?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scrollPane.getScene().getWindow();
            System.out.println("You have successfully exited!");
            stage.close();
        }
    } // the method will close a scene
    public void exitProgram(){
        System.exit(0);
    } // to determinate the program
}
