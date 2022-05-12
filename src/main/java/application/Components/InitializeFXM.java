package application.Components;

import application.Controller;
import application.database.Connection;
import application.model.User;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.time.LocalDate;

public class InitializeFXM {
    private Controller controller;
    private Connection connection;

    public InitializeFXM(Controller controller, Connection connection){
        this.controller = controller;
        this.connection = connection;
    }

    public void initializeMusic(Parent root) {
        controller.play_button_image = (ImageView) root.lookup("#play_button_image");
    }
    public void initializeWeather(Parent root) {
        controller.lblForecastA = (Label) root.lookup("#lblForecastA");
        controller.lblForecastB = (Label) root.lookup("#lblForecastB");
        controller.lblForecastC = (Label) root.lookup("#lblForecastC");
        controller.lblForecastD = (Label) root.lookup("#lblForecastD");
        controller.lblForecastE = (Label) root.lookup("#lblForecastE");
        controller.lblForecastF = (Label) root.lookup("#lblForecastF");
        controller.weatherIcon = (ImageView) root.lookup("#weatherIcon");
        controller.weatherPane = (Pane) root.lookup("#weatherPane");
        controller.weatherPaneBase = (Pane) root.lookup("#weatherPaneBase");
        controller.weatherPaneBase.setPickOnBounds(false);
        controller.weatherPane.setVisible(false);
        controller.weatherPane.setPickOnBounds(false);
        controller.weatherMenu = false;
    }
    public void initializeProfile(Parent root, User user){
        //Profile info
        if (user != null) {
            controller.profile_image_imageview = (ImageView) root.lookup("#profile_image_imageview");
            controller.profile_image_preview_imageview = (ImageView) root.lookup("#profile_image_preview_imageview");
            controller.profile_first_name_lbl = (TextField) root.lookup("#profile_first_name_lbl");
            controller.profile_last_name_lbl = (TextField) root.lookup("#profile_last_name_lbl");
            controller.profile_email_lbl = (TextField) root.lookup("#profile_email_lbl");
            controller.profile_address_lbl = (TextField) root.lookup("#profile_address_lbl");
            controller.profile_phone_lbl = (TextField) root.lookup("#profile_phone_lbl");
            controller.profile_old_password_passwordfield = (PasswordField) root.lookup("#profile_old_password_passwordfield");
            controller.profile_new_password_textfield = (TextField) root.lookup("#profile_new_password_textfield");
            controller.profile_confirm_password_textfield = (TextField) root.lookup("#profile_confirm_password_textfield");
            controller.profile_image_preview_imageview = (ImageView) root.lookup("#profile_image_preview_imageview");
            controller.profile_profile_image_gridpane = (GridPane) root.lookup("#profile_profile_image_gridpane");
            controller.profile_edit_btn = (Button) root.lookup("#profile_edit_btn");

            controller.pfp_display_msg = (Label) root.lookup("#pfp_edit_error_msg");

            controller.profile_cancel_btn = (Button) root.lookup("#profile_cancel_btn");
            controller.profile_cancel_btn.setDisable(true);

            controller.edit_pfp_fname_issue = (Label) root.lookup("#edit_pfp_fname_issue");
            controller.edit_pfp_lname_issue = (Label) root.lookup("#edit_pfp_lname_issue");
            controller.edit_pfp_address_issue = (Label) root.lookup("#edit_pfp_address_issue");
            controller.edit_pfp_email_issue = (Label) root.lookup("#edit_pfp_email_issue");
            controller.edit_pfp_phone_issue = (Label) root.lookup("#edit_pfp_phone_issue");
            controller.edit_pfp_old_pwd_issue = (Label) root.lookup("#edit_pfp_old_pwd_issue");
            controller.edit_pfp_new_pwd_issue = (Label) root.lookup("#edit_pfp_new_pwd_issue");
            controller.edit_pfp_new_c_pwd_issue = (Label) root.lookup("#edit_pfp_new_c_pwd_issue");

            controller.profile_first_name_lbl.setText(user.getLastName()); // DO NOT EDIT, IT SHOULD BE LIKE THAT! /Sossio
            controller.profile_last_name_lbl.setText(user.getFirstName()); // DO NOT EDIT, IT SHOULD BE LIKE THAT! /Sossio
            controller.profile_email_lbl.setText(user.getEmail());
            controller.profile_address_lbl.setText(user.getAddress());
            controller.profile_phone_lbl.setText(user.getPhoneNumber());

            try {
                Image image = connection.getProfilePicture(user);
                controller.profile_image_imageview.setImage(image);
                controller.profile_image_preview_imageview.setImage(image);
                controller.profile_image_preview_imageview.setImage(connection.getProfilePicture(user));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            controller.profile_first_name_lbl.setDisable(true);
            controller.profile_last_name_lbl.setDisable(true);
            controller.profile_email_lbl.setDisable(true);
            controller.profile_address_lbl.setDisable(true);
            controller.profile_phone_lbl.setDisable(true);
            //controller.profilePassword.setDisable(true);
            controller.profile_old_password_passwordfield.setDisable(true);
            controller.profile_new_password_textfield.setDisable(true);
            controller.profile_confirm_password_textfield.setDisable(true);
            LocalDate date = LocalDate.now();
            controller.date_input_flight = (DatePicker) root.lookup("#date_input_flight");
            controller.dateR_input_flight = (DatePicker) root.lookup("#dateR_input_flight");
            controller.date_input_flight.setValue(date);
            controller.dateR_input_flight.setValue(date.plusWeeks(1));
        }
    }
}
