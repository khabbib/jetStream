package application;
import application.api.WeatherAPI;
import application.components.admin.AdminControl;
import application.components.registration.RegisterAdmin;
import application.components.flight.*;
import application.components.initialize.InitializeFXM;
import application.components.user.*;
import application.components.login.ShowPasswordField;
import application.components.reservation.Book;
import application.components.ticket.PurchaseHandler;
import application.components.ticket.UserHistory;
import application.components.registration.RegistrationUser;
import application.config.Config;
import application.eventHandler.AdminEvent;
import application.eventHandler.UserEvent;
import application.games.*;
import application.api.Db;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import worldMapAPI.World;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is main class which connects all components, methods and variables together with FXML.
 */
public class Controller implements Initializable {

    //<editor-fold desc="========= GLOBAL VARIABLES =========" >
    //<editor-fold desc="======== LOGIN WINDOW VARIABLES ========">
    @FXML public TextField login_pass, login_email, show_password_field_login;
    @FXML public Label error_message_lbl, success_msg_lbl; // ERRORS HANDLER
    @FXML private Button forgot_password_login;
    @FXML public CheckBox show_pasword_login;
    public static boolean exploreMode = true; // If explore mode is off, it means user is logged in.

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="========= ADMIN VARIABLES =========">
    //<editor-fold desc="======== REGISTER WINDOW VARIABLES (admin) ========">
    @FXML public CheckBox       is_admin_checkbox;
    @FXML public TextField      first_name_reg_admin, last_name_reg_admin, address_reg_admin, emailaddress_reg_admin, phone_number_reg_admin;
    @FXML public PasswordField  password_reg_admin, confirm_password_reg_admin;
    @FXML public Label          name_issue_reg_admin,last_name_issue_reg_admin, address_issue_reg_admin, email_issue_reg_admin, phone_number_issue_reg_admin,
                                password_issue_reg_admin, confirm_password_issue_reg_admin, registration_error_admin;
    //</editor-fold>
    //<editor-fold desc="======== DASHBOARD VARIABLES ========">
    @FXML public AnchorPane     admin_flights_anchorpane, admin_tickets_anchorpane, admin_members_anchorpane, admin_register_anchorpane;
    @FXML public Button         admin_flights_button, admin_members_button, admin_tickets_button, admin_logout_button, registerCommitBtn_admin,
                                registerMemberBtn_admin, returnToMemberListBtn_admin, refreshMembersBtn_admin, search_input_flight_admin,
                                prev_tur_date_flight_admin, next_tur_date_flight_admin;
    //</editor-fold>
    //<editor-fold desc="======== TABLE VARIABLES ========">

    // ticket table variables
    public ObservableList<UserHistory> fetchedList_ticket_admin, items_ticket_admin;
    @FXML public Button addTicketBtn_ticket_admin, deleteTicketBtn_ticket_admin, refreshTicketsBtn_ticket_admin;
    @FXML public TableView<UserHistory> table_tickets;
    @FXML public CheckBox select_col_ticket_admin;

    // members table variables
    public ObservableList<User> fetchedList_member_admin, items_member_admin;
    @FXML public Button delet_btn_mbr_admin, deletS_btn_mbr_admin;
    @FXML public TableView<User> table_member_admin;
    @FXML public CheckBox select_col_mbr_admin;

    //flights table variables
    public ObservableList<Flight> fetchedList_flight_admin, items_flight_admin;
    @FXML public Button delete_singelFlightBtn_admin, delete_allFlightsBtn_admin, refreshFlightsBtn_admin, addFlightsBtn_admin;
    @FXML public TableView<Flight> table_flight_admin;
    @FXML public CheckBox select_all_box_flight_admin;
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="========= USER DASHBOARD VARIABLES =========" >
    //<editor-fold desc="========= DEFAULT VARIABLES =========" >
    public static boolean is_editing_profile_image = false;
    public static boolean is_editing_profile;
    public File profile_image_directory;
    public File[] profile_image_files;
    public CreateWorld create_world;
    public static World world_map;
    public Stage main_stage;
    public Scene main_scene;
    public static User user;
    public Parent root;
    //public String rfc;
    public String rfc = String.valueOf(new AtomicInteger());

    //</editor-fold>
    //<editor-fold desc="========= FXML VARIABLES =========" >

    // LOADER ICON
    @FXML public ImageView login_loader_flight;

