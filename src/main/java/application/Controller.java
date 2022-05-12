package application;
import application.Components.*;
import application.Components.AdminComponents.AdminControl;
import application.Components.AdminComponents.RegisterAdmin;
import application.config.Config;
import application.games.Game2048Main;
import application.games.MPlayer;
import application.games.Piano;
import application.games.Pong;
import application.model.*;
import application.auth.Purchase;
import application.database.Connection;
import application.moveScreen.MoveScreen;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import worldMapAPI.World;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 */
public class Controller implements Initializable {

    //<editor-fold desc="GLOBAL VARIABLES" >
    // error / success message in user dashboard variable

    @FXML public Label user_notification_lbl;
    @FXML public Pane user_dashboard_msgbox_pane;
    // Default variables
    private Stage main_stage;
    private Scene main_scene;
    private Parent root;
    private CreateWorld create_world;
    public static World world_map;
    private static User user;
    private File profile_image_directory;
    private File[] profile_image_files;
    private static boolean is_editing_profile;

    // FXML variables
    @FXML private ButtonBar logout_button_bar;
    @FXML public ScrollPane world_map_scrollpane;
    @FXML public ScrollPane flights_scrollpane;
    @FXML private Label error_message_lbl;
    @FXML public Label success_msg_lbl;
    @FXML public Label username_lbl;
    @FXML public VBox flight_display_vbox;

    // ===== PROFILE EDITING =====
    // General
    @FXML public ImageView profile_image_imageview;
    @FXML public ImageView profile_image_preview_imageview;
    @FXML public TextField profile_first_name_lbl, profile_last_name_lbl, profile_email_lbl, profile_address_lbl, profile_phone_lbl;
    @FXML public Button profile_cancel_btn;
    @FXML public GridPane profile_profile_image_gridpane;
    @FXML public PasswordField profile_old_password_passwordfield;
    @FXML public TextField profile_new_password_textfield, profile_confirm_password_textfield;
    @FXML public Button profile_edit_btn;
    private static boolean is_editing_profile_image = false;

    // Issues
    @FXML public Label edit_pfp_fname_issue, edit_pfp_lname_issue, edit_pfp_address_issue, edit_pfp_email_issue, edit_pfp_phone_issue;
    @FXML public Label edit_pfp_old_pwd_issue, edit_pfp_new_pwd_issue, edit_pfp_new_c_pwd_issue;

    // Seat
    public final GridPane economy_seat_gridpane = new GridPane(); //Layout
    public final GridPane business_seat_gridpane = new GridPane(); //Layout

    // toggle options
    @FXML private Button menu_profile_btn, menu_flight_btn, menu_history_btn, menu_entertainment_btn, menu_support_btn, booking_close_btn, booking_close_second_page_btn;
    @FXML public AnchorPane profile_anchorpane, history_anchorpane, flight_anchorpane, entertainment_anchorpane, support_anchorpane;
    @FXML private Pane menu_highlight_color_flight, menu_highlight_color_history, menu_highlight_color_entertainment, menu_highlight_color_support;

   //</editor-fold>

    //<editor-fold desc="ADMIN VARIABLES">
    @FXML private ListView<String> ticket_listview, member_listview, flightListView;
    @FXML private AnchorPane admin_flights_anchorpane, admin_tickets_anchorpane, admin_members_anchorpane, admin_register_anchorpane;
    @FXML private Button admin_flights_button, admin_members_button, admin_tickets_button, admin_logout_button, registerCommitBtn_admin, registerMemberBtn_admin, returnToMemberListBtn_admin, refreshMembersBtn_admin, deleteMemberBtn_admin,
            search_input_flight_admin, prev_tur_date_flight_admin, next_tur_date_flight_admin;

    //</editor-fold>
    //<editor-fold desc="DASHBOARD VARIABLES">

    // purchase variables
    @FXML private AnchorPane payment_anchorpane;
    @FXML public TextField card_nbr;
    @FXML public TextField card_fname;
    @FXML public TextField card_lname;
    @FXML public TextField card_month;
    @FXML public TextField card_year;
    @FXML public TextField card_cvc;
    @FXML private Button card_prev_btn, card_purchase_btn, seat_next_btn;
    @FXML public Label card_counter_nbr;
    @FXML public Label payment_err_msg;
    @FXML private AnchorPane success_purchase_anchorpane;
    @FXML private Button success_purchase_to_dashboard_button, print_ticket_purchase_btn;

    // From seat
    @FXML public AnchorPane booking_seat_anchorpane;
    @FXML public ScrollPane business_seat_scrollpane;
    @FXML public ScrollPane eco_seat_scrollpane;
    @FXML public ImageView booking_profile_image;
    @FXML public AnchorPane booking_passenger_anchorpane;
    @FXML public TextField booking_first_name_textfield;
    @FXML public TextField booking_last_name_textfield;
    @FXML public TextField booking_four_digits_textfield;
    @FXML public TextField booking_email_textfield;
    @FXML public AnchorPane flight_seats_eco_anchorpane;
    @FXML public AnchorPane flights_seats_business_anchorpane;
    @FXML public Label booking_seat_number_lbl, booking_msg_lbl, booking_flight_number_lbl, booking_retur_seat_number_lbl,
            booking_price_lbl, booking_departure_lbl, booking_destination_lbl,
            booking_departure_extra_lbl, booking_destination_extra_lbl, booking_is_retur_lbl;
    @FXML public VBox booking_info_vbox;


    // menu images
    @FXML public ImageView map_menu_user_image;
    @FXML public ImageView history_menu_user_image;
    @FXML public ImageView entertainment_menu_user_image;
    @FXML public ImageView support_menu_user_image;

    //</editor-fold>
    //<editor-fold desc="SEAT VARIABLES"
    private ArrayList<String> taken_seat_economy = new ArrayList<>();
    private ArrayList<String> taken_seat_business = new ArrayList<>();
    private double seat_price = 0.0;
    private ArrayList<Flight> round_trip_flights = new ArrayList<>();
    private boolean has_return_flight = false;
    private String departure_seat, return_seat;

    //</editor-fold>
    //<editor-fold desc="HISTORY VARIABLES">
    ObservableList<UserHistory> history_items_list;
    ObservableList<UserHistory> history_flights;
    @FXML public TableView<UserHistory> history_tableview;
    @FXML public Label history_reference_number_lbl;
    @FXML private Button history_multiple_delete_button, history_single_delete_button, history_display_flight_path_button;
    @FXML private CheckBox history_select_all_checkbox;
    @FXML public TableColumn<Book, String> from_col_table_historik;
    @FXML public TableColumn<Book, String> to_col_table_historik;

    //</editor-fold
    //<editor-fold desc="SEARCH VARIABLES">
    public ArrayList<Flight> available_flights_list = new ArrayList<>();
    @FXML public ListView<String> search_list_appear;
    @FXML public ListView<String> search_list_appear_second;
    @FXML public ListView<String> search_list_appear_third;
    @FXML public Label nbr_of_available_flights, search_flight_error_lbl;
    @FXML public DatePicker date_input_flight,dateR_input_flight;
    @FXML public TextField from_input_flight_textfield;

    @FXML public HBox return_date_pick_hbox;
    


    @FXML public Button date_previous_day_button, date_next_day_button, date_previous_day_return_button, date_next_day_return_button;


    @FXML public CheckBox round_trip_checkbox;
    @FXML public TextField display_input_flights;
    @FXML public TextField search_f_name;

    //</editor-fold
    //<editor-fold desc="REGISTER VARIABLES">
    @FXML public Label registration_error_lbl;
    // Register a new user
    @FXML public TextField registration_first_name;
    @FXML public TextField registration_last_name;
    @FXML public TextField registration_address;
    @FXML public TextField registration_email;
    @FXML public TextField registration_phone_number;
    @FXML public TextField registration_password;
    @FXML public TextField registration_confirm_password;
    @FXML public Label name_issue_reg;
    @FXML public Label last_name_issue_reg;
    @FXML public Label address_issue_reg;
    @FXML public Label email_issue_reg;
    @FXML public Label phone_number_issue_reg;
    @FXML public Label password_issue_reg;
    @FXML public Label confirm_password_issue_reg;

    //Register user/admin in MemberPanel

    // Register a new user
    @FXML public CheckBox is_admin_checkbox;
    @FXML public TextField first_name_reg_admin;
    @FXML public TextField last_name_reg_admin;
    @FXML public TextField address_reg_admin;
    @FXML public TextField emailaddress_reg_admin;
    @FXML public TextField phone_number_reg_admin;
    @FXML public PasswordField password_reg_admin;
    @FXML public PasswordField confirm_password_reg_admin;
    @FXML public Label name_issue_reg_admin;
    @FXML public Label last_name_issue_reg_admin;
    @FXML public Label address_issue_reg_admin;
    @FXML public Label email_issue_reg_admin;
    @FXML public Label phone_number_issue_reg_admin;
    @FXML public Label password_issue_reg_admin;
    @FXML public Label confirm_password_issue_reg_admin;
    @FXML public Label registration_error_admin;
    //</editor-fold

