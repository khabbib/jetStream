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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

    @FXML public Label notify_user_dashboard;
    @FXML public Pane msgBox_user_dashboard;
    // Default variables
    private CreateWorld createWorld;
    public static World world;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static User user;
    private File dir;
    private File[] files;
    private static boolean editingProfile;

    // FXML variables
    @FXML private ButtonBar logout;
    @FXML public ScrollPane scrollPane;
    @FXML public ScrollPane scrollFlights;
    @FXML private Label error_msg;
    @FXML public Label success_msg;
    @FXML public Label u_name;
    @FXML public VBox display_flight;

    // ===== PROFILE EDITING =====
    // General
    @FXML public ImageView profilePicture;
    @FXML public ImageView profilePicturePreview;
    @FXML public TextField profileFirstName, profileLastName, profileEmail, profileAdress, profilePhoneNumber;
    @FXML public Button edit_pfp_cancel_btn;
    @FXML public GridPane profileSelector;
    @FXML public PasswordField profileOldPassword;
    @FXML public TextField profileNewPassword, profileNewPasswordConfirm;
    @FXML public Button btnEditProfile;
    private static boolean changingProfileImage = false;

    // Issues
    @FXML public Label edit_pfp_fname_issue, edit_pfp_lname_issue, edit_pfp_address_issue, edit_pfp_email_issue, edit_pfp_phone_issue;
    @FXML public Label edit_pfp_old_pwd_issue, edit_pfp_new_pwd_issue, edit_pfp_new_c_pwd_issue;

    // ===== MINI GAMES =====
    @FXML private StackPane game1;
    @FXML private StackPane game2;
    @FXML private Button quizButton;

    // Seat
    private final GridPane gridE = new GridPane(); //Layout
    private final GridPane gridB = new GridPane(); //Layout

    // toggle options
    @FXML private Button iconProfile, iconFlight, iconHistorik, iconGame, iconSupport, iconCloseSeat, iconCloseSeat1;
    @FXML public AnchorPane pnlProfile, pnlHistorik, pnlFlight, pnlGame, pnlSupport;
    @FXML private Pane lgtF_menu_user, lgtH_menu_user, lgtG_menu_user, lgtS_menu_user;

   //</editor-fold>

    //<editor-fold desc="ADMIN VARIABLES">
    @FXML private ListView<String> ticketListView, memberListView, flightListView;
    @FXML private AnchorPane pnlFlights, pnlTickets, pnlMember, registerAnchorPane;
    @FXML private Button flightsBtn, membersBtn, ticketsBtn, logoutButton, registerCommitBtn_admin, registerMemberBtn_admin, returnToMemberListBtn_admin, refreshMembersBtn_admin, deleteMemberBtn_admin;

    //</editor-fold>
    //<editor-fold desc="DASHBOARD VARIABLES">

    // purchase variables
    @FXML private AnchorPane pnlPayment;
    @FXML public TextField card_nbr;
    @FXML public TextField card_fname;
    @FXML public TextField card_lname;
    @FXML public TextField card_month;
    @FXML public TextField card_year;
    @FXML public TextField card_cvc;
    @FXML private Button card_prev_btn, card_purchase_btn, seat_next_btn;
    @FXML public Label card_counter_nbr;

    // scrollpane seats
    @FXML public ScrollPane business_scrollpane;
    @FXML public ScrollPane eco_scrollpane;

    @FXML private AnchorPane pnl_success_purchase;
    @FXML private Button redirect_to_dash_btn, print_ticket_purchase_btn;

    // From seat
    @FXML public AnchorPane pnlSeat;
    @FXML public ImageView pgr_prf_seat_pnl;
    @FXML public AnchorPane pnlPassager;
    @FXML public TextField first_name_seat_pnl;
    @FXML public TextField last_name_seat_pnl;
    @FXML public TextField four_digit_seat_pnl;
    @FXML public TextField email_seat_pnl;
    @FXML public AnchorPane flight_seats_eco;
    @FXML public AnchorPane flights_seats_business;
    @FXML public Label seat_nbr_seat_pnl,msg_seat_pnl,flight_nbr_seat_pnl, rtur_seat_nbr_seat_pnl,
            price_seat_pnl,from_info_seat_pnl, to_info_seat_pnl,
            from_d_info_seat_pnl, to_d_info_seat_pnl, isTur_seat_pnl;
    @FXML public VBox tur_info_seat_panel;


    // menu images
    @FXML public ImageView map_menu_user;
    @FXML public ImageView historik_menu_user;
    @FXML public ImageView game_menu_user;
    @FXML public ImageView support_menu_user;

    //</editor-fold>
    //<editor-fold desc="SEAT VARIABLES"
    private ArrayList<String> takenSeatE = new ArrayList<>();
    private ArrayList<String> takenSeatB = new ArrayList<>();
    private double price = 0.0;
    private ArrayList<Flight> turAndReturnFlights = new ArrayList<>();
    private boolean hasReturnFlight = false;
    private String turSeat, turFlight_nbr_seat_pnl, rTurSeat;

    //</editor-fold>
    //<editor-fold desc="HISTORY VARIABLES">
    ObservableList<UserHistory> fetchedList;
    ObservableList<UserHistory> items;
    @FXML public TableView<UserHistory> table_historik;
    @FXML public Label rfc_no_sucesspnl;
    @FXML private Button mremove_btn_historik, sremove_btn_historik, flightPathBtn;
    @FXML private CheckBox select_all_box_historik;
    @FXML private TableColumn<Book, String>
            no_col_table_historik;
    @FXML private TableColumn<Book, String> company_col_table_historik;
    @FXML private TableColumn<Book, String> model_col_table_historik;
    @FXML private TableColumn<Book, String> rfc_col_table_historik;
    @FXML private TableColumn<Book, String> flightid_col_table_historik;
    @FXML public TableColumn<Book, String> from_col_table_historik;
    @FXML public TableColumn<Book, String> to_col_table_historik;
    @FXML private TableColumn<Book, String> seatno_col_table_historik;
    @FXML private TableColumn<Book, String> date_col_table_historik;
    @FXML private TableColumn<Book, String> price_col_table_historik;
    //</editor-fold
    //<editor-fold desc="SEARCH VARIABLES">
    public ArrayList<Flight> avalibleFlights = new ArrayList<>();
    @FXML public ListView<String> searchListAppear;
    @FXML public ListView<String> searchListAppear2;
    @FXML public ListView<String> searchListAppear3;
    @FXML private ImageView exchange_search_flight;
    @FXML public Label nbr_of_available_flights, err_search_flight;
    @FXML public DatePicker date_input_flight,dateR_input_flight;
    @FXML public TextField from_input_flight;
    @FXML public HBox rtur_date_pick;


    @FXML public Button prev_tur_date_flight, next_tur_date_flight, prev_rtur_date_flight, next_rtur_date_flight;


    @FXML public CheckBox turR_checkBox_flight;
    @FXML public TextField disc_input_flight;
    @FXML private Label no_flight_aval_msg;
    @FXML public TextField search_f_name;

    //</editor-fold
    //<editor-fold desc="REGISTER VARIABLES">
    @FXML public Label registration_error;
    // Register a new user
    @FXML public TextField first_name_reg;
    @FXML public TextField last_name_reg;
    @FXML public TextField address_reg;
    @FXML public TextField emailaddress_reg;
    @FXML public TextField phone_number_reg;
    @FXML public TextField password_reg;
    @FXML public TextField confirm_password_reg;
    @FXML public Label name_issue_reg;
    @FXML public Label last_name_issue_reg;
    @FXML public Label address_issue_reg;
    @FXML public Label email_issue_reg;
    @FXML public Label phone_number_issue_reg;
    @FXML public Label password_issue_reg;
    @FXML public Label confirm_password_issue_reg;

    //Register user/admin in MemberPanel

    // Register a new user
    @FXML public CheckBox isAdminCheckbox;
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
    public ObservableList<User> fetchedList_admin;
    public ObservableList<User> items_admin;
    @FXML public Button delet_btn_mbr_admin, deletS_btn_mbr_admin;
    @FXML public TableView<User> table_member_admin;
    @FXML public CheckBox select_col_mbr_admin;

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

    //<editor-fold desc="instance initialize">
    application.Components.Support support;
    Search search;
    ConfirmActions confirmActions;
    DashboardController dashboardController;
    Connection connection;
    Config config;

    FlightPaths flightPaths;
    RegistrationUser registrationUser;
    RegisterAdmin registerAdmin;
    InitializeFXM initializeFXM;
    AdminControl adminControl;
    //</editor-fold>

    // Loader in login
    @FXML public ImageView login_loader_flight;

    //----------------- HOME -----------------//
    public Controller(){
        connection = new Connection(this);
        config = new Config(this, root, stage);
        support = new Support(this);
        confirmActions = new ConfirmActions(this);
        search = new Search(this, connection, confirmActions);
        registrationUser = new RegistrationUser(this, connection, config);
        registerAdmin = new RegisterAdmin(this, connection, config);
        dashboardController = new DashboardController(this, root, connection);
        initializeFXM = new InitializeFXM(this,connection);
        flightPaths = new FlightPaths(this);
        adminControl = new AdminControl(this, connection);
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
        pnlFlight.toFront();
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
                    try {
                        profilePicturePreview.setImage(connection.getProfilePicture(user));
                    } catch (SQLException ei) {
                        ei.printStackTrace();
                    }
                } else {
                    confirmActions.displayMessage(error_msg, "Wrong email or password!", true);
                }
            } else {
                confirmActions.displayMessage(error_msg, "Email has wrong format!", true);
            }
        } else {
            confirmActions.displayMessage(error_msg, "Email or password is empty, please fill in fields!", true);
        }
    }

    /**
     * Resets the text in textfield from-to.
     * @author Sossio.
     */
    public void resetSearchFromTo() {
        from_input_flight.setText("");
        disc_input_flight.setText("");
        searchListAppear2.setVisible(false);
        searchListAppear3.setVisible(false);
    }

    /**
     * Resets the text in textfield country name.
     * @author Sossio.
     */
    public void resetSearchCountry() {
        search_f_name.setText("");
        searchListAppear.setVisible(false);
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
        createWorld = new CreateWorld();
        world = createWorld.init(this, connection);
        createWorld.addWorldInMap(scrollPane, user);
        scrollPane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        world.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        //coordinates();
        setInfoIntoTableHistorik();
    } // the method will render dashboard page for user

    public void coordinates(){
        for (int i = 0; i < 20; i++) {
            Circle circle = new Circle(560,i*20,4);
            if (i%2 == 0) {circle.setFill(Color.RED);
            } else {circle.setFill(Color.GREEN);}
            world.getChildren().add(circle);
        }
    }

    /**
     * @param e
     * @throws IOException
     */
    public void noLoginRequired(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Dashboard.fxml")));
        dashboardController.userInitializeFXML(root, user);
        scrollFlights = (ScrollPane) root.lookup("#scrollFlights");
        scrollFlights.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pnlSeat = (AnchorPane) root.lookup("#pnlSeat");

        display_flight = (VBox) scrollFlights.getContent();
        scrollPane = (ScrollPane) root.lookup("#scrollPane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        createWorld = new CreateWorld();
        world = createWorld.init(this, connection);

        scrollPane.setContent(new StackPane(world));
        scrollPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        u_name = (Label) root.lookup("#u_name");
        u_name.setText(null);
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        MoveScreen.moveScreen(root,stage);
        stage.setTitle("Test dashboard window");
        stage.setScene(scene);
        stage.show();
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
            success_msg = (Label) root.lookup("#success_msg");
            confirmActions.displayMessage(success_msg, "User successfully registered!", false);
        } else {
            confirmActions.displayMessage(success_msg, "Error", true);
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
            pnlMember.toFront();
            confirmActions.displayMessage(registration_error_admin, "User successfully registered!", false);
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
        success_msg = (Label) root.lookup("#sucess_msg");
    }

    /**
     * flight lists dashboard
     * @param flights
     */
    public void fillFlights (ArrayList <Flight> flights) {
        boolean isFlight = checkFlightExistans(flights);
        if (isFlight){
            display_flight.getChildren().clear();
            scrollFlights.setVvalue(0);
            nbr_of_available_flights.setText(String.valueOf(flights.size()));
            try {
                pgr_prf_seat_pnl.setImage(connection.getProfilePicture(user));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } // update profile picture

            for (int i = 0; i < flights.size();i++){
                HBox hbox = createFlightsContent(flights, i);
                StackPane stackholer = new StackPane();
                stackholer.getChildren().add(hbox);
                stackholer.setAlignment(Pos.TOP_LEFT);
                display_flight.getChildren().addAll(hbox); // the box
                display_flight.setAlignment(Pos.TOP_LEFT);
                if (flights.get(i).isrTur()){
                    turAndReturnFlights.add(flights.get(i));
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
                            if(!turAndReturnFlights.isEmpty()){
                                turAndReturnFlights.remove(finalI1); // remove one-way flight
                                hasReturnFlight = true; // set to true if there is more flight
                            }
                        }else { // chose one-way
                            fillInfoSeatPnl(flights, finalI1);
                            createThisSeat(flights, finalI1);
                        }

                        // flights seat panel will be shown

                        pnlSeat.toFront();
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
        try {
            int[] amountSeats = connection.getSeatNumber(flights.get(finalI1).getId());
            boolean buildSeatsSuccess = chooseSeat(amountSeats[0], amountSeats[1]);
            if(buildSeatsSuccess){
                ArrayList<String> bookedS = connection.getBookedSeats(flights.get(finalI1).getId());
                if (!bookedS.isEmpty()){
                    for (String seat : bookedS){
                        if (seat.contains("E")){
                            takenSeatE.add(seat);
                            showTakenS(seat, gridE);
                        }
                        if(seat.contains("B")){
                            takenSeatB.add(seat);
                            showTakenS(seat, gridB);
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private boolean preperBeforeCreatingSeats() {
        takenSeatE.clear();
        takenSeatB.clear();
        gridE.getChildren().clear();
        gridB.getChildren().clear();

        // Seat window
        gridE.setHgap(5);
        gridE.setVgap(5);
        gridB.setHgap(5);
        gridB.setVgap(5);
        HBox hboxLR_seat = new HBox();
        hboxLR_seat.getChildren().addAll(gridE);
        HBox hboxTLR_seat = new HBox();
        hboxTLR_seat.getChildren().add(gridB);
        hboxTLR_seat.setAlignment(Pos.TOP_CENTER);
        flight_seats_eco.getChildren().add(hboxLR_seat);
        flights_seats_business.getChildren().add(hboxTLR_seat);
        return true;
    }



    // check if there is any flight for searched name
    private boolean checkFlightExistans(ArrayList<Flight> flights) {
        boolean flight = false;
        if (flights == null){
            Label lable = new Label("No flight available!");
            lable.setStyle("-fx-text-fill: #999; -fx-padding: 20px");
            if (!display_flight.getChildren().isEmpty()){
                display_flight.getChildren().clear();
                display_flight.getChildren().add(lable);
            }else {
                display_flight.getChildren().add(lable);
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
        first_name_seat_pnl.setText(user.getFirstName());
        last_name_seat_pnl.setText(user.getLastName());
        email_seat_pnl.setText(user.getEmail());
        from_info_seat_pnl.setText(flights.get(finalI1).getDeparture_name());
        to_info_seat_pnl.setText(flights.get(finalI1).getDestination_name());
        from_d_info_seat_pnl.setText(flights.get(finalI1).getDeparture_date());
        to_d_info_seat_pnl.setText(flights.get(finalI1).getDestination_date());
        flight_nbr_seat_pnl.setText(flights.get(finalI1).getId());
        price = Double.parseDouble(flights.get(finalI1).getPrice());
        isTur_seat_pnl.setText(String.valueOf(flights.get(finalI1).isrTur()));
        price_seat_pnl.setText(String.valueOf(price));
        // profile picture

        // chosen flights color
        for (int g = 0; g < display_flight.getChildren().size(); g++){
            display_flight.getChildren().get(g).setOpacity(1);
        }
        display_flight.getChildren().get(finalI1).setOpacity(0.95);

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
        if (editingProfile == false) {
            edit_pfp_cancel_btn.setDisable(false);

            profileFirstName.setDisable(false);
            profileLastName.setDisable(false);
            profileAdress.setDisable(false);
            profileEmail.setDisable(true); // Let be false cause an error will occur!
            profilePhoneNumber.setDisable(false);
            profileOldPassword.setDisable(false);
            profileNewPassword.setDisable(false);
            profileNewPasswordConfirm.setDisable(false);
            btnEditProfile.setText("Confirm");
            editingProfile = true;
        } else {

            User editedUser = user;

            boolean successMessage = true;

            // Edit Firstname
            if (!profileFirstName.getText().isEmpty() && (profileFirstName.getText().length() >= 3 && profileFirstName.getText().length() <= 30)) {
                editedUser.setFirstName(profileFirstName.getText());

                System.out.println("Updating user firstname...");
                user = editedUser;

                connection.updateUserFirstName(user);

            } else {
                System.out.println("Firstname issue!");
                confirmActions.displayMessage(edit_pfp_fname_issue, "Size issue 3-30!", true);
                profileFirstName.setText(connection.getUserDatabaseFirstName(user.getUserId()));

                successMessage = false;
            }

            // Edit Lastname
            if(!profileLastName.getText().isEmpty() && (profileLastName.getText().length() >= 3 && profileLastName.getText().length() <= 30)) {
                editedUser.setLastName(profileLastName.getText());

                System.out.println("Updating lastname...");
                user = editedUser;

                connection.updateUserLastName(user);
            } else {
                System.out.println("Lastname issue!");
                confirmActions.displayMessage(edit_pfp_lname_issue, "Size issue 3-30!", true);
                profileLastName.setText(connection.getUserDatabaseLastName(user.getUserId()));

                successMessage = false;
            }

            // Edit Address
            if (!profileAdress.getText().isEmpty() && (profileAdress.getText().length() >= 5 && profileAdress.getText().length() <= 60)){
                editedUser.setAddress(profileAdress.getText());

                System.out.println("Updating address...");
                user = editedUser;

                connection.updateUserAddress(user);
            } else {
                System.out.println("Address issue!");
                confirmActions.displayMessage(edit_pfp_address_issue, "Size issue 5-60!", true);
                profileAdress.setText(connection.getUserDatabaseAddress(user.getUserId()));

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
            if (!profilePhoneNumber.getText().isEmpty() && profilePhoneNumber.getText().length() == 10){
                editedUser.setPhoneNumber(profilePhoneNumber.getText());

                System.out.println("Updating phone number...");
                user = editedUser;

                connection.updateUserPhoneNumber(user);
            } else {
                System.out.println("Phone number issue!");
                confirmActions.displayMessage(edit_pfp_phone_issue, "Size issue 10!", true);
                profilePhoneNumber.setText(connection.getUserDatabasePhoneNumber(user.getUserId()));

                successMessage = false;
            }

            // Edit Password (Special design)
            if (!profileOldPassword.getText().isEmpty() || !profileNewPassword.getText().isEmpty() || !profileNewPasswordConfirm.getText().isEmpty()){

                if(connection.hashPassword(profileOldPassword.getText()).equals(connection.getUserDatabasePassword(user.getUserId()))) {

                    if(profileNewPassword.getText().length() >= 8 && profileNewPassword.getText().length() <= 20) {

                        if(profileNewPasswordConfirm.getText().equals(profileNewPassword.getText())) {
                            editedUser.setPassword(profileNewPassword.getText());

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
            profileOldPassword.setText("");
            profileNewPassword.setText("");
            profileNewPasswordConfirm.setText("");

            // Make text fields disabled
            profileFirstName.setDisable(true);
            profileLastName.setDisable(true);
            profileAdress.setDisable(true);
            profileEmail.setDisable(true);
            profilePhoneNumber.setDisable(true);
            profileOldPassword.setDisable(true);
            profileNewPassword.setDisable(true);
            profileNewPasswordConfirm.setDisable(true);
            editingProfile = false;
            btnEditProfile.setText("Edit");
            edit_pfp_cancel_btn.setDisable(true);
        }
    }

    /**
     * This method cancels editing profile.
     * @throws SQLException if any error occurs.
     * @author Sossio.
     */
    public void editProfileCancel() throws SQLException {
        // Reset to current data
        profileFirstName.setText(connection.getUserDatabaseFirstName(user.getUserId()));
        profileLastName.setText(connection.getUserDatabaseLastName(user.getUserId()));
        profileAdress.setText(connection.getUserDatabaseAddress(user.getUserId()));
        //profileEmail.setText(connection.getUserDatabaseEmail(user.getUserId()));
        profilePhoneNumber.setText(connection.getUserDatabasePhoneNumber(user.getUserId()));

        // Reset password fields
        profileOldPassword.setText("");
        profileNewPassword.setText("");
        profileNewPasswordConfirm.setText("");

        // Make text fields disabled
        profileFirstName.setDisable(true);
        profileLastName.setDisable(true);
        profileAdress.setDisable(true);
        profileEmail.setDisable(true);
        profilePhoneNumber.setDisable(true);
        profileOldPassword.setDisable(true);
        profileNewPassword.setDisable(true);
        profileNewPasswordConfirm.setDisable(true);
        editingProfile = false;
        btnEditProfile.setText("Edit");
        edit_pfp_cancel_btn.setDisable(true);
        confirmActions.displayMessage(pfp_display_msg, "Profile editing canceled!", false);
    }

    /**
     * Shows available profile images using a grid.
     * @author Kasper.
     */
    public void changeImage() {
        if (changingProfileImage == false) {
            profileSelector.setVisible(true);
            dir = new File("src/main/resources/application/profiles/64x64");
            files = dir.listFiles();
            changingProfileImage = true;

            int b = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    profileSelector.add(new ImageView(new Image(files[b].getAbsolutePath())), i, j);
                    b++;
                }
            }
        } else {
            profileSelector.setVisible(false);
            changingProfileImage = false;
        }
    }

    /**
     * Edits the profile picture of a user.
     * @param event
     * @author Kasper. Developed by Sossio.
     */
    public void clickGrid(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != profileSelector) {
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            dir = new File("src/main/resources/application/profiles/256x256");
            files = dir.listFiles();
            Image image = new Image(files[(colIndex*5)+rowIndex].getAbsolutePath());
            profilePicturePreview.setImage(image);
            profilePicture.setImage(image);
            String profilePic = (files[(colIndex*5)+rowIndex].getAbsolutePath());
            System.out.println(profilePic);
            profilePic = profilePic.substring(profilePic.indexOf("application") , profilePic.length());
            profilePic = profilePic.replace("\\","/");
            System.out.println(profilePic);

            try {
                connection.setProfilePictureIdk(profilePic, user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            changingProfileImage = false;
            profileSelector.setVisible(false);
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
        gridE.getChildren().removeAll();
        gridB.getChildren().removeAll();
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
            if (gridE.getColumnCount() == 2 && gridE.getRowCount() == 1){
                gridE.add(label, columnIndex, rowIndex);
                gridE.setMargin(label, new Insets(0, 20, 0, 0));
            } else if (gridE.getColumnCount() == 3 && gridE.getRowCount() > 1) {
                System.out.println("column 4");
                gridE.setMargin(label, new Insets(0, 0, 0, 20));
                gridE.add(label, columnIndex, rowIndex);
            }
            else {
                gridE.add(label, columnIndex, rowIndex);
            }
           //</editor-fold>
           //grid_left.getColumnCount();
           label.setOnMouseClicked(e ->{
               price_seat_pnl.setText(String.valueOf(price));

               seat_nbr_seat_pnl.setText(label.getId());
               toggleSeatColor(); // restore seats
               // seat color change
               for (int i = 0; i < gridE.getChildren().size(); i++){
                   gridE.getChildren().get(i).setOpacity(1);
                   if (!Objects.equals(gridE.getChildren().get(i).getId(), label.getId())){
                       gridE.getChildren().get(i).setOpacity(0.5);
                       for (String taken : takenSeatE){
                           if (taken.equals(gridE.getChildren().get(i).getId())){
                                gridE.getChildren().get(i).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5;");
                           }
                       }
                   }
               }
           });
        }else {

           //<editor-fold desc="short">
           Label label = createSeatItem();
           label.setId("B" + rowIndex+ columnIndex);
           if (gridB.getColumnCount() == 2 && gridB.getRowCount() == 1){
               System.out.println("column 3");
               gridB.add(label, columnIndex, rowIndex);
               gridB.setMargin(label, new Insets(0, 20, 0, 0));
           } else if (gridB.getColumnCount() == 3 && gridB.getRowCount() > 1) {
               System.out.println("column 4");
               gridB.setMargin(label, new Insets(0, 0, 0, 20));
               gridB.add(label, columnIndex, rowIndex);
           }
           else {
               gridB.add(label, columnIndex, rowIndex);
           }
           //</editor-fold>
           label.setOnMouseClicked(e ->{

               price_seat_pnl.setText(String.valueOf(price * 1.1));
               seat_nbr_seat_pnl.setText(label.getId());
               toggleSeatColor(); // restore seats


               // seat color change
               for (int i = 0; i < gridB.getChildren().size(); i++){
                   gridB.getChildren().get(i).setOpacity(1);
                   if (!Objects.equals(gridB.getChildren().get(i).getId(), label.getId())){
                       gridB.getChildren().get(i).setOpacity(0.5);
                       for (String taken : takenSeatB){
                           if (taken.equals(gridB.getChildren().get(i).getId())){
                               System.out.println("business taken seats exist");
                               gridB.getChildren().get(i).setStyle("-fx-background-color: #FF8000; -fx-background-radius: 5;");
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
        for (int ge = 0; ge < gridE.getChildren().size(); ge++){ // ge store for grid-economy
            if (!takenSeatE.contains(gridE.getChildren().get(ge).getId())){
                gridE.getChildren().get(ge).setOpacity(1);
                //gridE.getChildren().get(ge).setStyle("-fx-background-color: #AEFF47; -fx-background-radius: 5; -fx-opacity: 1;"); // restore all seats
            }
        }
        for (int gb = 0; gb < gridB.getChildren().size(); gb++){ // gb stor for grid-business
            if (!takenSeatB.contains(gridB.getChildren().get(gb).getId())){
                gridB.getChildren().get(gb).setOpacity(1);
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

    /**
     * @param e
     */
    public void purchaseHandle(ActionEvent e){
        if (e.getSource() == card_prev_btn){
            pnlPassager.toFront();
            pnlPayment.toBack();
        }else if(e.getSource() == card_purchase_btn){

            //<editor-fold desc="file">
                String nbr = card_nbr.getText();
                String name = card_fname.getText();
                String lname = card_lname.getText();
                String month = card_month.getText();
                String year = card_year.getText();
                String cvc = card_cvc.getText();
            //</editor-fold>

                if (!card_nbr.getText().isEmpty()){
                    boolean validCard = Purchase.purchaseTicket(nbr, name, lname, month, year, cvc);
                    if (validCard){
                        System.out.println("Card is valid!");
                        boolean purchaseDone1 = false;
                        boolean purchaseDone2 = false;
                        String rfc1 = "", rfc2 = "";

                        if(turSeat != null){
                            for (int i = 0; i <= 1; i++){
                                System.out.println("Loop is running...");
                                if (turSeat != null && turFlight_nbr_seat_pnl != null){ // saving tur flight
                                    System.out.println("First condition");
                                    StringBuilder rfc = connection.generateRandomRFC();
                                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                    boolean saveTicket = connection.savePurchasedTicket(user.getUserId(), turFlight_nbr_seat_pnl, String.valueOf(rfc), date, turSeat, false);
                                    if (saveTicket){
                                        rfc1 = String.valueOf(rfc);
                                        purchaseDone1 = true;
                                    }
                                    turSeat = null;
                                    turFlight_nbr_seat_pnl = null;
                                }else {
                                    System.out.println("Second condition");
                                    StringBuilder rfc = connection.generateRandomRFC();
                                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                    boolean saveTicket = connection.savePurchasedTicket(user.getUserId(), flight_nbr_seat_pnl.getText(), String.valueOf(rfc), date, seat_nbr_seat_pnl.getText(), false);
                                    if (saveTicket){
                                        purchaseDone2 = true;
                                    }else {
                                        System.out.println("Did not saved the purchase in database");
                                    }
                                }
                            }
                        }
                        else {
                            System.out.println("Second condition 2");
                            StringBuilder rfc = connection.generateRandomRFC();
                            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                            boolean saveTicket = connection.savePurchasedTicket(user.getUserId(), flight_nbr_seat_pnl.getText(), String.valueOf(rfc), date, seat_nbr_seat_pnl.getText(), false);
                            if (saveTicket){
                                purchaseDone1 = true;
                                rfc1 = String.valueOf(rfc);
                                //confirmPurchase(String.valueOf(rfc));
                            }else {
                                System.out.println("Did not saved the purchase in database");
                            }
                        }

                        if(purchaseDone1 && purchaseDone2){
                            System.out.println("Booked two-ways flights");
                            confirmPurchase(rfc2); // can send more information form here.
                        }else if(purchaseDone1){
                            System.out.println("Booked only one-way flight");
                            confirmPurchase(rfc1);
                        }


                    }else {
                        System.out.println("Card not valid");
                    }
                }

        }else if(e.getSource() == seat_next_btn){
            //<editor-fold desc="file">
                String name_s = first_name_seat_pnl.getText();
                String lname_s = last_name_seat_pnl.getText();
                String fourdigit = four_digit_seat_pnl.getText();
                String email = email_seat_pnl.getText();
                String isTur = isTur_seat_pnl.getText();
                String seat = seat_nbr_seat_pnl.getText();
            //</editor-fold>

                if (!name_s.isEmpty() && !lname_s.isEmpty() && !fourdigit.isEmpty() && !email.isEmpty() && !seat.isEmpty()){
                    if (turAndReturnFlights.size() == 1){
                        System.out.println("Active tur");
                        turSeat = seat_nbr_seat_pnl.getText();
                        turFlight_nbr_seat_pnl = flight_nbr_seat_pnl.getText();

                        flight_nbr_seat_pnl.setText(null);
                        seat_nbr_seat_pnl.setText(null);
                        price_seat_pnl.setText(null);

                        // clear the operation - preperBeforeCreatingSeats
                        boolean build = preperBeforeCreatingSeats();
                        if (build){
                            from_info_seat_pnl.setText(turAndReturnFlights.get(0).getDeparture_name());
                            to_info_seat_pnl.setText(turAndReturnFlights.get(0).getDestination_name());
                            from_d_info_seat_pnl.setText(turAndReturnFlights.get(0).getDeparture_date());
                            to_d_info_seat_pnl.setText(turAndReturnFlights.get(0).getDestination_date());
                            flight_nbr_seat_pnl.setText(turAndReturnFlights.get(0).getId());
                            price = Double.parseDouble(turAndReturnFlights.get(0).getPrice());
                            price_seat_pnl.setText(String.valueOf(price));
                            createThisSeat(turAndReturnFlights, 0);
                            turAndReturnFlights.clear();
                        }
                    } else {
                        System.out.println("To front");
                        pnlPayment.toFront();
                    }
                }else {
                    confirmActions.displayMessage(msg_seat_pnl, "Empty field issue!", true);
                }
        }else if(e.getSource() == redirect_to_dash_btn){
            updateHistoryList();
            pnlFlight.toFront();
            restore_psgr_info();
            pnl_success_purchase.toBack();
            pnlPayment.toBack();

        }
    }

    private void confirmPurchase(String rfc) {
        System.out.println(email_seat_pnl.getText() + " Your email!!!");
        if (!email_seat_pnl.getText().isEmpty()){
            boolean sentMail = Purchase.sendEmail(email_seat_pnl.getText(), first_name_seat_pnl.getText(), flight_nbr_seat_pnl.getText(), seat_nbr_seat_pnl.getText(), price_seat_pnl.getText());
            if (sentMail){
                if (turSeat != null){
                    System.out.println("Tur is reached here");
                    rfc_no_sucesspnl.setText(rfc.toString());
                    pnl_success_purchase.toFront();
                    turSeat = null;
                    System.out.println("Email successfully sent!");
                }else {
                    System.out.println("Returne has been reached here");
                    rfc_no_sucesspnl.setText(rfc.toString());
                    pnl_success_purchase.toFront();
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
        first_name_seat_pnl.clear();
        last_name_seat_pnl.clear();
        four_digit_seat_pnl.clear();
        email_seat_pnl.clear();
        seat_nbr_seat_pnl.setText(null);
        flight_nbr_seat_pnl.setText(null);
        price_seat_pnl.setText(null);
        card_nbr.clear();
        card_fname.clear();
        card_lname.clear();
        card_month.clear();
        card_year.clear();
        card_cvc.clear();
        takenSeatE.clear();
        takenSeatB.clear();
        gridE.getChildren().clear();
        gridB.getChildren().clear();
        turAndReturnFlights.clear();
        turSeat = null;
        turFlight_nbr_seat_pnl = null;

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
                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setTitle("Admin window");
                    stage.setScene(scene);
                    stage.show();


                    fillMemmbersTable(root);
                    adminControl.fillTicketTable(root);


                    memberListView = (ListView<String>) root.lookup("#memberListView");
                    if(memberListView != null)
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
                        memberListView.setItems(tickets);

                    }

                    ticketListView = (ListView<String>) root.lookup("#ticketListView");
                    if(ticketListView != null)
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
                        ticketListView.setItems(tickets);
                    }
                } else {
                    error_msg.setText("Wrong email or pass!");
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(a -> error_msg.setText(null));
                    pause.play();
                }
            }catch (IOException io){
                io.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            error_msg.setText("Fill the field!");

        }
    }

    /**
     * User even handler.
     * @param e
     */
    public void userDev(ActionEvent e){
        if (e.getSource() == iconProfile) {
            pnlProfile.toFront();
            toggleMenuColor();
        }
        else if(e.getSource() == iconCloseSeat || e.getSource() == iconCloseSeat1){
            pnlSeat.toBack();
            restore_psgr_info();
        }
        else if (e.getSource() == iconFlight) {
            pnlFlight.toFront();
            toggleMenuColor();
            lgtF_menu_user.setVisible(true);
            map_menu_user.setOpacity(1);
        }
        else if (e.getSource() == iconHistorik) {
            pnlHistorik.toFront();
            toggleMenuColor();
            lgtH_menu_user.setVisible(true);
            historik_menu_user.setOpacity(1);
        }
        else if (e.getSource() == iconGame) {
            pnlGame.toFront();
            toggleMenuColor();
            lgtG_menu_user.setVisible(true);
            game_menu_user.setOpacity(1);

        }
        else if(e.getSource() == iconSupport){
            pnlSupport.toFront();
            toggleMenuColor();
            lgtS_menu_user.setVisible(true);
            support_menu_user.setOpacity(1);
        }

        // navigating av
        else if(e.getSource() == prev_tur_date_flight){
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
                confirmActions.displayMessage(err_search_flight, "Date is not initialized!", true);
            }
        }
        else if(e.getSource() == next_tur_date_flight){
            if (date_input_flight.getValue() != null){
                date_input_flight.setValue(date_input_flight.getValue().plusDays(1));
            }else
                confirmActions.displayMessage(err_search_flight, "Date is not initialized!", true);
        }
        else if(e.getSource() == prev_rtur_date_flight){
            if (dateR_input_flight.getValue() != null){
                dateR_input_flight.setValue(dateR_input_flight.getValue().plusDays(1));
            }else
                confirmActions.displayMessage(err_search_flight, "Date is not initialized!", true);
        }
        else if(e.getSource() == next_rtur_date_flight){
            if (dateR_input_flight.getValue() != null){
                dateR_input_flight.setValue(dateR_input_flight.getValue().plusDays(1));
            }else
                confirmActions.displayMessage(err_search_flight, "Date is not initialized!", true);
        }


    }

    /**
     *
     */
    private void toggleMenuColor() {
        lgtF_menu_user.setVisible(false);
        lgtH_menu_user.setVisible(false);
        lgtG_menu_user.setVisible(false);
        lgtS_menu_user.setVisible(false);

        map_menu_user.setOpacity(0.5);
        historik_menu_user.setOpacity(0.5);
        game_menu_user.setOpacity(0.5);
        support_menu_user.setOpacity(0.5);
    }

    /**
     * Administrator dev.
     * @param e
     * @throws IOException
     * @autor Obed.
     */
    public void adminDev(ActionEvent e) throws SQLException {
        if(e.getSource() == logoutButton)
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
            pnlMember.toFront();
        }

        else if(e.getSource() == registerMemberBtn_admin)
        {
            registerAnchorPane.toFront();
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
        if (!from_input_flight.getText().isEmpty() || !disc_input_flight.getText().isEmpty()){
            String from = from_input_flight.getText();
            String to = disc_input_flight.getText();
            from_input_flight.setText(to);
            disc_input_flight.setText(from);
        }else {
            confirmActions.notifyError(msgBox_user_dashboard, notify_user_dashboard, "Search fields are empty!");
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
                if (searchListAppear != null){
                    searchListAppear.getItems().removeAll();
                }
                searchListAppear.setVisible(true);
                searchListAppear.setItems(searchAprear);
                searchListAppear.getSelectionModel().selectedItemProperty().addListener(e ->{
                    search_f_name.setText(searchListAppear.getSelectionModel().getSelectedItem());
                        searchListAppear.setVisible(false);
            });
            }
        }

        hidePopupSearch(search_f_name.getText(), searchListAppear);
    }

    /**
     * On key pressed search and show name.
     * @author Khabib.
     */
    public void departureNameAppear(){
        if (from_input_flight != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(from_input_flight.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAppear2 != null){
                    searchListAppear2.getItems().removeAll();
                }
                searchListAppear2.setVisible(true);
                searchListAppear2.setItems(searchAprear);
                searchListAppear2.getSelectionModel().selectedItemProperty().addListener(e ->{
                    from_input_flight.setText(searchListAppear2.getSelectionModel().getSelectedItem());
                        searchListAppear2.setVisible(false);
            });
            }
        }

        hidePopupSearch(from_input_flight.getText(), searchListAppear2);
    }

    /**
     * On key pressed search and show name.
     * @author Khabib.
     */
    public void destinationNameAppear(){
        if (disc_input_flight != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(disc_input_flight.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAppear3 != null){
                    searchListAppear3.getItems().removeAll();
                }
                searchListAppear3.setVisible(true);
                searchListAppear3.setItems(searchAprear);
                searchListAppear3.getSelectionModel().selectedItemProperty().addListener(e ->{
                    disc_input_flight.setText(searchListAppear3.getSelectionModel().getSelectedItem());
                        searchListAppear3.setVisible(false);
            });
            }
        }

        hidePopupSearch(disc_input_flight.getText(), searchListAppear3);
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

    //----------------- Amdin Tables  -----------------//
    public void fillMemmbersTable(Parent root) throws SQLException {
        adminControl.fillMemmbersTable(root);

    }
    public void fillTicketTable(Parent root) throws SQLException {
        adminControl.fillTicketTable(root);
    }
    //----------------- History  -----------------//

    /**
     *
     */
    public void setInfoIntoTableHistorik(){ // the method calls from user dashboard to load everything.
        sremove_btn_historik = (Button) root.lookup("#sremove_btn_historik");
        flightPathBtn = (Button) root.lookup("#flightPathBtn");
        mremove_btn_historik = (Button) root.lookup("#mremove_btn_historik");
        table_historik = (TableView<UserHistory>) root.lookup("#table_historik");
        select_all_box_historik = (CheckBox) root.lookup("#select_all_box_historik");
        table_historik.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("no_col_table_historik"));
        table_historik.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("company_col_table_historik"));
        table_historik.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("model_col_table_historik"));
        table_historik.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("rfc_col_table_historik"));
        table_historik.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("flightid_col_table_historik"));
        table_historik.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("from_col_table_historik"));
        table_historik.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("to_col_table_historik"));
        table_historik.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("seatno_col_table_historik"));
        table_historik.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("date_col_table_historik"));
        table_historik.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("price_col_table_historik"));
        table_historik.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("select_col_table_historik"));
        table_historik.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        table_historik.getSelectionModel().selectedItemProperty().addListener((ObservableList, oldValue, newValue) ->{

            if (newValue != null){
                if (newValue.getSelect_col_table_historik().isSelected()){
                    newValue.getSelect_col_table_historik().setSelected(false);
                }else
                    newValue.getSelect_col_table_historik().setSelected(true);
                System.out.println("selected item: " + newValue.getCompany_col_table_historik() + " value: " + newValue.getSelect_col_table_historik().isSelected());
             }
            sremove_btn_historik.setDisable(false);
            flightPathBtn.setDisable(false);

        });
        updateHistoryList();
        select_all_box_historik.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("selected all");
                items = table_historik.getItems();
                boolean selectedAllItems = false; // selected or not

                for (UserHistory item : items){
                    if (select_all_box_historik.isSelected()){
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
                    mremove_btn_historik.setDisable(false);

                }else {
                    mremove_btn_historik.setDisable(true);
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

        fetchedList = FXCollections.observableArrayList(list);
        table_historik.setItems(fetchedList);
    }

    /**
     * the method will handle delete option in history panel.
     * @param e event
     */
    public void userRemoveHistory(ActionEvent e){
        if (table_historik.getItems().size() > 0){ // check if there is any items before running the operation.
            if (e.getSource() == sremove_btn_historik){ // if single remove button clicked
                items = table_historik.getItems(); // get the whole tables items into an observable list to compare.
                if (items != null){ // if observable items has item
                    // show a confirmation message to user
                    boolean confirmed = confirmActions.confirmThisAction("Confirm to delete selected item", "Do you want to proceed?", "The selected items will be deleted!");
                    if (confirmed){ // if user confirm the action
                        for (UserHistory item: items){ // loop through all historic items
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
            }else if(e.getSource() == mremove_btn_historik){ // if remove all button clicked
                items = table_historik.getItems(); // get the whole tables items into an observable list to compare.
                if (items != null){ // if observable items has item
                    // show a confirmation message to user
                    boolean confirmed = confirmActions.confirmThisAction("Confirm to delete the item", "Do you want to proceed?", items.size() +" items will be deleted.");
                    if (confirmed){ // if user confirm the action
                        for (UserHistory item: items){ // loop through all historic items
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
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public Stage getStage(){return stage;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
