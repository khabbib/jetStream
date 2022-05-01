package application.Components;

import application.Controller;
import application.config.Config;
import application.database.Connection;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.sql.SQLException;

public class Registration {
    private Controller controller;
    private Config config;
    private Connection connection;
    public Registration(Controller controller, Connection connection, Config config){
        this.controller = controller;
        this.connection = connection;
        this.config = config;
    }

    public boolean registerUser(ActionEvent e) throws SQLException {
        boolean registered = false;
        if (!controller.first_name_reg.getText().isEmpty() && !controller.last_name_reg.getText().isEmpty() && !controller.address_reg.getText().isEmpty() && !controller.emailaddress_reg.getText().isEmpty() && !controller.phone_number_reg.getText().isEmpty() && !controller.password_reg.getText().isEmpty() && !controller.confirm_password_reg.getText().isEmpty()){
            if ((controller.first_name_reg.getText().length() >= 3 && controller.first_name_reg.getText().length() <= 30)){
                if ((controller.last_name_reg.getText().length() >= 3 && controller.last_name_reg.getText().length() <= 30)){
                    if ((controller.address_reg.getText().length() >= 5 && controller.address_reg.getText().length() <= 60)){
                        if((controller.emailaddress_reg.getText().length() >= 6 && controller.emailaddress_reg.getText().length() <= 30)){
                            if ((controller.phone_number_reg.getText().length() == 12)){
                                if (controller.password_reg.getText().length() >= 8 && controller.password_reg.getText().length() <= 20){
                                    if (controller.password_reg.getText().equals(controller.confirm_password_reg.getText())){
                                        if(controller.emailaddress_reg.getText().contains("@") && (controller.emailaddress_reg.getText().contains("gmail") || controller.emailaddress_reg.getText().contains("hotmail") || controller.emailaddress_reg.getText().contains("yahoo") || controller.emailaddress_reg.getText().contains("outlook"))){
                                            boolean ok = connection.saveUser(controller.first_name_reg.getText(), controller.last_name_reg.getText(), controller.address_reg.getText(), controller.emailaddress_reg.getText(), controller.phone_number_reg.getText(), controller.password_reg.getText(), false);
                                            if (ok) {
                                                registered = true;
                                                config.render(e, "user/Login", "Login window");
                                                if (controller.success_msg != null){
                                                    controller.success_msg.setText("successfully registered the user!");
                                                }else {
                                                    System.out.println("");
                                                }

                                            } else {
                                                controller.registration_error.setText("Couldn't register the information");
                                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                                pause.setOnFinished(a -> controller.registration_error.setText(null));
                                                pause.play();
                                            }
                                        }else {
                                            System.out.println("Email type issue");
                                            controller.email_issue_reg.setText("Type issue [email]");
                                        }
                                    }else {
                                        System.out.println("confirm password not much the actual password");
                                        controller.confirm_password_issue_reg.setText("Much issue [confirm password]");
                                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                        pause.setOnFinished(a -> controller.confirm_password_issue_reg.setText(null));
                                        pause.play();
                                    }
                                }else {
                                    System.out.println("password issue");
                                    controller.password_issue_reg.setText("Size issue 8-20");
                                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                    pause.setOnFinished(a -> controller.password_issue_reg.setText(null));
                                    pause.play();
                                }
                            }else {
                                controller.phone_number_issue_reg.setText("size issue 12 digit");
                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                pause.setOnFinished(a -> controller.phone_number_issue_reg.setText(null));
                                pause.play();
                            }
                        } else {
                            System.out.println("email address issue");
                            controller.email_issue_reg.setText("size issue 6-30");
                            PauseTransition pause = new PauseTransition(Duration.seconds(2));
                            pause.setOnFinished(a -> controller.email_issue_reg.setText(null));
                            pause.play();
                        }
                    }else {
                        System.out.println("address issue");
                        controller.address_issue_reg.setText("size issue 5-60");
                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(a -> controller.address_issue_reg.setText(null));
                        pause.play();
                    }
                } else {
                    System.out.println("last name issue");
                    controller.last_name_issue_reg.setText("Size issue 3-30");
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(a -> controller.last_name_issue_reg.setText(null));
                    pause.play();
                }
            } else {
                controller.name_issue_reg.setText("Size issue 3-30");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(a -> controller.name_issue_reg.setText(null));
                pause.play();
            }
        }else {
            controller.registration_error.setText("Empty field issue");
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(x -> controller.registration_error.setText(null));
            pause.play();
        }
        return registered;
    }


}