    // ERROR
    @FXML public Label err_advanced_search;
    @FXML public ScrollPane world_map_scrollpane, flights_scrollpane;
    @FXML public Label username_lbl;
    @FXML public VBox flight_display_vbox;

    // ===== PROFILE EDITING =====
    // General
    @FXML public ImageView profile_image_imageview, profile_image_preview_imageview;
    @FXML public TextField profile_first_name_lbl, profile_last_name_lbl, profile_email_lbl, profile_address_lbl,
                           profile_phone_lbl, profile_new_password_textfield, profile_confirm_password_textfield;
    @FXML public Button profile_cancel_btn, profile_edit_btn;
    @FXML public GridPane profile_profile_image_gridpane;
    @FXML public PasswordField profile_old_password_passwordfield;

    // PROFILES
    @FXML public Label edit_pfp_fname_issue, edit_pfp_lname_issue, edit_pfp_address_issue, edit_pfp_email_issue,
                       edit_pfp_phone_issue, edit_pfp_old_pwd_issue, edit_pfp_new_pwd_issue, edit_pfp_new_c_pwd_issue, pfp_display_msg;


    //  MENU BUTTONS ETC.
    @FXML
    public Button menu_profile_btn, menu_flight_btn, menu_history_btn, menu_entertainment_btn, menu_support_btn,
                  menu_ceo_btn, menu_my_tickets_btn, booking_close_btn, booking_close_second_page_btn;
    @FXML public AnchorPane profile_anchorpane, history_anchorpane, flight_anchorpane, entertainment_anchorpane, support_anchorpane, ceo_anchorpane, my_ticket_anchorpane;
    @FXML public Pane menu_highlight_color_support, menu_highlight_color_ceo, menu_highlight_color_my_ticket, menu_highlight_color_flight,
                      menu_highlight_color_history, menu_highlight_color_entertainment;

    //</editor-fold>
    //<editor-fold desc="========= WEATHER VARIABLES =========" >
    @FXML public static Label lblForecastA,lblForecastB,lblForecastC,lblForecastD,lblForecastE,lblForecastF;
    @FXML public static ImageView weatherIcon, play_button_image;
    @FXML public static Pane weatherPane, weatherPaneBase;
    public static boolean weatherMenu;
    //</editor-fold>
    //<editor-fold desc="======== TICKET-PROCESS VARIABLES ========">
    @FXML public AnchorPane success_purchase_anchorpane, booking_seat_anchorpane, booking_passenger_anchorpane,
            flight_seats_eco_anchorpane, flights_seats_business_anchorpane, payment_anchorpane;
    @FXML public TextField  card_nbr, card_fname, card_lname, card_month, card_year, card_cvc;
    @FXML public Button     card_prev_btn, card_purchase_btn, seat_next_btn, success_purchase_to_dashboard_button, print_ticket_purchase_btn;     // FINAL BUTTONS AFTER PURCHASING THE TICKET
    @FXML public Label      card_counter_nbr, payment_err_msg;
    @FXML public ScrollPane business_seat_scrollpane, eco_seat_scrollpane;
    @FXML public TextField  booking_last_name_textfield, booking_first_name_textfield, booking_four_digits_textfield, booking_email_textfield;
    @FXML public Label      booking_seat_number_lbl, booking_msg_lbl, booking_flight_number_lbl, booking_retur_seat_number_lbl,
            booking_price_lbl, booking_departure_lbl, booking_destination_lbl,
            booking_departure_extra_lbl, booking_destination_extra_lbl, booking_is_retur_lbl;
    @FXML public VBox       booking_info_vbox;
    @FXML public ImageView  support_menu_user_image, ceo_menu_user_image, my_tickets_menu_user_image,
            booking_profile_image, map_menu_user_image, history_menu_user_image, entertainment_menu_user_image;
    public ArrayList<String> taken_seat_business = new ArrayList<>();
    public ArrayList<String> taken_seat_economy = new ArrayList<>();
    public ArrayList<Flight> round_trip_flights = new ArrayList<>();
    public final GridPane economy_seat_gridpane = new GridPane();
    public final GridPane business_seat_gridpane = new GridPane();
    public String departure_seat, return_seat;
    public boolean has_return_flight = false;
    public double seat_price = 0.0;

