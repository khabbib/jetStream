package application.Components;

import application.Controller;
import application.database.Connection;
import application.model.User;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

public class InitializeFXM {
    private Controller controller;
    private Connection connection;

    public InitializeFXM(Controller controller, Connection connection){
        this.controller = controller;
        this.connection = connection;
    }

    public void initializeProfile(Parent root, User user){
        //Profile info
        if (user != null) {
            controller.profilePicture = (ImageView) root.lookup("#profilePicture");
            controller.profilePicturePreview = (ImageView) root.lookup("#profilePicturePreview");
            controller.profileFirstName = (TextField) root.lookup("#profileFirstName");
            controller.profileLastName = (TextField) root.lookup("#profileLastName");
            controller.profileEmail = (TextField) root.lookup("#profileEmail");
            controller.profileAdress = (TextField) root.lookup("#profileAdress");
            controller.profilePhoneNumber = (TextField) root.lookup("#profilePhoneNumber");
            controller.profileOldPassword = (PasswordField) root.lookup("#profileOldPassword");
            controller.profileNewPassword = (PasswordField) root.lookup("#profileNewPassword");
            controller.profileNewPasswordConfirm = (PasswordField) root.lookup("#profileNewPasswordConfirm");
            controller.profilePicturePreview = (ImageView) root.lookup("#profilePicturePreview");
            controller.profileSelector = (GridPane) root.lookup("#profileSelector");
            controller.btnEditProfile = (Button) root.lookup("#btnEditProfile");

            controller.pfp_display_msg = (Label) root.lookup("#pfp_edit_error_msg");

            controller.edit_pfp_fname_issue = (Label) root.lookup("#edit_pfp_fname_issue");
            controller.edit_pfp_lname_issue = (Label) root.lookup("#edit_pfp_lname_issue");
            controller.edit_pfp_address_issue = (Label) root.lookup("#edit_pfp_address_issue");
            controller.edit_pfp_email_issue = (Label) root.lookup("#edit_pfp_email_issue");
            controller.edit_pfp_phone_issue = (Label) root.lookup("#edit_pfp_phone_issue");
            controller.edit_pfp_old_pwd_issue = (Label) root.lookup("#edit_pfp_old_pwd_issue");
            controller.edit_pfp_new_pwd_issue = (Label) root.lookup("#edit_pfp_new_pwd_issue");
            controller.edit_pfp_new_c_pwd_issue = (Label) root.lookup("#edit_pfp_new_c_pwd_issue");

            controller.profileFirstName.setText(user.getLastName()); // DO NOT EDIT, IT SHOULD BE LIKE THAT! /Sossio
            controller.profileLastName.setText(user.getFirstName()); // DO NOT EDIT, IT SHOULD BE LIKE THAT! /Sossio
            controller.profileEmail.setText(user.getEmail());
            controller.profileAdress.setText(user.getAddress());
            controller.profilePhoneNumber.setText(user.getPhoneNumber());

            try {
                Image image = connection.getProfilePicture(user);
                controller.profilePicture.setImage(image);
                controller.profilePicturePreview.setImage(image);
                controller.profilePicturePreview.setImage(connection.getProfilePicture(user));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            controller.profileFirstName.setDisable(true);
            controller.profileLastName.setDisable(true);
            controller.profileEmail.setDisable(true);
            controller.profileAdress.setDisable(true);
            controller.profilePhoneNumber.setDisable(true);
                //controller.profilePassword.setDisable(true);
                controller.profileOldPassword.setDisable(true);
                controller.profileNewPassword.setDisable(true);
                controller.profileNewPasswordConfirm.setDisable(true);
        }
    }
}