    //<editor-fold desc="SUPPORT VARIABLES">
    @FXML public Button issue_btn_sup, feedback_btn_sup, contact_btn_sup, send_fb_btn_sup, send_issue_btn_sup, send_contact_btn_sup;
    @FXML public TextField subject_fb_txt_sup, email_fb_txt_sup, subject_contact_txt_sup, email_contact_txt_sup, title_issue_txt_sup, email_issue_txt_sup;
    //@FXML private TextFlow msgcontent_fb_txt_sup,msgcontent_contact_txt_sup,msgcontent_issue_txt_sup;
    @FXML public Label sup_report_display_msg, sup_contact_display_msg, sup_feedback_display_msg;
    @FXML public AnchorPane issue_panel_sup, contact_panel_sup, feedback_panel_sup;
    @FXML public TextArea msg_issue_txt_sup, msg_fb_txt_sup, msg_contact_txt_sup;
    //</editor-fold
    //<editor-fold desc="SERCH VARIABLES">
    //</editor-fold

    //<editor-fold desc="ADMIN TABLE VARIABLES">

    // ticket tabel variables
    public ObservableList<UserHistory> fetchedList_ticket_admin;
    public ObservableList<UserHistory> items_ticket_admin;
    @FXML public Button addTicketBtn_ticket_admin, deleteTicketBtn_ticket_admin, refreshTicketsBtn_ticket_admin;
    @FXML public TableView<UserHistory> table_tickets;
    @FXML public CheckBox select_col_ticket_admin;

    // members table variables
    public ObservableList<User> fetchedList_member_admin;
    public ObservableList<User> items_member_admin;
    @FXML public Button delet_btn_mbr_admin, deletS_btn_mbr_admin;
    @FXML public TableView<User> table_member_admin;
    @FXML public CheckBox select_col_mbr_admin;

    //flights table variables
    public ObservableList<Flight> fetchedList_flight_admin;
    public ObservableList<Flight> items_flight_admin;
    @FXML public Button delete_singelFlightBtn_admin, delete_allFlightsBtn_admin, refreshFlightsBtn_admin, addFlightsBtn_admin;
    @FXML public TableView<Flight> table_flight_admin;
    @FXML public CheckBox select_all_box_flight_admin;

    //</editor-fold>
    //<editor-fold desc"LOGIN VARIABLES">
    @FXML private CheckBox show_pasword_login;
    @FXML private Button forgot_password_login;
    @FXML private TextField login_pass;
    @FXML private TextField login_email;
    @FXML private TextField show_password_field_login;

    //</editor-fold>

    public Pane pane;
    // Edit profile
    @FXML public Label pfp_display_msg;

    //Weather
    @FXML public static Label lblForecastA,lblForecastB,lblForecastC,lblForecastD,lblForecastE,lblForecastF;
    @FXML public static ImageView weatherIcon;
    @FXML public static Pane weatherPane;
    @FXML public static Pane weatherPaneBase;
    public static boolean weatherMenu;

    //<editor-fold desc="instance initialize">
    application.Components.Support support;
    Search search;
    ConfirmActions confirmActions;
    DashboardController dashboardController;
    Connection connection;
    WeatherAPI weatherAPI;
    Config config;

    FlightPaths flightPaths;
    RegistrationUser registrationUser;
    RegisterAdmin registerAdmin;
    InitializeFXM initializeFXM;
    AdminControl adminControl;
    MusicHandler musicHandler;
    //</editor-fold>

    // Loader in login
    @FXML public ImageView login_loader_flight;

    //----------------- HOME -----------------//
    public Controller(){
        connection = new Connection(this);
        config = new Config(this, root, main_stage);
        support = new Support(this);
        confirmActions = new ConfirmActions(this);
        search = new Search(this, connection, confirmActions);
        registrationUser = new RegistrationUser(this, connection, config);
        registerAdmin = new RegisterAdmin(this, connection, config);
        dashboardController = new DashboardController(this, root, connection);
        initializeFXM = new InitializeFXM(this,connection);
        flightPaths = new FlightPaths(this);
        weatherAPI = new WeatherAPI();
        adminControl = new AdminControl(this, connection);
        musicHandler = new MusicHandler();
    }

    //----------------- HOME -----------------//

    /**
     * the method will switch the user to the Home page
     * @param e
     * @throws IOException
     */
    public void switchToHome(ActionEvent e) {
        config.render(e,"Home", "Home");
    }

    /***
     * Creates and animates flightpaths on world map.
     * Author: Kasper
     */
    public void displayFlightPaths() {
        flightPaths.start();
        flight_anchorpane.toFront();
    }

    public void forecast(String country) throws IOException, InterruptedException {
        ArrayList<String> arrayList = weatherAPI.weatherForecast(country);
        if (country.contains("United States")) { country = "USA"; }
        lblForecastA.setText(country);
        lblForecastB.setText("Weather: " + arrayList.get(1));
        lblForecastC.setText(arrayList.get(2) + "°C");
        lblForecastD.setText("Feels like: " + arrayList.get(3) + "°C");
        lblForecastE.setText("Minimum: " + arrayList.get(4) + "°C");
        lblForecastF.setText("Maxiumum: " + arrayList.get(5) + "°C");
        weatherIcon.setImage(new Image("http://openweathermap.org/img/wn/"+arrayList.get(0).replace(" ","")+"@2x.png"));
    }

    public void weatherButton() {
        if (!weatherMenu) {
            weatherPane.setVisible(true);
            weatherPane.setPickOnBounds(true);
            weatherMenu = true;
        } else {
            weatherPane.setVisible(false);
            weatherPane.setPickOnBounds(false);
            weatherMenu = false;
        }
    }


    /**
     * Login operation (show password )
     * @param e
     * @author Khabib.
     */
    public void showPassword(ActionEvent e){
        if (e.getSource() == show_pasword_login){
            login_pass.setVisible(true);
        }
    }