    //</editor-fold>
    //<editor-fold desc="======== HISTORY VARIABLES ========">
    @FXML private Button history_multiple_delete_button;
    @FXML private Button history_single_delete_button;
    @FXML private Button history_display_flight_path_button;
    @FXML
    public Button detailes_btn_histroy;
    @FXML public TableColumn<Book, String> from_col_table_historik,to_col_table_historik;
    ObservableList<UserHistory> history_items_list, history_flights;
    @FXML public TableView<UserHistory> history_tableview;
    @FXML private CheckBox history_select_all_checkbox;
    @FXML public Label history_reference_number_lbl, rfc_smp_history;

    //</editor-fold
    //<editor-fold desc="======== SEARCH VARIABLES ========">
    @FXML public Button date_previous_day_button, date_next_day_button, date_previous_day_return_button, date_next_day_return_button;
    @FXML public ListView<String> search_list_appear, search_list_appear_second, search_list_appear_third;
    @FXML public TextField from_input_flight_textfield, display_input_flights, search_f_name;
    @FXML public Label nbr_of_available_flights, search_flight_error_lbl;
    public ArrayList<Flight> available_flights_list = new ArrayList<>();
    @FXML public DatePicker date_input_flight,dateR_input_flight;
    @FXML public CheckBox round_trip_checkbox;
    @FXML public HBox return_date_pick_hbox;
    //</editor-fold>
    //<editor-fold desc="======== REGISTER WINDOW VARIABLES ========">
    @FXML public TextField  registration_first_name, registration_last_name, registration_address, registration_email,
            registration_phone_number, registration_password, registration_confirm_password;
    @FXML public Label      name_issue_reg, last_name_issue_reg, address_issue_reg, email_issue_reg, phone_number_issue_reg,
            password_issue_reg, confirm_password_issue_reg, registration_error_lbl;
    //</editor-fold>
    //<editor-fold desc="======== SUPPORT WINDOW VARIABLES ========">
    @FXML public Button issue_btn_sup, feedback_btn_sup, contact_btn_sup, send_fb_btn_sup, send_issue_btn_sup, send_contact_btn_sup;
    @FXML public TextField subject_fb_txt_sup, email_fb_txt_sup, subject_contact_txt_sup, email_contact_txt_sup, title_issue_txt_sup,
            email_issue_txt_sup;
    @FXML public TextArea msg_issue_txt_sup, msg_fb_txt_sup, msg_contact_txt_sup;
    @FXML public Label sup_report_display_msg, sup_contact_display_msg, sup_feedback_display_msg;
    @FXML public AnchorPane issue_panel_sup, contact_panel_sup, feedback_panel_sup;
    //</editor-fold
    //<editor-fold desc="======== SUPPORT WINDOW VARIABLES ========">
    @FXML public Label from_myticket, to_myticket, seat_myticket,err_msg_myticket,
            rfc_muticket, flightno_myticket, terminal_myticket, dep_date_myticket,
            dep_time_myticket, des_date_myticket, des_time_myticket, airline_myticket, ttl_price_myticket;
    @FXML public Button checka_btn_myticket, cancel_btn_my_ticket, returnD_btn_checking, returnT_btn_checking;
    @FXML public AnchorPane checkning_pnl;

    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="========= INSTANCES OF CLASSES =========" >

    public FlightsViewManager flightsViewManager;
    public RegistrationUser registrationUser;
    public PurchaseHandler purchaseHandler;
    public ProfileManager profileManager;
    public RegisterAdmin registerAdmin;
    public InitializeFXM initializeFXM;
    public ShowPasswordField password;
    public AdminControl adminControl;
    public ErrorHandler errorHandler;
    public UserControl userControl;
    public FlightPaths flightPaths;
    public AdminEvent adminEvent;
    public WeatherAPI weatherAPI;
    public UserEvent userEvent;
    public BgMusic bgMusic;
    public Support support;
    public Config config;
    public Search search;
    public Db db;
    //</editor-fold>

