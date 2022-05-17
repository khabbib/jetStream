package application.components.user;

import application.Controller;
import application.api.Db;
import application.components.flight.CreateWorld;
import application.components.initialize.InitializeFXM;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * This class handles user dashboard action and initialization of variables.
 */
public class UserControl {
    private Controller controller;
    private Parent root;
    private Db db;
    private InitializeFXM initializeFXM;

    /**
     * Class constructor.
     * @param controller connects to all variables and methods.
     * @param root gets parent root.
     * @param db connects to database.
     */
    public UserControl(Controller controller, Parent root, Db db){
        this.controller = controller;
        this.root = root;
        this.db = db;
        initializeFXM = new InitializeFXM(controller, db);
    }

    /**
     * This method shows which menu is active on user dashboard.
     * @author Khabib.
     */
    public void toggleMenuColor(Controller controller) {
        controller.menu_highlight_color_flight.setVisible(false);
        controller.menu_highlight_color_history.setVisible(false);
        controller.menu_highlight_color_entertainment.setVisible(false);
        controller.menu_highlight_color_support.setVisible(false);
        controller.menu_highlight_color_ceo.setVisible(false);
        controller.menu_highlight_color_my_ticket.setVisible(false);

        controller.map_menu_user_image.setOpacity(0.5);
        controller.history_menu_user_image.setOpacity(0.5);
        controller.entertainment_menu_user_image.setOpacity(0.5);
        controller.support_menu_user_image.setOpacity(0.5);
        controller.ceo_menu_user_image.setOpacity(0.5);
        controller.my_tickets_menu_user_image.setOpacity(0.5);
    }

    /**
     * This method will switch the user to the dashboard page.
     * Navigate to dashboard pages.
     * @param e takes an event listener.
     * @author Khabib. Developed by Sossio.
     */
    public void switchToUserDashboard(ActionEvent e, Controller controller) {
        if (!controller.login_email.getText().isEmpty() || !controller.login_pass.getText().isEmpty()) {
            if (controller.login_email.getText().contains("@") && (controller.login_email.getText().contains("gmail") || controller.login_email.getText().contains("hotmail") || controller.login_email.getText().contains("yahoo") || controller.login_email.getText().contains("outlook"))) {
                User user = controller.db.authenticationUser(controller.login_email.getText(), controller.login_pass.getText());
                if (user != null) {
                    controller.renderDashboard(e, user);
                    controller.login_loader_flight.setVisible(true); // set loader to true
                    controller.playSound("Login", "sounds/login.wav");
                    try {
                        controller.profile_image_preview_imageview.setImage(controller.db.getProfilePicture(user));
                    } catch (SQLException ei) {
                        ei.printStackTrace();
                    }
                } else {
                    controller.errorHandler.displayMessage(controller.error_message_lbl, "Wrong email or password!", true);
                }
            } else {
                controller.errorHandler.displayMessage(controller.error_message_lbl, "Email has wrong format!", true);
            }
        } else {
            controller.errorHandler.displayMessage(controller.error_message_lbl, "Email or password is empty, please fill in fields!", true);
        }
    }

    /**
     * The method will render dashboard page for user.
     * @param e handles an event.
     * @param user is user.
     * @param controller connects all methods and variables.
     * @author Khabib. Developed by Kasper.
     */
    public void renderDashboard(ActionEvent e, User user, Controller controller) {
        controller.user = user;
        controller.root = controller.config.render(e,"user/Dashboard", "User Dashboard");
        controller.initializeFXM.userInitializeFXML(controller.root, user);
        controller.initializeFXM.initializeProfile(controller.root, user);
        controller.initializeFXM.initializeWeather(controller.root);
        controller.initializeFXM.initializeMusic(controller.root);
        controller.weatherPaneBase.setClip(new Rectangle(186, 334));
        controller.create_world = new CreateWorld();
        controller.world_map = controller.create_world.init(controller, controller.db);
        controller.create_world.addWorldInMap(controller.world_map_scrollpane, user);
        controller.world_map_scrollpane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        controller.world_map.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        controller.setInfoIntoTableHistorik();
        controller.exploreMode = false;
    }

    /**
     * This method is a shortcut to login user dashboard.
     * @param e is event action.
     * @param controller connects to all variables and methods.
     * @throws IOException if any IO exceptions occurs.
     * @author Khabib. Developed by Sossio
     */
    public void noLoginRequired(ActionEvent e, Controller controller) throws IOException {
        controller.root = controller.config.render(e,"user/Dashboard", "User Dashboard");
        initializeFXM.userInitializeFXML(controller.root, null);
        User user = new User("1", "E", "E", "E", "E", "E", "E", false, 1);
        controller.initializeFXM.userInitializeFXML(controller.root, user);
        controller.initializeFXM.initializeProfile(controller.root, user);
        controller.initializeFXM.initializeWeather(controller.root);
        controller.initializeFXM.initializeMusic(controller.root);
        controller.weatherPaneBase.setClip(new Rectangle(186, 334));
        controller.create_world = new CreateWorld();
        controller.world_map = controller.create_world.init(controller, controller.db);
        controller.create_world.addWorldInMap(controller.world_map_scrollpane, user);
        controller.world_map_scrollpane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");
        controller.world_map.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color:  #0E0E1B;");

        if(!controller.exploreMode) {
            controller.setInfoIntoTableHistorik();
        }

        controller.exploreMode = true;
    }
}