    /**
     * @author Khabib.
     */
    public void syncPasswordShow(){
        int maxLength = 15;
        if (show_pasword_login.isSelected()){
            show_password_field_login.setDisable(false);
            show_password_field_login.setOpacity(1);
            show_password_field_login.setText(login_pass.getText());
            System.out.println("Select");

        }else{
            show_password_field_login.setDisable(true);
            show_password_field_login.setText(null);
            show_password_field_login.setOpacity(0);
        }
        login_pass.textProperty().addListener(new ChangeListener<String>() {
            private boolean validating = false;
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!validating) {
                    validating = true;
                    String newText = newValue;
                    if (newText.length() > maxLength) {
                        newText = newText.substring(0, maxLength);
                    }
                    show_password_field_login.setText(newText);
                    login_pass.setText(newText);

                    validating = false;
                }
            }
        });
    }


    /**
     * Start minigames!
     * @author Kasper
     */
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
    public void play2048(){
        Game2048Main game2048Main = new Game2048Main();
        try {
            Stage primary = new Stage();
            game2048Main.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method plays sound on button actions.
     * @param soundName takes the name of the sound as a string to print in concole.
     * @param src is file path name.
     * @author Sossio. :D
     */
    public void playSoundLogin(String soundName, String src) {
        Media buzzer = new Media(getClass().getResource(src).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(buzzer);
        mediaPlayer.play();
        System.out.println(soundName + " fx played!");
    }

    /**
     * the method will switch the user to the dashboard page
     * navigate to dashboard pages
     * @param e
     * @throws IOException
     */
    public void switchToDashboard(ActionEvent e) throws IOException {
        if (!login_email.getText().isEmpty() || !login_pass.getText().isEmpty()) {
            if (login_email.getText().contains("@") && (login_email.getText().contains("gmail") || login_email.getText().contains("hotmail") || login_email.getText().contains("yahoo") || login_email.getText().contains("outlook"))) {
                User user = connection.authenticationUser(login_email.getText(), login_pass.getText());
                if (user != null) {
                    renderDashboard(e, user);
                    login_loader_flight.setVisible(true); // set loader to true
                    playSoundLogin("Login", "sounds/login.wav");
                    try {
                        profile_image_preview_imageview.setImage(connection.getProfilePicture(user));
                    } catch (SQLException ei) {
                        ei.printStackTrace();
                    }
                } else {
                    confirmActions.displayMessage(error_message_lbl, "Wrong email or password!", true);
                }
            } else {
                confirmActions.displayMessage(error_message_lbl, "Email has wrong format!", true);
            }
        } else {
            confirmActions.displayMessage(error_message_lbl, "Email or password is empty, please fill in fields!", true);
        }
    }

    /**
     * Resets the text in textfield from-to.
     * @author Sossio.
     */
    public void resetSearchFromTo() {
        from_input_flight_textfield.setText("");
        display_input_flights.setText("");
        search_list_appear_second.setVisible(false);
        search_list_appear_third.setVisible(false);
    }

    /**
     * Resets the text in textfield country name.
     * @author Sossio.
     */
    public void resetSearchCountry() {
        search_f_name.setText("");
        search_list_appear.setVisible(false);
    }

    /**
     * @param e
     * @param user
     * @throws IOException
     */
    public void renderDashboard(ActionEvent e, User user) {
        this.user = user;
        this.root = config.render(e,"user/Dashboard", "User Dashboard");
        dashboardController.userInitializeFXML(root, user);
        initializeFXM.initializeProfile(root, user);
        initializeFXM.initializeWeather(root);
        weatherPaneBase.setClip(new Rectangle(186, 334));
        create_world = new CreateWorld();
        world_map = create_world.init(this, connection);
        create_world.addWorldInMap(world_map_scrollpane, user);
        world_map_scrollpane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        world_map.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        //coordinates();
        setInfoIntoTableHistorik();
    } // the method will render dashboard page for user

    public void coordinates(){
        for (int i = 0; i < 20; i++) {
            Circle circle = new Circle(560,i*20,4);
            if (i%2 == 0) {circle.setFill(Color.RED);
            } else {circle.setFill(Color.GREEN);}
            world_map.getChildren().add(circle);
        }
    }

    /**
     * @param e
     * @throws IOException
     */
    public void noLoginRequired(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Dashboard.fxml")));
        dashboardController.userInitializeFXML(root, user);
        flights_scrollpane = (ScrollPane) root.lookup("#flights_scrollpane");
        flights_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        booking_seat_anchorpane = (AnchorPane) root.lookup("#booking_seat_anchorpane");

        flight_display_vbox = (VBox) flights_scrollpane.getContent();
        world_map_scrollpane = (ScrollPane) root.lookup("#world_map_scrollpane");
        world_map_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        world_map_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        create_world = new CreateWorld();
        world_map = create_world.init(this, connection);

        world_map_scrollpane.setContent(new StackPane(world_map));
        world_map_scrollpane.setBackground(new Background(new BackgroundFill(world_map.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        username_lbl = (Label) root.lookup("#username_lbl");
        username_lbl.setText(null);
        main_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        main_scene = new Scene(root);
        MoveScreen.moveScreen(root, main_stage);
        main_stage.setTitle("Test dashboard window");
        main_stage.setScene(main_scene);
        main_stage.show();
    }// shortcut login to user dashboard

    /**
     * @param e
     * @throws IOException
     */
    public void switchToRegistration(ActionEvent e) {
        this.root = config.render(e, "user/Registration", "Registration");
    }// the method will switch the user to the registration page

    /**
     * The method will register the user and return to the login page
     * @param e
     * @throws SQLException
     * @throws IOException
     */
    public void registerUser(ActionEvent e) {
        boolean ok = registrationUser.registerUser();
        System.out.println("Kommer in i if-satsen!");
        if (ok){
            root = config.render(e, "user/Login", "Login");
            success_msg_lbl = (Label) root.lookup("#success_msg_lbl");
            confirmActions.displayMessage(success_msg_lbl, "User successfully registered!", false);
            playSoundLogin("Success", "sounds/success.wav");
        } else {
            confirmActions.displayMessage(success_msg_lbl, "Error", true);
        }
    }

    /**
     * @param e
     * @throws SQLException
     */
    public void registerUserAdmin(ActionEvent e) throws SQLException {
        boolean ok = registerAdmin.registerUserAdmin(e);
        if (ok){
            adminControl.updateMemberTable();
            admin_members_anchorpane.toFront();
            confirmActions.displayMessage(registration_error_admin, "User successfully registered!", false);
            playSoundLogin("Success", "sounds/success.wav");
            first_name_reg_admin.setText("");
            last_name_reg_admin.setText("");
            first_name_reg_admin.setText("");
            address_reg_admin.setText("");
            emailaddress_reg_admin.setText("");
            phone_number_reg_admin.setText("");
            password_reg_admin.setText("");
            confirm_password_reg_admin.setText("");
        }
    }

    /**
     * the method will switch the user to the login page
     * @param e
     * @throws IOException
     */
    public void switchToLogin(ActionEvent e) {
        this.root = config.render(e, "user/Login", "Login");
        success_msg_lbl = (Label) root.lookup("#success_msg_lbl");
    }

    /**
     * flight lists dashboard
     * @param flights
     */
    public void fillFlights (ArrayList <Flight> flights) {
        boolean isFlight = checkFlightExistans(flights);
        if (isFlight){
            flight_display_vbox.getChildren().clear();
            flights_scrollpane.setVvalue(0);
            nbr_of_available_flights.setText(String.valueOf(flights.size()));
            try {
                booking_profile_image.setImage(connection.getProfilePicture(user));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } // update profile picture

            for (int i = 0; i < flights.size();i++){
                HBox hbox = createFlightsContent(flights, i);
                StackPane stackholer = new StackPane();
                stackholer.getChildren().add(hbox);
                stackholer.setAlignment(Pos.TOP_LEFT);
                flight_display_vbox.getChildren().addAll(hbox); // the box
                flight_display_vbox.setAlignment(Pos.TOP_LEFT);
                if (flights.get(i).isrTur()){
                    round_trip_flights.add(flights.get(i));
                }

                int finalI1 = i;
                // to click
                hbox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                   boolean ready = preperBeforeCreatingSeats();
                   if (ready){
                        if (flights.get(finalI1).isrTur()){ // chose two-way
                            System.out.println("A tur flight from event handler");
                            fillInfoSeatPnl(flights, finalI1);
                            createThisSeat(flights, finalI1);
                            if(!round_trip_flights.isEmpty()){
                                round_trip_flights.remove(finalI1); // remove one-way flight
                                has_return_flight = true; // set to true if there is more flight
                            }
                        }else { // chose one-way
                            fillInfoSeatPnl(flights, finalI1);
                            createThisSeat(flights, finalI1);
                        }
                        booking_seat_anchorpane.toFront();
                   }
                });
                // to hover
                hbox.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                    hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#112"), CornerRadii.EMPTY, Insets.EMPTY)));
                });
                hbox.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
                });
            }
        }else {
            nbr_of_available_flights.setText("0");
        }
    } // the method will show the flights list on the right side of the dashboard when a user choose a country

    private void createThisSeat(ArrayList<Flight> flights, int finalI1) {

        economy_seat_gridpane.getChildren().clear();
        business_seat_gridpane.getChildren().clear();

        try {
            int[] amountSeats = connection.getSeatNumber(flights.get(finalI1).getId());
            boolean buildSeatsSuccess = chooseSeat(amountSeats[0], amountSeats[1]);
            if(buildSeatsSuccess){
                ArrayList<String> bookedS = connection.getBookedSeats(flights.get(finalI1).getId());
                if (!bookedS.isEmpty()){
                    for (String seat : bookedS){
                        if (seat.contains("E")){
                            taken_seat_economy.add(seat);
                            showTakenS(seat, economy_seat_gridpane);
                        }
                        if(seat.contains("B")){
                            taken_seat_business.add(seat);
                            showTakenS(seat, business_seat_gridpane);
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private boolean preperBeforeCreatingSeats() {
        taken_seat_economy.clear();
        taken_seat_business.clear();

        flight_seats_eco_anchorpane.getChildren().clear();
        flights_seats_business_anchorpane.getChildren().clear();
        // Seat window
        economy_seat_gridpane.setHgap(5);
        economy_seat_gridpane.setVgap(5);
        business_seat_gridpane.setHgap(5);
        business_seat_gridpane.setVgap(5);
        HBox hboxLR_seat = new HBox();
        hboxLR_seat.getChildren().addAll(economy_seat_gridpane);
        HBox hboxTLR_seat = new HBox();
        hboxTLR_seat.getChildren().add(business_seat_gridpane);
        hboxTLR_seat.setAlignment(Pos.TOP_CENTER);
        flight_seats_eco_anchorpane.getChildren().add(hboxLR_seat);
        flights_seats_business_anchorpane.getChildren().add(hboxTLR_seat);
        return true;
    }



    // check if there is any flight for searched name
    private boolean checkFlightExistans(ArrayList<Flight> flights) {
        boolean flight = false;
        if (flights == null){
            Label lable = new Label("No flight available!");
            lable.setStyle("-fx-text-fill: #999; -fx-padding: 20px");
            if (!flight_display_vbox.getChildren().isEmpty()){
                flight_display_vbox.getChildren().clear();
                flight_display_vbox.getChildren().add(lable);
            }else {
                flight_display_vbox.getChildren().add(lable);
            }
        }else
            flight = true;

        return flight;
    }

    // create content of the flights list
    private HBox createFlightsContent(ArrayList<Flight> flights, int i) {
        HBox hbox = new HBox(1);

        VBox vBoxLeft = new VBox();
        VBox vBoxCenter = new VBox();
        VBox vBoxRight = new VBox();

        Font font = Font.font("Futura", FontWeight.BOLD, 12);
        Font font2 = Font.font("Futura", FontWeight.NORMAL, 10);

        Label departure = new Label("Departure:");
        departure.setPadding(new Insets(0,0,10,0));
        departure.setFont(font2);
        Label country = new Label(flights.get(i).getDeparture_name().replace("_"," "));
        if (country.getText().length() >= 15) {
            country.setWrapText(true);
            country.setMinHeight(35);
        }
        country.setFont(font);
        Label date = new Label(flights.get(i).getDeparture_time().substring(0,5) + " | " + flights.get(i).getDeparture_date());
        date.setFont(font2);
        vBoxLeft.getChildren().addAll(departure,country,date);

        Label destination = new Label("Destination:");
        destination.setPadding(new Insets(0,0,10,0));
        destination.setFont(font2);
        Label country2 = new Label(flights.get(i).getDestination_name().replace("_"," "));
        if (country2.getText().length() >= 15) {
            country2.setWrapText(true);
            country2.setMinHeight(35);
        }
        country2.setFont(font);
        Label date2 = new Label(flights.get(i).getDestination_time().substring(0,5) + " | " + flights.get(i).getDestination_date());
        date2.setFont(font2);
        vBoxCenter.getChildren().addAll(destination,country2,date2);

        Button button = new Button(flights.get(i).getPrice() + " SEK");

        button.setFont(font);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: #ff7000");
        vBoxRight.getChildren().add(button);


                /* // match tur and return flights

                // Change tur and return flights to a different color
                for (Flight flight : flights) {
                    if (flights.get(i).getDeparture_name().equals(flight.getDestination_name())) {
                        hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#fb3584"), CornerRadii.EMPTY, Insets.EMPTY)));
                    }else {
                        hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
                 */ // match tur and return flights

        vBoxLeft.setMaxWidth(105);
        vBoxLeft.setMinWidth(105);
        vBoxCenter.setMaxWidth(105);
        vBoxCenter.setMinWidth(105);

        hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
        hbox.getChildren().addAll(vBoxLeft,vBoxCenter,vBoxRight);
        hbox.setPadding(new Insets(5));
        hbox.setMargin(vBoxLeft,new Insets(10,0,10,10));
        hbox.setMargin(vBoxCenter,new Insets(10,0,10,0));
        hbox.setMargin(vBoxRight,new Insets(30,15,10,0));
        hbox.setEffect(new DropShadow(2.0, Color.BLACK));
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setSpacing(30);

        return hbox;
    }

    // fill information to seat pnl.
    private void fillInfoSeatPnl(ArrayList<Flight> flights, int finalI1) {
        booking_first_name_textfield.setText(user.getFirstName());
        booking_last_name_textfield.setText(user.getLastName());
        booking_email_textfield.setText(user.getEmail());
        booking_departure_lbl.setText(flights.get(finalI1).getDeparture_name());
        booking_destination_lbl.setText(flights.get(finalI1).getDestination_name());
        booking_departure_extra_lbl.setText(flights.get(finalI1).getDeparture_date());
        booking_destination_extra_lbl.setText(flights.get(finalI1).getDestination_date());
        booking_flight_number_lbl.setText(flights.get(finalI1).getId());
        seat_price = Double.parseDouble(flights.get(finalI1).getPrice());
        booking_is_retur_lbl.setText(String.valueOf(flights.get(finalI1).isrTur()));
        booking_price_lbl.setText(String.valueOf(seat_price));
        // profile picture

        // chosen flights color
        for (int g = 0; g < flight_display_vbox.getChildren().size(); g++){
            flight_display_vbox.getChildren().get(g).setOpacity(1);
        }
        flight_display_vbox.getChildren().get(finalI1).setOpacity(0.95);

    }

    /**
     * @param seats
     * @param grid
     */
    private void showTakenS(String seats, GridPane grid) {
        System.out.println(seats + " booked");
        for (int c = 0; c < grid.getChildren().size(); c++){
            if (seats.contains(grid.getChildren().get(c).getId())) {
                grid.getChildren().get(c).setDisable(true);
                grid.getChildren().get(c).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5; -fx-opacity: 1;");
                break;
            }
        }
    }

    /**
     * This method edits profile information to later save in the database.
     * @throws SQLException if any error occurs.
     * @author Kasper. Huge modified and developed by Sossio.
     */
    public void editProfile() throws SQLException {
        if (is_editing_profile == false) {
            profile_cancel_btn.setDisable(false);

            profile_first_name_lbl.setDisable(false);
            profile_last_name_lbl.setDisable(false);
            profile_address_lbl.setDisable(false);
            profile_email_lbl.setDisable(true); // Let be false cause an error will occur!
            profile_phone_lbl.setDisable(false);
            profile_old_password_passwordfield.setDisable(false);
            profile_new_password_textfield.setDisable(false);
            profile_confirm_password_textfield.setDisable(false);
            profile_edit_btn.setText("Confirm");
            is_editing_profile = true;
        } else {

            User editedUser = user;

            boolean successMessage = true;

            // Edit Firstname
            if (!profile_first_name_lbl.getText().isEmpty() && (profile_first_name_lbl.getText().length() >= 3 && profile_first_name_lbl.getText().length() <= 30)) {
                editedUser.setFirstName(profile_first_name_lbl.getText());

                System.out.println("Updating user firstname...");
                user = editedUser;

                connection.updateUserFirstName(user);

            } else {
                System.out.println("Firstname issue!");
                confirmActions.displayMessage(edit_pfp_fname_issue, "Size issue 3-30!", true);
                profile_first_name_lbl.setText(connection.getUserDatabaseFirstName(user.getUserId()));

                successMessage = false;
            }

            // Edit Lastname
            if(!profile_last_name_lbl.getText().isEmpty() && (profile_last_name_lbl.getText().length() >= 3 && profile_last_name_lbl.getText().length() <= 30)) {
                editedUser.setLastName(profile_last_name_lbl.getText());

                System.out.println("Updating lastname...");
                user = editedUser;

                connection.updateUserLastName(user);
            } else {
                System.out.println("Lastname issue!");
                confirmActions.displayMessage(edit_pfp_lname_issue, "Size issue 3-30!", true);
                profile_last_name_lbl.setText(connection.getUserDatabaseLastName(user.getUserId()));

                successMessage = false;
            }

            // Edit Address
            if (!profile_address_lbl.getText().isEmpty() && (profile_address_lbl.getText().length() >= 5 && profile_address_lbl.getText().length() <= 60)){
                editedUser.setAddress(profile_address_lbl.getText());

                System.out.println("Updating address...");
                user = editedUser;

                connection.updateUserAddress(user);
            } else {
                System.out.println("Address issue!");
                confirmActions.displayMessage(edit_pfp_address_issue, "Size issue 5-60!", true);
                profile_address_lbl.setText(connection.getUserDatabaseAddress(user.getUserId()));

                successMessage = false;
            }

            // Edit Email (LET BE!)
//            if (!profileEmail.getText().isEmpty() && (profileEmail.getText().length() >= 6 && profileEmail.getText().length() <= 30)) {
//                editedUser.setEmail(profileEmail.getText());
//
//                System.out.println("Updating user okay!");
//                user = editedUser;
//
//                boolean okToEditProfile = connection.updateUserPersonData(user, connection.getUserDatabaseEmail(user.getUserId()));
//                if(okToEditProfile) {
//                    confirmActions.displayMessage(pfp_display_msg, "Profile is updated!", false);
//                } else {
//                    confirmActions.displayMessage(edit_pfp_email_issue, "New email is taken!", true);
//                    profileEmail.setText(connection.getUserDatabaseEmail(user.getUserId()));
//                }
//
//            } else {
//                System.out.println("Email issue!");
//                confirmActions.displayMessage(edit_pfp_email_issue, "Size issue 6-30!", true);
//                profileEmail.setText(connection.getUserDatabaseEmail(user.getUserId()));
//            }

            // Edit Phone Number
            if (!profile_phone_lbl.getText().isEmpty() && profile_phone_lbl.getText().length() == 10){
                editedUser.setPhoneNumber(profile_phone_lbl.getText());

                System.out.println("Updating phone number...");
                user = editedUser;

                connection.updateUserPhoneNumber(user);
            } else {
                System.out.println("Phone number issue!");
                confirmActions.displayMessage(edit_pfp_phone_issue, "Size issue 10!", true);
                profile_phone_lbl.setText(connection.getUserDatabasePhoneNumber(user.getUserId()));

                successMessage = false;
            }

            // Edit Password (Special design)
            if (!profile_old_password_passwordfield.getText().isEmpty() || !profile_new_password_textfield.getText().isEmpty() || !profile_confirm_password_textfield.getText().isEmpty()){

                if(connection.hashPassword(profile_old_password_passwordfield.getText()).equals(connection.getUserDatabasePassword(user.getUserId()))) {

                    if(profile_new_password_textfield.getText().length() >= 8 && profile_new_password_textfield.getText().length() <= 20) {

                        if(profile_confirm_password_textfield.getText().equals(profile_new_password_textfield.getText())) {
                            editedUser.setPassword(profile_new_password_textfield.getText());

                            System.out.println("Updating new password...");
                            user = editedUser;

                            connection.updateUserPassword(user);
                        } else {
                            confirmActions.displayMessage(edit_pfp_new_c_pwd_issue, "Mach issue!", true);
                            successMessage = false;
                        }

                    } else {
                        confirmActions.displayMessage(edit_pfp_new_pwd_issue, "Size issue 8-20!", true);
                        successMessage = false;
                    }

                } else {
                    confirmActions.displayMessage(edit_pfp_old_pwd_issue, "Wrong password!", true);
                    successMessage = false;
                }

            } else {
                //System.out.println("To change pwd, fill all!");

                //confirmActions.displayMessage(edit_pfp_old_pwd_issue, "To change pwd, fill all!", false);
            }

            if(successMessage) {
                confirmActions.displayMessage(pfp_display_msg, "Profile is updated!", false);
            }

            // Reset password fields
            profile_old_password_passwordfield.setText("");
            profile_new_password_textfield.setText("");
            profile_confirm_password_textfield.setText("");

            // Make text fields disabled
            profile_first_name_lbl.setDisable(true);
            profile_last_name_lbl.setDisable(true);
            profile_address_lbl.setDisable(true);
            profile_email_lbl.setDisable(true);
            profile_phone_lbl.setDisable(true);
            profile_old_password_passwordfield.setDisable(true);
            profile_new_password_textfield.setDisable(true);
            profile_confirm_password_textfield.setDisable(true);
            is_editing_profile = false;
            profile_edit_btn.setText("Edit");
            profile_cancel_btn.setDisable(true);
        }
    }

    /**
     * This method cancels editing profile.
     * @throws SQLException if any error occurs.
     * @author Sossio.
     */
    public void editProfileCancel() throws SQLException {
        // Reset to current data
        profile_first_name_lbl.setText(connection.getUserDatabaseFirstName(user.getUserId()));
        profile_last_name_lbl.setText(connection.getUserDatabaseLastName(user.getUserId()));
        profile_address_lbl.setText(connection.getUserDatabaseAddress(user.getUserId()));
        //profileEmail.setText(connection.getUserDatabaseEmail(user.getUserId()));
        profile_phone_lbl.setText(connection.getUserDatabasePhoneNumber(user.getUserId()));

        // Reset password fields
        profile_old_password_passwordfield.setText("");
        profile_new_password_textfield.setText("");
        profile_confirm_password_textfield.setText("");

        // Make text fields disabled
        profile_first_name_lbl.setDisable(true);
        profile_last_name_lbl.setDisable(true);
        profile_address_lbl.setDisable(true);
        profile_email_lbl.setDisable(true);
        profile_phone_lbl.setDisable(true);
        profile_old_password_passwordfield.setDisable(true);
        profile_new_password_textfield.setDisable(true);
        profile_confirm_password_textfield.setDisable(true);
        is_editing_profile = false;
        profile_edit_btn.setText("Edit");
        profile_cancel_btn.setDisable(true);
        confirmActions.displayMessage(pfp_display_msg, "Profile editing canceled!", false);
    }

    /**
     * Shows available profile images using a grid.
     * @author Kasper.
     */
    public void changeImage() {
        if (is_editing_profile_image == false) {
            profile_profile_image_gridpane.setVisible(true);
            profile_image_directory = new File("src/main/resources/application/profiles/64x64");
            profile_image_files = profile_image_directory.listFiles();
            is_editing_profile_image = true;

            int b = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    profile_profile_image_gridpane.add(new ImageView(new Image(profile_image_files[b].getAbsolutePath())), i, j);
                    b++;
                }
            }
        } else {
            profile_profile_image_gridpane.setVisible(false);
            is_editing_profile_image = false;
        }
    }

    /**
     * Edits the profile picture of a user.
     * @param event
     * @author Kasper. Developed by Sossio.
     */
    public void clickGrid(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != profile_profile_image_gridpane) {
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            profile_image_directory = new File("src/main/resources/application/profiles/256x256");
            profile_image_files = profile_image_directory.listFiles();
            Image image = new Image(profile_image_files[(colIndex*5)+rowIndex].getAbsolutePath());
            profile_image_preview_imageview.setImage(image);
            profile_image_imageview.setImage(image);
            String profilePic = (profile_image_files[(colIndex*5)+rowIndex].getAbsolutePath());
            System.out.println(profilePic);
            profilePic = profilePic.substring(profilePic.indexOf("application") , profilePic.length());
            profilePic = profilePic.replace("\\","/");
            System.out.println(profilePic);

            try {
                connection.setProfilePictureIdk(profilePic, user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            is_editing_profile_image = false;
            profile_profile_image_gridpane.setVisible(false);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);

            confirmActions.displayMessage(pfp_display_msg, "Profile image is updated!", false);
        }
    }

    /**
     * create seats
     * @param econonySeats
     * @param businessSeats
     */
    public boolean chooseSeat(int econonySeats, int businessSeats) throws InterruptedException {
        economy_seat_gridpane.getChildren().removeAll();
        business_seat_gridpane.getChildren().removeAll();
        // 72/6 = 12
        // 12 row
        // 6 column
        //
        if (econonySeats%6 == 0){
            for(int row = 0;row < econonySeats/6; row++){ // cal
                for(int col = 0;col < 6; col++){ // row
                    build_eco_seats(row,col, false); // business is false for now
                }
            }
        }

        if (businessSeats%6 == 0){
            for(int row = 0;row < businessSeats/6; row++){ // cal
                for(int col = 0;col < 6; col++){ // row
                    build_eco_seats(row,col, true); // business is false for now
                }
            }
        }

        /*
        for(int i = 0;i < businessSeats/3; i++){ // cal
            for(int j = 0;j <businessSeats/3; j++){ // row
                business = true;
                build_eco_seats(i,j, business);
            }
        }

         */

        Instant start = Instant.now();
        Thread.sleep(1000);
        Instant end = Instant.now();
        System.out.println("timer: " + start + " end: " + end); // prints PT1M3.553S
        return true;
    }// the method will show the chosen seat on the screen

    /**
     * @param columnIndex
     * @param rowIndex
     * @param business
     */
    public void build_eco_seats(int rowIndex, int columnIndex, boolean business) {

        //grid_left.setColumnIndex(label, columnIndex);
        /*
        if (business){
            System.out.println("business: " + business);
            grid_business.add(label, columnIndex,rowIndex);

        }
            */
       if(!business) {
           //<editor-fold desc="short">
           Label label = createSeatItem();
           label.setId("E" + rowIndex+ columnIndex);
            if (economy_seat_gridpane.getColumnCount() == 2 && economy_seat_gridpane.getRowCount() == 1){
                economy_seat_gridpane.add(label, columnIndex, rowIndex);
                economy_seat_gridpane.setMargin(label, new Insets(0, 20, 0, 0));
            } else if (economy_seat_gridpane.getColumnCount() == 3 && economy_seat_gridpane.getRowCount() > 1) {
                System.out.println("column 4");
                economy_seat_gridpane.setMargin(label, new Insets(0, 0, 0, 20));
                economy_seat_gridpane.add(label, columnIndex, rowIndex);
            }
            else {
                economy_seat_gridpane.add(label, columnIndex, rowIndex);
            }
           //</editor-fold>
           //grid_left.getColumnCount();
           label.setOnMouseClicked(e ->{
               booking_price_lbl.setText(String.valueOf(seat_price));

               booking_seat_number_lbl.setText(label.getId());
               toggleSeatColor(); // restore seats
               // seat color change
               for (int i = 0; i < economy_seat_gridpane.getChildren().size(); i++){
                   economy_seat_gridpane.getChildren().get(i).setOpacity(1);
                   if (!Objects.equals(economy_seat_gridpane.getChildren().get(i).getId(), label.getId())){
                       economy_seat_gridpane.getChildren().get(i).setOpacity(0.5);
                       for (String taken : taken_seat_economy){
                           if (taken.equals(economy_seat_gridpane.getChildren().get(i).getId())){
                                economy_seat_gridpane.getChildren().get(i).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5;");
                           }
                       }
                   }
               }
           });
        }else {

           //<editor-fold desc="short">
           Label label = createSeatItem();
           label.setId("B" + rowIndex+ columnIndex);
           if (business_seat_gridpane.getColumnCount() == 2 && business_seat_gridpane.getRowCount() == 1){
               System.out.println("column 3");
               business_seat_gridpane.add(label, columnIndex, rowIndex);
               business_seat_gridpane.setMargin(label, new Insets(0, 20, 0, 0));
           } else if (business_seat_gridpane.getColumnCount() == 3 && business_seat_gridpane.getRowCount() > 1) {
               System.out.println("column 4");
               business_seat_gridpane.setMargin(label, new Insets(0, 0, 0, 20));
               business_seat_gridpane.add(label, columnIndex, rowIndex);
           }
           else {
               business_seat_gridpane.add(label, columnIndex, rowIndex);
           }
           //</editor-fold>
           label.setOnMouseClicked(e ->{

               booking_price_lbl.setText(String.valueOf(seat_price * 1.1));
               booking_seat_number_lbl.setText(label.getId());
               toggleSeatColor(); // restore seats


               // seat color change
               for (int i = 0; i < business_seat_gridpane.getChildren().size(); i++){
                   business_seat_gridpane.getChildren().get(i).setOpacity(1);
                   if (!Objects.equals(business_seat_gridpane.getChildren().get(i).getId(), label.getId())){
                       business_seat_gridpane.getChildren().get(i).setOpacity(0.5);
                       for (String taken : taken_seat_business){
                           if (taken.equals(business_seat_gridpane.getChildren().get(i).getId())){
                               System.out.println("business taken seats exist");
                               business_seat_gridpane.getChildren().get(i).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5;");
                           }
                       }
                   }
               }
           });
       }

    }

    /**
     *
     */
    private void toggleSeatColor() {
        for (int ge = 0; ge < economy_seat_gridpane.getChildren().size(); ge++){ // ge store for grid-economy
            if (!taken_seat_economy.contains(economy_seat_gridpane.getChildren().get(ge).getId())){
                economy_seat_gridpane.getChildren().get(ge).setOpacity(1);
                //gridE.getChildren().get(ge).setStyle("-fx-background-color: #AEFF47; -fx-background-radius: 5; -fx-opacity: 1;"); // restore all seats
            }
        }
        for (int gb = 0; gb < business_seat_gridpane.getChildren().size(); gb++){ // gb stor for grid-business
            if (!taken_seat_business.contains(business_seat_gridpane.getChildren().get(gb).getId())){
                business_seat_gridpane.getChildren().get(gb).setOpacity(1);
                //gridB.getChildren().get(gb).setStyle("-fx-background-color: #AEFF47; -fx-background-radius: 5; -fx-opacity: 1;"); // restore all seats
            }
        }
    }

    /**
     * @return
     */
    public Label createSeatItem(){
        Label label = new Label();
        label.setMinWidth(30);
        label.setMinHeight(30);
        label.setText(label.getId());
        label.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(0))));
        return label;
    }

    public void mediaHandler(ActionEvent e) {
        if (e.getSource().toString().contains("play")) {
            musicHandler.playButton();
        } else if (e.getSource().toString().contains("next")) {
            musicHandler.nextButton();
        } else if (e.getSource().toString().contains("prev")) {
            musicHandler.prevButton();
        }
        System.out.println(e.getSource());
    }

    /**
     * @param e
     * @author Khabib. Developed by Sossio.
     */
    public void purchaseHandle(ActionEvent e){
        if (e.getSource() == card_prev_btn){
            booking_passenger_anchorpane.toFront();
            payment_anchorpane.toBack();
        }else if(e.getSource() == card_purchase_btn){

            String nbr = card_nbr.getText();
            String name = card_fname.getText();
            String lname = card_lname.getText();
            String month = card_month.getText();
            String year = card_year.getText();
            String cvc = card_cvc.getText();

            if (!nbr.isEmpty()) {
                if (!name.isEmpty()) {
                    if (!lname.isEmpty()) {
                        if (!year.isEmpty()) {
                            if (!month.isEmpty()) {
                                if (!cvc.isEmpty()) {

                                    boolean validCard = Purchase.purchaseTicket(nbr, name, lname, month, year, cvc);
                                    if (validCard) {
                                        System.out.println("Card is valid!");
                                        boolean purchaseDone1 = false;
                                        boolean purchaseDone2 = false;
                                        String rfc1 = "", rfc2 = "";

                                        if (departure_seat != null) {
                                            for (int i = 0; i <= 1; i++) {
                                                System.out.println("Loop is running...");

                                                if (departure_seat != null && return_seat != null) { // saving tur flight
                                                    System.out.println("First condition");
                                                    StringBuilder rfc = connection.generateRandomRFC();
                                                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                                    boolean saveTicket = connection.savePurchasedTicket(user.getUserId(), return_seat, String.valueOf(rfc), date, departure_seat, false);
                                                    if (saveTicket) {
                                                        rfc1 = String.valueOf(rfc);
                                                        purchaseDone1 = true;
                                                    }
                                                    departure_seat = null;
                                                    return_seat = null;
                                                } else {
                                                    System.out.println("Second condition 2");
                                                    StringBuilder rfc = connection.generateRandomRFC();
                                                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                                    boolean saveTicket = connection.savePurchasedTicket(user.getUserId(), booking_flight_number_lbl.getText(), String.valueOf(rfc), date, booking_seat_number_lbl.getText(), false);
                                                    if (saveTicket) {
                                                        purchaseDone1 = true;
                                                        rfc1 = String.valueOf(rfc);
                                                        //confirmPurchase(String.valueOf(rfc));
                                                    } else {
                                                        System.out.println("Did not saved the purchase in database");
                                                    }
                                                }
                                            }
                                        } else {
                                            System.out.println("Tur seat is null.");

                                            System.out.println("Second condition 2");
                                            StringBuilder rfc = connection.generateRandomRFC();
                                            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                            boolean saveTicket = connection.savePurchasedTicket(user.getUserId(), booking_flight_number_lbl.getText(), String.valueOf(rfc), date, booking_seat_number_lbl.getText(), false);
                                            if (saveTicket){
                                                purchaseDone1 = true;
                                                rfc1 = String.valueOf(rfc);
                                                //confirmPurchase(String.valueOf(rfc));
                                            }else {
                                                System.out.println("Did not saved the purchase in database");
                                            }
                                        }

                                        if (purchaseDone1 && purchaseDone2) {
                                            System.out.println("Booked two-ways flights");
                                            confirmPurchase(rfc2); // can send more information form here.
                                        } else if (purchaseDone1) {
                                            System.out.println("Booked only one-way flight");
                                            confirmPurchase(rfc1);
                                        }

                                    }else{
                                        System.out.println("Card not valid");
                                        confirmActions.displayMessage(payment_err_msg, "Card is not valid.", true);
                                    }
                                } else {
                                    confirmActions.displayMessage(payment_err_msg, "CVC is empty!", true);
                                }
                            } else {
                                confirmActions.displayMessage(payment_err_msg, "Month is empty!", true);
                            }
                        } else {
                            confirmActions.displayMessage(payment_err_msg, "Year is empty!", true);
                        }
                    } else {
                        confirmActions.displayMessage(payment_err_msg, "Last name is empty!", true);
                    }
                } else {
                    confirmActions.displayMessage(payment_err_msg, "First name is empty!", true);
                }
            }else {
                confirmActions.displayMessage(payment_err_msg, "Card numbers is empty!", true);
            }

        }else if(e.getSource() == seat_next_btn){
            //<editor-fold desc="file">
                String name_s = booking_first_name_textfield.getText();
                String lname_s = booking_last_name_textfield.getText();
                String fourdigit = booking_four_digits_textfield.getText();
                String email = booking_email_textfield.getText();
                String isTur = booking_is_retur_lbl.getText();
                String seat = booking_seat_number_lbl.getText();
            //</editor-fold>

                if (name_s.length() >= 3 && name_s.length() <= 30){
                    if (lname_s.length() >= 3 && lname_s.length() <= 30) {
                        if (fourdigit.length() == 12) {
                            if (email.length() >= 6 && email.length() <= 60 && email.contains("@") && email.contains("gmail")) {
                                if (!seat.isEmpty()) {

                                    if (round_trip_flights.size() == 1){
                                        System.out.println("Active tur");
                                        departure_seat = booking_seat_number_lbl.getText();
                                        return_seat = booking_flight_number_lbl.getText();

                                        booking_flight_number_lbl.setText(null);
                                        booking_seat_number_lbl.setText(null);
                                        booking_price_lbl.setText(null);

                                        // clear the operation - preperBeforeCreatingSeats
                                        boolean build = preperBeforeCreatingSeats();
                                        if (build){
                                            booking_departure_lbl.setText(round_trip_flights.get(0).getDeparture_name());
                                            booking_destination_lbl.setText(round_trip_flights.get(0).getDestination_name());
                                            booking_departure_extra_lbl.setText(round_trip_flights.get(0).getDeparture_date());
                                            booking_destination_extra_lbl.setText(round_trip_flights.get(0).getDestination_date());
                                            booking_flight_number_lbl.setText(round_trip_flights.get(0).getId());
                                            seat_price = Double.parseDouble(round_trip_flights.get(0).getPrice());
                                            booking_price_lbl.setText(String.valueOf(seat_price));
                                            createThisSeat(round_trip_flights, 0);
                                            round_trip_flights.clear();
                                        }
                                    } else {
                                        System.out.println("To front");
                                        payment_anchorpane.toFront();
                                    }

                                } else {
                                    confirmActions.displayMessage(booking_msg_lbl, "Please choose a seat!", true);
                                }
                            } else {
                                confirmActions.displayMessage(booking_msg_lbl, "Email char 6-30 or format issue!", true);
                            }
                        } else {
                            confirmActions.displayMessage(booking_msg_lbl, "SSN shall be 12 chars!", true);
                        }
                    } else {
                        confirmActions.displayMessage(booking_msg_lbl, "Lastname shall be 3-30 chars!", true);
                    }
                }else {
                    confirmActions.displayMessage(booking_msg_lbl, "Firstname shall be 3-30 chars!", true);
                }
        }else if(e.getSource() == success_purchase_to_dashboard_button){
            updateHistoryList();
            flight_anchorpane.toFront();
            restore_psgr_info();
            success_purchase_anchorpane.toBack();
            payment_anchorpane.toBack();

        }
    }

    private void confirmPurchase(String rfc) {
        System.out.println(booking_email_textfield.getText() + " Your email!!!");
        if (!booking_email_textfield.getText().isEmpty()){
            boolean sentMail = Purchase.sendEmail(booking_email_textfield.getText(), booking_first_name_textfield.getText(), booking_flight_number_lbl.getText(), booking_seat_number_lbl.getText(), booking_price_lbl.getText());
            if (sentMail){
                if (departure_seat != null){
                    System.out.println("Tur is reached here");
                    history_reference_number_lbl.setText(rfc.toString());
                    success_purchase_anchorpane.toFront();
                    departure_seat = null;
                    System.out.println("Email successfully sent!");
                }else {
                    System.out.println("Returne has been reached here");
                    history_reference_number_lbl.setText(rfc.toString());
                    success_purchase_anchorpane.toFront();
                }
            }else {
                System.out.println("The email addrss is not correct");
                //JOptionPane.showMessageDialog(null, "The email address is not correct!");
            }
        }
        System.out.println("saved information in database");
    }

    /**
     *
     */
    public void restore_psgr_info(){
        booking_first_name_textfield.clear();
        booking_last_name_textfield.clear();
        booking_four_digits_textfield.clear();
        booking_email_textfield.clear();
        booking_seat_number_lbl.setText(null);
        booking_flight_number_lbl.setText(null);
        booking_price_lbl.setText(null);
        card_nbr.clear();
        card_fname.clear();
        card_lname.clear();
        card_month.clear();
        card_year.clear();
        card_cvc.clear();
        taken_seat_economy.clear();
        taken_seat_business.clear();
        economy_seat_gridpane.getChildren().clear();
        business_seat_gridpane.getChildren().clear();
        round_trip_flights.clear();
        departure_seat = null;
        return_seat = null;

    }

    /**
     * Navigate to admin pages.
     * @param e
     * @author Obed.
     */
    public void switchToAdminView(ActionEvent e) {

        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            try {
                User user = connection.authenticationAdmin(login_email.getText(), login_pass.getText());
                if (user != null) {

                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin/AdminView.fxml")));
                    main_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    main_scene = new Scene(root);
                    main_stage.setTitle("Admin window");
                    main_stage.setScene(main_scene);
                    main_stage.show();


                    adminControl.fillMemmbersTable(root);
                    adminControl.fillTicketTable(root);
                    adminControl.fillTableFlights(root);

                    member_listview = (ListView<String>) root.lookup("#member_listview");
                    if(member_listview != null)
                    {
                        ArrayList<User> member = connection.getAllUsers();
                        ArrayList<String> temp = new ArrayList<>();
                        int pageNr = 0;
                        for(User item: member)
                        {
                            pageNr++;
                            StringBuilder temp2 = new StringBuilder();
                            System.out.println(item.isIsadmin() + " Obedddddd ");
                            temp2.append(pageNr).append(" Member[ id. ").append(item.getUserId()).append(", First Name: ").append(item.getFirstName()).append(", List Name: ").append(item.getLastName()).append(", Adress: ").append(item.getAddress()).append(", Email: ").append(item.getEmail()).append(", Number: ").append(item.getPhoneNumber()).append(", Password: ").append(item.getPassword()).append(", isAdmin: ").append(item.isIsadmin()).append(" ]");
                            temp.add(temp2.toString());
                        }

                        ObservableList<String> tickets = FXCollections.observableList(temp);
                        member_listview.setItems(tickets);

                    }

                    ticket_listview = (ListView<String>) root.lookup("#ticket_listview");
                    if(ticket_listview != null)
                    {


                        ArrayList<Book> ticket = connection.searchTicket();
                        ArrayList<String> temp = new ArrayList<>();
                        for(Book item: ticket)
                        {
                            StringBuilder temp2 = new StringBuilder();
                            temp2.append("Ticket[ user. ").append(item.getUser_id()).append(", flightid: ").append(item.getFlight_id()).append(", seat number: ").append(item.getSeatNbr()).append(" ]");
                            temp.add(temp2.toString());
                        }
                        ObservableList<String> tickets = FXCollections.observableList(temp);
                        ticket_listview.setItems(tickets);
                    }
                } else {
                    error_message_lbl.setText("Wrong email or pass!");
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(a -> error_message_lbl.setText(null));
                    pause.play();
                }
            }catch (IOException io){
                io.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            error_message_lbl.setText("Fill the field!");

        }
    }

    /**
     * User even handler.
     * @param e
     */
    public void userDev(ActionEvent e){
        if (e.getSource() == menu_profile_btn) {
            profile_anchorpane.toFront();
            toggleMenuColor();
        }
        else if(e.getSource() == booking_close_btn || e.getSource() == booking_close_second_page_btn){
            booking_seat_anchorpane.toBack();
            restore_psgr_info();
        }
        else if (e.getSource() == menu_flight_btn) {
            flight_anchorpane.toFront();
            toggleMenuColor();
            menu_highlight_color_flight.setVisible(true);
            map_menu_user_image.setOpacity(1);
        }
        else if (e.getSource() == menu_history_btn) {
            history_anchorpane.toFront();
            toggleMenuColor();
            menu_highlight_color_history.setVisible(true);
            history_menu_user_image.setOpacity(1);
        }
        else if (e.getSource() == menu_entertainment_btn) {
            entertainment_anchorpane.toFront();
            toggleMenuColor();
            menu_highlight_color_entertainment.setVisible(true);
            entertainment_menu_user_image.setOpacity(1);

        }
        else if(e.getSource() == menu_support_btn){
            support_anchorpane.toFront();
            toggleMenuColor();
            menu_highlight_color_support.setVisible(true);
            support_menu_user_image.setOpacity(1);
        }

        // navigating av
        else if(e.getSource() == date_previous_day_button){
            System.out.println("NOOOO");
            if (date_input_flight.getValue() == null){
                System.out.println("Nulll value");
                LocalDate date = LocalDate.now();
                date_input_flight.setValue(date);
                date_input_flight.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                    System.out.println("Not null 1");
                        System.out.println(date);
                        // do something
                        System.out.println(oldValue + " old");
                        System.out.println(newValue + " new");
                        date_input_flight.setValue(date);

                });
            }
            if (date_input_flight.getValue() != null){
                date_input_flight.setValue(date_input_flight.getValue().minusDays(1));
            }else {
                confirmActions.displayMessage(search_flight_error_lbl, "Date is not initialized!", true);
            }
        }
        else if(e.getSource() == date_next_day_button){
            if (date_input_flight.getValue() != null){
                date_input_flight.setValue(date_input_flight.getValue().plusDays(1));
            }else
                confirmActions.displayMessage(search_flight_error_lbl, "Date is not initialized!", true);
        }
        else if(e.getSource() == date_previous_day_return_button){
            if (dateR_input_flight.getValue() != null){
                dateR_input_flight.setValue(dateR_input_flight.getValue().plusDays(1));
            }else
                confirmActions.displayMessage(search_flight_error_lbl, "Date is not initialized!", true);
        }
        else if(e.getSource() == date_next_day_return_button){
            if (dateR_input_flight.getValue() != null){
                dateR_input_flight.setValue(dateR_input_flight.getValue().plusDays(1));
            }else
                confirmActions.displayMessage(search_flight_error_lbl, "Date is not initialized!", true);
        }


    }

    /**
     *
     */
    private void toggleMenuColor() {
        menu_highlight_color_flight.setVisible(false);
        menu_highlight_color_history.setVisible(false);
        menu_highlight_color_entertainment.setVisible(false);
        menu_highlight_color_support.setVisible(false);

        map_menu_user_image.setOpacity(0.5);
        history_menu_user_image.setOpacity(0.5);
        entertainment_menu_user_image.setOpacity(0.5);
        support_menu_user_image.setOpacity(0.5);
    }

    /**
     * Administrator dev.
     * @param e
     * @throws IOException
     * @autor Obed.
     */
    public void adminDev(ActionEvent e) throws SQLException {
        if(e.getSource() == admin_logout_button)
        {
            switchToLogin(e);
        }
        else if(e.getSource() == refreshMembersBtn_admin){
            adminControl.updateMemberTable();
        }else if(e.getSource() == deletS_btn_mbr_admin){
            // delete a member here
            adminControl.updateMemberTable();
        }
        else if(e.getSource() == returnToMemberListBtn_admin)
        {
            admin_members_anchorpane.toFront();
        }

        else if(e.getSource() == registerMemberBtn_admin)
        {
            admin_register_anchorpane.toFront();
        }

        else if(e.getSource() == admin_flights_button)
        {
            admin_flights_anchorpane.toFront();
        }

        else if(e.getSource() == admin_tickets_button)
        {
            admin_tickets_anchorpane.toFront();
        }

        else if(e.getSource() == admin_members_button)
        {
            admin_members_anchorpane.toFront();
        }

    }

    //----------------- SEARCH FLIGHTS -----------------//

    /**
     * @param e
     */
    public void searchFlight(ActionEvent e) {
        search.searchFlight();
    }
    public void checkboxEvent(ActionEvent e){
        search.checkboxEvent(e);
    }
    //----------------- SEARCH FIELD -----------------//

    /**
     *
     */
    public void searchHit(){
        search.searchHit();
    }

    /**
     *
     */
    public void change_search_info(){
        if (!from_input_flight_textfield.getText().isEmpty() || !display_input_flights.getText().isEmpty()){
            String from = from_input_flight_textfield.getText();
            String to = display_input_flights.getText();
            from_input_flight_textfield.setText(to);
            display_input_flights.setText(from);
        }else {
            confirmActions.notifyError(user_dashboard_msgbox_pane, user_notification_lbl, "Search fields are empty!");
        }

    }

    /**
     * On key pressed search and show name.
     * @author Khabib.
     */
    public void searchAppear(){
        if (search_f_name != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(search_f_name.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (search_list_appear != null){
                    search_list_appear.getItems().removeAll();
                }
                search_list_appear.setVisible(true);
                search_list_appear.setItems(searchAprear);
                search_list_appear.getSelectionModel().selectedItemProperty().addListener(e ->{
                    search_f_name.setText(search_list_appear.getSelectionModel().getSelectedItem());
                        search_list_appear.setVisible(false);
            });
            }
        }

        hidePopupSearch(search_f_name.getText(), search_list_appear);
    }

    /**
     * On key pressed search and show name.
     * @author Khabib.
     */
    public void departureNameAppear(){
        if (from_input_flight_textfield != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(from_input_flight_textfield.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (search_list_appear_second != null){
                    search_list_appear_second.getItems().removeAll();
                }
                search_list_appear_second.setVisible(true);
                search_list_appear_second.setItems(searchAprear);
                search_list_appear_second.getSelectionModel().selectedItemProperty().addListener(e ->{
                    from_input_flight_textfield.setText(search_list_appear_second.getSelectionModel().getSelectedItem());
                        search_list_appear_second.setVisible(false);
            });
            }
        }

        hidePopupSearch(from_input_flight_textfield.getText(), search_list_appear_second);
    }

    /**
     * On key pressed search and show name.
     * @author Khabib.
     */
    public void destinationNameAppear(){
        if (display_input_flights != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(display_input_flights.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (search_list_appear_third != null){
                    search_list_appear_third.getItems().removeAll();
                }
                search_list_appear_third.setVisible(true);
                search_list_appear_third.setItems(searchAprear);
                search_list_appear_third.getSelectionModel().selectedItemProperty().addListener(e ->{
                    display_input_flights.setText(search_list_appear_third.getSelectionModel().getSelectedItem());
                        search_list_appear_third.setVisible(false);
            });
            }
        }

        hidePopupSearch(display_input_flights.getText(), search_list_appear_third);
    }

    /**
     * @param text
     * @param popupWindow
     * @author Khabib and Sossio.
     */
    public void hidePopupSearch(String text, ListView popupWindow) {
        if(text == "") {
            popupWindow.setVisible(false);
        }
    }

    /**
     * @param srch
     * @return
     */
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

    /**
     * @param searchTargetKey
     * @return
     */
    private ArrayList<String> compareSearchKey(String searchTargetKey) {
        ArrayList<String> obs  = new ArrayList<>();
        for (Enum item : CountryList.values()) {
            if (item.toString().contains(searchTargetKey)){
                obs.add(item.toString());
                System.out.println(item + " /" + searchTargetKey);
            }
        }
        return obs;
    }

    //----------------- Support -----------------//

    /**
     * @param e
     */
    public void support_event_handler(ActionEvent e){
        support.supportInfo(e);
    }


    //----------------- History  -----------------//

    /**
     *
     */
    public void setInfoIntoTableHistorik(){ // the method calls from user dashboard to load everything.
        history_single_delete_button = (Button) root.lookup("#history_single_delete_button");
        history_display_flight_path_button = (Button) root.lookup("#history_display_flight_path_button");
        history_multiple_delete_button = (Button) root.lookup("#history_multiple_delete_button");
        history_tableview = (TableView<UserHistory>) root.lookup("#history_tableview");
        history_select_all_checkbox = (CheckBox) root.lookup("#history_select_all_checkbox");
        history_tableview.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("no_col_table_historik"));
        history_tableview.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("company_col_table_historik"));
        history_tableview.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("model_col_table_historik"));
        history_tableview.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("rfc_col_table_historik"));
        history_tableview.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("flightid_col_table_historik"));
        history_tableview.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("from_col_table_historik"));
        history_tableview.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("to_col_table_historik"));
        history_tableview.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("seatno_col_table_historik"));
        history_tableview.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("date_col_table_historik"));
        history_tableview.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("price_col_table_historik"));
        history_tableview.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("select_col_table_historik"));
        history_tableview.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        history_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableList, oldValue, newValue) ->{

            if (newValue != null){
                if (newValue.getSelect_col_table_historik().isSelected()){
                    newValue.getSelect_col_table_historik().setSelected(false);
                }else
                    newValue.getSelect_col_table_historik().setSelected(true);
                System.out.println("selected item: " + newValue.getCompany_col_table_historik() + " value: " + newValue.getSelect_col_table_historik().isSelected());
             }
            history_single_delete_button.setDisable(false);
            history_display_flight_path_button.setDisable(false);

        });
        updateHistoryList();
        history_select_all_checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("selected all");
                history_flights = history_tableview.getItems();
                boolean selectedAllItems = false; // selected or not

                for (UserHistory item : history_flights){
                    if (history_select_all_checkbox.isSelected()){
                        selectedAllItems = true;
                        item.getSelect_col_table_historik().setSelected(true);
                        System.out.println(item.getSelect_col_table_historik().isSelected() + " state");
                    }else {
                        selectedAllItems = false;
                        item.getSelect_col_table_historik().setSelected(false);
                        System.out.println(item.getSelect_col_table_historik().isSelected() + " state");
                    }
                }

                if (selectedAllItems){ // check if all items are selected
                    history_multiple_delete_button.setDisable(false);

                }else {
                    history_multiple_delete_button.setDisable(true);
                    System.out.println("no content in the list");
                }

            }
        });

    }

    /**
     * separated method to use multiple times
     * it will update the historic table in user dashboard everytime an action happen or user want to navigate to the panel etc.
     */
    public void updateHistoryList(){
        ArrayList<UserHistory> list = connection.searchDataForTableHistory(Integer.parseInt(user.getUserId()), false);

        history_items_list = FXCollections.observableArrayList(list);
        history_tableview.setItems(history_items_list);
    }

    /**
     * the method will handle delete option in history panel.
     * @param e event
     */
    public void userRemoveHistory(ActionEvent e){
        if (history_tableview.getItems().size() > 0){ // check if there is any items before running the operation.
            if (e.getSource() == history_single_delete_button){ // if single remove button clicked
                history_flights = history_tableview.getItems(); // get the whole tables items into an observable list to compare.
                if (history_flights != null){ // if observable items has item
                    // show a confirmation message to user
                    boolean confirmed = confirmActions.confirmThisAction("Confirm to delete selected item", "Do you want to proceed?", "The selected items will be deleted!");
                    if (confirmed){ // if user confirm the action
                        for (UserHistory item: history_flights){ // loop through all historic items
                            if (item.getSelect_col_table_historik().isSelected()){ // check if the checkbox for one or more item is selected
                                boolean ok = connection.deleteHistoryByRFC(item.getRfc_col_table_historik()); // send the actual reference number as an argument to database to compare and delete
                                if (ok){ // if database succeed to delete the item runs this statement
                                    updateHistoryList(); // historic table updates
                                    System.out.println("Item has been deleted successfully!"); // show a success message for user
                                }
                            }
                        }
                    }else { // if user not confirm the action
                        System.out.println("not deleted screen message");
                    }
                }
            }else if(e.getSource() == history_multiple_delete_button){ // if remove all button clicked
                history_flights = history_tableview.getItems(); // get the whole tables items into an observable list to compare.
                if (history_flights != null){ // if observable items has item
                    // show a confirmation message to user
                    boolean confirmed = confirmActions.confirmThisAction("Confirm to delete the item", "Do you want to proceed?", history_flights.size() +" items will be deleted.");
                    if (confirmed){ // if user confirm the action
                        for (UserHistory item: history_flights){ // loop through all historic items
                            boolean ok = connection.deleteHistoryByRFC(item.getRfc_col_table_historik()); // send the actual reference number as an argument to database to compare and delete
                            if (ok){ // if database succeed to delete the item runs this statement
                                updateHistoryList(); // historic table updates
                                System.out.println("Item has been deleted successfully!"); // show a success message for user
                            }
                        }
                    }else {
                        System.out.println("The action is canceled! screen message"); // canceled message to user
                    }
                }

            }

        }
    }

    //----------------- GETTERS AND SETTERS -----------------//
    public Scene getMain_scene() {
        return main_scene;
    }
    public void setMain_scene(Scene main_scene) {
        this.main_scene = main_scene;
    }
    public Stage getMain_stage(){return main_stage;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}