    //----------------- HOME -----------------//
    public Controller(){
        db = new Db(this);
        config = new Config(this, root, main_stage);
        support = new Support(this);
        errorHandler = new ErrorHandler(this);
        search = new Search(this, db, errorHandler);
        registrationUser = new RegistrationUser(this, db, config);
        registerAdmin = new RegisterAdmin(this, db, config);
        userControl = new UserControl(this, root, db);
        initializeFXM = new InitializeFXM(this, db);
        flightPaths = new FlightPaths(this);
        weatherAPI = new WeatherAPI();
        profileManager = new ProfileManager();
        password = new ShowPasswordField();
        adminControl = new AdminControl(this, db);
        userEvent = new UserEvent(this);
        adminEvent = new AdminEvent();
        purchaseHandler = new PurchaseHandler();
        flightsViewManager = new FlightsViewManager(this);
        bgMusic = new BgMusic(this);
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
        weatherAPI.setInformation(this,country);
    }

    public void weatherButton() {
       weatherAPI.weatherMenu(this);
    }


    /**
     * Login operation (show password )
     * @param e
     * @author Khabib.
     */
    public void showPassword(ActionEvent e){
        password.showPassword(e,this);
    }

    /**
     * @author Khabib.
     */
    public void syncPasswordShow(){
       password.syncPasswordShow(this);
    }

    public void stopMusic(){
        System.out.println("hello");
    }

