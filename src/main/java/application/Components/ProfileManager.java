package application.Components;

import application.Controller;
import application.model.User;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.sql.SQLException;

public class ProfileManager {

    public void editProfile(Controller controller) throws SQLException {
        if (controller.is_editing_profile == false) {
            controller.profile_cancel_btn.setDisable(false);

            controller.profile_first_name_lbl.setDisable(false);
            controller.profile_last_name_lbl.setDisable(false);
            controller.profile_address_lbl.setDisable(false);
            controller.profile_email_lbl.setDisable(true); // Let be false cause an error will occur!
            controller.profile_phone_lbl.setDisable(false);
            controller.profile_old_password_passwordfield.setDisable(false);
            controller.profile_new_password_textfield.setDisable(false);
            controller.profile_confirm_password_textfield.setDisable(false);
            controller.profile_edit_btn.setText("Confirm");
            controller.is_editing_profile = true;
        } else {

            User editedUser = controller.user;

            boolean successMessage = true;

            // Edit Firstname
            if (!controller.profile_first_name_lbl.getText().isEmpty() && (controller.profile_first_name_lbl.getText().length() >= 3 && controller.profile_first_name_lbl.getText().length() <= 30)) {
                editedUser.setFirstName(controller.profile_first_name_lbl.getText());

                System.out.println("Updating user firstname...");
                controller.user = editedUser;

                controller.connection.updateUserFirstName(controller.user);

            } else {
                System.out.println("Firstname issue!");
                controller.confirmActions.displayMessage(controller.edit_pfp_fname_issue, "Size issue 3-30!", true);
                controller.profile_first_name_lbl.setText(controller.connection.getUserDatabaseFirstName(controller.user.getUserId()));

                successMessage = false;
            }

            // Edit Lastname
            if(!controller.profile_last_name_lbl.getText().isEmpty() && (controller.profile_last_name_lbl.getText().length() >= 3 && controller.profile_last_name_lbl.getText().length() <= 30)) {
                editedUser.setLastName(controller.profile_last_name_lbl.getText());

                System.out.println("Updating lastname...");
                controller.user = editedUser;

                controller.connection.updateUserLastName(controller.user);
            } else {
                System.out.println("Lastname issue!");
                controller.confirmActions.displayMessage(controller.edit_pfp_lname_issue, "Size issue 3-30!", true);
                controller.profile_last_name_lbl.setText(controller.connection.getUserDatabaseLastName(controller.user.getUserId()));

                successMessage = false;
            }

            // Edit Address
            if (!controller.profile_address_lbl.getText().isEmpty() && (controller.profile_address_lbl.getText().length() >= 5 && controller.profile_address_lbl.getText().length() <= 60)){
                editedUser.setAddress(controller.profile_address_lbl.getText());

                System.out.println("Updating address...");
                controller.user = editedUser;

                controller.connection.updateUserAddress(controller.user);
            } else {
                System.out.println("Address issue!");
                controller.confirmActions.displayMessage(controller.edit_pfp_address_issue, "Size issue 5-60!", true);
                controller.profile_address_lbl.setText(controller.connection.getUserDatabaseAddress(controller.user.getUserId()));

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
            if (!controller.profile_phone_lbl.getText().isEmpty() && controller.profile_phone_lbl.getText().length() == 10){
                editedUser.setPhoneNumber(controller.profile_phone_lbl.getText());

                System.out.println("Updating phone number...");
                controller.user = editedUser;

                controller.connection.updateUserPhoneNumber(controller.user);
            } else {
                System.out.println("Phone number issue!");
                controller.confirmActions.displayMessage(controller.edit_pfp_phone_issue, "Size issue 10!", true);
                controller.profile_phone_lbl.setText(controller.connection.getUserDatabasePhoneNumber(controller.user.getUserId()));

                successMessage = false;
            }

            // Edit Password (Special design)controller.
            if (!controller.profile_old_password_passwordfield.getText().isEmpty() || !controller.profile_new_password_textfield.getText().isEmpty() || !controller.profile_confirm_password_textfield.getText().isEmpty()){

                if(controller.connection.hashPassword(controller.profile_old_password_passwordfield.getText()).equals(controller.connection.getUserDatabasePassword(controller.user.getUserId()))) {

                    if(controller.profile_new_password_textfield.getText().length() >= 8 && controller.profile_new_password_textfield.getText().length() <= 20) {

                        if(controller.profile_confirm_password_textfield.getText().equals(controller.profile_new_password_textfield.getText())) {
                            editedUser.setPassword(controller.profile_new_password_textfield.getText());

                            System.out.println("Updating new password...");
                            controller.user = editedUser;

                            controller.connection.updateUserPassword(controller.user);
                        } else {
                            controller.confirmActions.displayMessage(controller.edit_pfp_new_c_pwd_issue, "Mach issue!", true);
                            successMessage = false;
                        }

                    } else {
                        controller.confirmActions.displayMessage(controller.edit_pfp_new_pwd_issue, "Size issue 8-20!", true);
                        successMessage = false;
                    }

                } else {
                    controller.confirmActions.displayMessage(controller.edit_pfp_old_pwd_issue, "Wrong password!", true);
                    successMessage = false;
                }

            } else {
                //System.out.println("To change pwd, fill all!");

                //confirmActions.displayMessage(edit_pfp_old_pwd_issue, "To change pwd, fill all!", false);
            }

            if(successMessage) {
                controller.confirmActions.displayMessage(controller.pfp_display_msg, "Profile is updated!", false);
            }

            // Reset password fields
            controller.profile_old_password_passwordfield.setText("");
            controller.profile_new_password_textfield.setText("");
            controller.profile_confirm_password_textfield.setText("");

            // Make text fields disabled
            controller.profile_first_name_lbl.setDisable(true);
            controller.profile_last_name_lbl.setDisable(true);
            controller.profile_address_lbl.setDisable(true);
            controller.profile_email_lbl.setDisable(true);
            controller.profile_phone_lbl.setDisable(true);
            controller.profile_old_password_passwordfield.setDisable(true);
            controller.profile_new_password_textfield.setDisable(true);
            controller.profile_confirm_password_textfield.setDisable(true);
            controller.is_editing_profile = false;
            controller.profile_edit_btn.setText("Edit");
            controller.profile_cancel_btn.setDisable(true);
        }
    }

    public void editProfileCancel(Controller controller) throws SQLException {
        // Reset to current data
        controller.profile_first_name_lbl.setText(controller.connection.getUserDatabaseFirstName(controller.user.getUserId()));
        controller.profile_last_name_lbl.setText(controller.connection.getUserDatabaseLastName(controller.user.getUserId()));
        controller.profile_address_lbl.setText(controller.connection.getUserDatabaseAddress(controller.user.getUserId()));
        //profileEmail.setText(connection.getUserDatabaseEmail(user.getUserId()));
        controller.profile_phone_lbl.setText(controller.connection.getUserDatabasePhoneNumber(controller.user.getUserId()));

        // Reset password fields
        controller.profile_old_password_passwordfield.setText("");
        controller.profile_new_password_textfield.setText("");
        controller.profile_confirm_password_textfield.setText("");

        // Make text fields disabled
        controller.profile_first_name_lbl.setDisable(true);
        controller.profile_last_name_lbl.setDisable(true);
        controller.profile_address_lbl.setDisable(true);
        controller.profile_email_lbl.setDisable(true);
        controller.profile_phone_lbl.setDisable(true);
        controller.profile_old_password_passwordfield.setDisable(true);
        controller.profile_new_password_textfield.setDisable(true);
        controller.profile_confirm_password_textfield.setDisable(true);
        controller.is_editing_profile = false;
        controller.profile_edit_btn.setText("Edit");
        controller.profile_cancel_btn.setDisable(true);
        controller.confirmActions.displayMessage(controller.pfp_display_msg, "Profile editing canceled!", false);
    }

    public void changeImage(Controller controller) {
        if (controller.is_editing_profile_image == false) {
            controller.profile_profile_image_gridpane.setVisible(true);
            controller.profile_image_directory = new File("src/main/resources/application/profiles/64x64");
            controller.profile_image_files = controller.profile_image_directory.listFiles();
            controller.is_editing_profile_image = true;

            int b = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    controller.profile_profile_image_gridpane.add(new ImageView(new Image(controller.profile_image_files[b].getAbsolutePath())), i, j);
                    b++;
                }
            }
        } else {
            controller.profile_profile_image_gridpane.setVisible(false);
            controller.is_editing_profile_image = false;
        }
    }

    public void clickGrid(MouseEvent event, Controller controller) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != controller.profile_profile_image_gridpane) {
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            controller.profile_image_directory = new File("src/main/resources/application/profiles/256x256");
            controller.profile_image_files = controller.profile_image_directory.listFiles();
            Image image = new Image(controller.profile_image_files[(colIndex*5)+rowIndex].getAbsolutePath());
            controller.profile_image_preview_imageview.setImage(image);
            controller.profile_image_imageview.setImage(image);
            String profilePic = (controller.profile_image_files[(colIndex*5)+rowIndex].getAbsolutePath());
            System.out.println(profilePic);
            profilePic = profilePic.substring(profilePic.indexOf("application") , profilePic.length());
            profilePic = profilePic.replace("\\","/");
            System.out.println(profilePic);

            try {
                controller.connection.setProfilePictureIdk(profilePic, controller.user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            controller.is_editing_profile_image = false;
            controller.profile_profile_image_gridpane.setVisible(false);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);

            controller.confirmActions.displayMessage(controller.pfp_display_msg, "Profile image is updated!", false);
        }
    }
}