    public void playPong(){
        Pong pong = new Pong();
        try { Stage primary = new Stage(); pong.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playGeoQuiz(){
        Geography geography = new Geography();
        try { Stage primary = new Stage(); geography.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playQuiz(){
        MPlayer mPlayer = new MPlayer();
        try { Stage primary = new Stage(); mPlayer.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playPiano(){
        Piano piano = new Piano();
        try { Stage primary = new Stage(); piano.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void play2048(){
        Game2048Main game2048Main = new Game2048Main();
        try { Stage primary = new Stage(); game2048Main.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method plays sound on button actions.
     * @param soundName takes the name of the sound as a string to print in concole.
     * @param src is file path name.
     * @author Sossio.
     */
    public void playSound(String soundName, String src) {
        Media buzzer = new Media(getClass().getResource(src).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(buzzer);
        mediaPlayer.play();
        System.out.println("'" + soundName + "' fx played!");
    }

    /**
     * the method will switch the user to the dashboard page
     * navigate to dashboard pages
     * @param e
     * @throws IOException
     */
    public void switchToUserDashboard(ActionEvent e) throws IOException {
        exploreMode = false;
        userControl.switchToUserDashboard(e,this);
    }

    /**
     * Resets the text in textfield from-to.
     * @author Sossio.
     */
    public void resetSearchFromTo() {
        flightsViewManager.resetSearchFromTo(this);
    }

    /**
     * Resets the text in textfield country name.
     * @author Sossio.
     */
    public void resetSearchCountry() {
        flightsViewManager.resetSearchCountry(this);
    }

    /**
     * @param e
     * @param user
     * @throws IOException
     */
    public void renderDashboard(ActionEvent e, User user) {
        userControl.renderDashboard(e, user,this);
    } // the method will render dashboard page for user

    /**
     * @param e
     * @throws IOException
     */
    public void noLoginRequired(ActionEvent e) throws IOException {
        exploreMode = true;
        userControl.noLoginRequired(e,this);
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
    public void registerUserButton(ActionEvent e) {
        registrationUser.registerUserBtnAction(e,this);
    }

    /**
     * @param e
     * @throws SQLException
     */
    public void registerUserAdminBtnAction(ActionEvent e) throws SQLException {
        registerAdmin.registerUserAdminBtnAction(e,this);
    }

    /**
     * the method will switch the user to the login page
     * @param e
     * @throws IOException
     */
    public void switchToLogin(ActionEvent e) {
        exploreMode = true;
        this.root = config.render(e, "user/Login", "Login");
        success_msg_lbl = (Label) root.lookup("#success_msg_lbl");
        if(user != null) {
            playSound("Logout", "sounds/logout.wav");
        } else {
            user = null;
        }
    }

    /**
     * flight lists dashboard
     * @param flights
     */
    public void fillFlights (ArrayList <Flight> flights) {
        flightsViewManager.fillFlights(flights,this);
    } // the method will show the flights list on the right side of the dashboard when a user choose a country

    public void createThisSeat(ArrayList<Flight> flights, int finalI1) {
        flightsViewManager.createThisSeat(flights,finalI1,this);
    }

    public boolean preperBeforeCreatingSeats() {
        return flightsViewManager.preperBeforeCreatingSeats(this);
    }

    // check if there is any flight for searched name
    public boolean checkFlightExistance(ArrayList<Flight> flights) {
        return flightsViewManager.checkFlightExistance(flights,this);
    }

    // create content of the flights list
    public HBox createFlightsContent(ArrayList<Flight> flights, int i) {
        return flightsViewManager.createFlightsContent(flights,i);
    }

    // fill information to seat pnl.
    public void fillInfoSeatPnl(ArrayList<Flight> flights, int finalI1) {
        flightsViewManager.fillInfoSeatPnl(flights,finalI1,this);
    }

    /**
     * create seats
     * @param econonySeats
     * @param businessSeats
     */
    public boolean chooseSeat(int econonySeats, int businessSeats) throws InterruptedException {
        return flightsViewManager.chooseSeat(econonySeats,businessSeats,this);
    }// the method will show the chosen seat on the screen

    /**
     * @param columnIndex
     * @param rowIndex
     * @param business
     */
    public void build_eco_seats(int rowIndex, int columnIndex, boolean business) {
        flightsViewManager.build_eco_seats(rowIndex,columnIndex,business,this);
    }

    /**
     *
     */
    public void toggleSeatColor() {
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


    /**
     * This method edits profile information to later save in the database.
     * @throws SQLException if any error occurs.
     * @author Kasper. Huge modified and developed by Sossio.
     */
    public void editProfile() throws SQLException {
        profileManager.editProfile(this);
    }

    /**
     * This method cancels editing profile.
     * @throws SQLException if any error occurs.
     * @author Sossio.
     */
    public void editProfileCancel() throws SQLException {
        profileManager.editProfileCancel(this);
    }

    /**
     * Shows available profile images using a grid.
     * @author Kasper.
     */
    public void changeImage() {
        profileManager.changeImage(this);
    }

    /**
     * Edits the profile picture of a user.
     * @param event
     * @author Kasper. Developed by Sossio.
     */
    public void clickGrid(MouseEvent event) {
        profileManager.clickGrid(event,this);
    }

    public void mediaHandler(ActionEvent e) {
        bgMusic.mediaHandler(e);
    }

    /**
     * @param e
     * @author Khabib. Developed by Sossio.
     */
    public void purchaseHandle(ActionEvent e){
        purchaseHandler.purchaseHandle(e,this);
    }

    public void confirmPurchase(String rfc) {
        purchaseHandler.confirmPurchase(rfc,this);
    }

    /**
     *
     */
    public void restore_psgr_info(){
        purchaseHandler.restore_psgr_info(this);
    }

    /**
     * Navigate to admin pages.
     * @param e
     * @author Obed.
     */
    public void switchToAdminView(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin/AdminView.fxml")));
        adminControl.switchToAdminView(e, this);
    }

    /**
     * User dashboard event handler.
     * @param e
     */
    public void userDashboardEventHandler(ActionEvent e) throws ParseException {
       userEvent.userDashboardEventHandler(e,this);
    }

    /**
     *
     */
    public void toggleMenuColor() {
        userControl.toggleMenuColor(this);
    }

    /**
     * Administrator dev.
     * @param e
     * @throws IOException
     * @autor Obed.
     */
    public void adminDashboardEventHandler(ActionEvent e) throws SQLException {
        adminEvent.adminDashboardEventHandler(e,this);
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

        if (from_input_flight_textfield != null || display_input_flights != null ){
            String from = from_input_flight_textfield.getText();
            String to = display_input_flights.getText();
            from_input_flight_textfield.setText(to);
            display_input_flights.setText(from);
        }else {
            errorHandler.displayMessage(err_advanced_search, "Search fields are empty!", true);
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
            System.out.println("Typed ===== " + from_input_flight_textfield.getText());
            if (!searchAprear.isEmpty()){
                System.out.println(searchAprear.get(0) + " appeared search");
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
        detailes_btn_histroy = (Button) root.lookup("#detailes_btn_histroy");
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
        history_tableview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        history_tableview.getSelectionModel().selectedItemProperty().addListener((ObservableList, oldValue, newValue) ->{
            if (newValue != null){
                if (newValue.getSelect_col_table_historik().isSelected()){
                    newValue.getSelect_col_table_historik().setSelected(false);
                    history_display_flight_path_button.setDisable(true);
                    history_single_delete_button.setDisable(true);
                    detailes_btn_histroy.setDisable(true);
                    rfc_smp_history.setText(null);
                }if (!newValue.getSelect_col_table_historik().isSelected()){
                newValue.getSelect_col_table_historik().setSelected(true);
                history_display_flight_path_button.setDisable(false);
                history_single_delete_button.setDisable(false);
                detailes_btn_histroy.setDisable(false);
                //rfc = newValue.getRfc_col_table_historik();
                rfc_smp_history.setText(newValue.getRfc_col_table_historik());
                System.out.println("true");
                }
                if (rfc_smp_history == null){
                    System.out.println("Rfc is null");
                    history_display_flight_path_button.setDisable(false);
                    history_single_delete_button.setDisable(false);
                    detailes_btn_histroy.setDisable(false);
                }
            }
        });
        updateDashboardInfo();
        history_select_all_checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                history_flights = history_tableview.getItems();
                boolean selectedAllItems = false; // selected or not

                for (UserHistory item : history_flights){
                    if (history_select_all_checkbox.isSelected()){
                        selectedAllItems = true;
                        item.getSelect_col_table_historik().setSelected(true);
                    }else {
                        selectedAllItems = false;
                        item.getSelect_col_table_historik().setSelected(false);
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

    public void pickTicketForDetailes(){
        System.out.println("rfc: " + rfc_smp_history.getText());
        ArrayList<UserHistory> list = db.searchDataForTableHistory( -1, rfc_smp_history.getText(), false);
        if (list.size() >0){
            userControl.fillMyTicket(list);
            my_ticket_anchorpane.toFront();
        }
    }

    /**
     * separated method to use multiple times
     * it will update the historic table in user dashboard everytime an action happen or user want to navigate to the panel etc.
     */
    public void updateDashboardInfo(){
        ArrayList<UserHistory> list = db.searchDataForTableHistory(Integer.parseInt(user.getUserId()), null , false);
        history_items_list = FXCollections.observableArrayList(list);
        userControl.fillMyTicket(list);
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
                    boolean confirmed = errorHandler.confirmThisAction("Confirm to delete selected item", "Do you want to proceed?", "The selected items will be deleted!");
                    if (confirmed){ // if user confirm the action
                        for (UserHistory item: history_flights){ // loop through all historic items
                            if (item.getSelect_col_table_historik().isSelected()){ // check if the checkbox for one or more item is selected
                                boolean ok = db.deleteHistoryByRFC(item.getRfc_col_table_historik()); // send the actual reference number as an argument to database to compare and delete
                                if (ok){ // if database succeed to delete the item runs this statement
                                    updateDashboardInfo(); // historic table updates
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
                    boolean confirmed = errorHandler.confirmThisAction("Confirm to delete the item", "Do you want to proceed?", history_flights.size() +" items will be deleted.");
                    if (confirmed){ // if user confirm the action
                        for (UserHistory item: history_flights){ // loop through all historic items
                            boolean ok = db.deleteHistoryByRFC(item.getRfc_col_table_historik()); // send the actual reference number as an argument to database to compare and delete
                            if (ok){ // if database succeed to delete the item runs this statement
                                updateDashboardInfo(); // historic table updates
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

    /**
     * This metod deletes member from database if deletebutton is selected
     * @param e
     * @throws SQLException
     * @author Obed
     */
    public void removemember_admin(ActionEvent e) throws SQLException {
        if (table_member_admin.getItems().size() > 0) { // check if there is any items before running the operation.
            if (e.getSource() == deletS_btn_mbr_admin) { // if single remove button clicked
                items_member_admin = table_member_admin.getItems(); // get the whole tables items into an observable list to compare.
                if (items_member_admin != null) { // if observable items has item
                    // show a confirmation message to user
                    boolean confirmed = errorHandler.confirmThisAction("Confirm to delete selected item", "Do you want to proceed?", "The selected items will be deleted!");
                    if (confirmed) { // if user confirm the action
                        for (User item : items_member_admin) { // loop through all historic items
                            if (item.getBox().isSelected()) { // check if the checkbox for one or more item is selected
                                boolean ok = db.deleteMember(item.getUserId()); // send the actual reference number as an argument to database to compare and delete
                                if (ok) { // if database succeed to delete the item runs this statement
                                    adminControl.updateMemberTable(); // historic table updates
                                    System.out.println("Item has been deleted successfully!"); // show a success message for user
                                }
                            }
                        }
                    } else { // if user not confirm the action
                        System.out.println("not deleted screen message");
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