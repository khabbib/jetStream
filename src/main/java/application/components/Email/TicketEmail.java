package application.components.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * This class made for sending email to every user who books a ticket.
 */
public class TicketEmail {



    /**
     * This method send email to the customer with necessary information about the purchased ticket.
     * @param receiver email address to receiver (Customer) : string
     * @param user user's name : string
     * @param flightnbr flight's number : string
     * @param seatNbr seat's number : string
     * @param price ticket's price : string
     * @return return status of email
     * @author Khabib.
     */
    public static boolean send(String receiver, String user, String flightnbr, String seatNbr, String price) {
        String sender = "jetstream.oksh@gmail.com";
        String password = "jetstreamemail";
        String template = buildTemplate(user,flightnbr, seatNbr, price);
        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", 587);
            prop.put("mail.transport.protocol", "smtp");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress("jetstream@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));


            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(template);

            Multipart multipart = new MimeMultipart();
            mimeBodyPart.setContent(template, "text/html; charset=utf-8");

            MimeBodyPart img = new MimeBodyPart();
            img.attachFile(new File("src/main/resources/application/image/prank.png"));

            MimeBodyPart html = new MimeBodyPart();
            html.attachFile(new File("src/main/resources/application/template/Email.html"));

            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(html);
            multipart.addBodyPart(img);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent!");

            return true;

        } catch (MessagingException | IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }


    /**
     * Template for send
     * @param user user's name : string
     * @param flightnbr flight number : string
     * @param seatnbr seat number : string
     * @param price price of ticket : string
     * @return return HTML tag : string
     * @author Khabib and Sossio.
     */
    private static String buildTemplate(String user, String flightnbr, String seatnbr, String price) {
        return (
                "<h1>Flight Confirmation</h1>\n" +
                "<p> Hi <span class=\"color:red;\">"+ user+",</p> \n" +
                "<h2>More details about your ticket</h2>\n" +
                "<p>Flight number: "+flightnbr+"</p>\n" +
                "<p>Seat: "+seatnbr+"</p>\n" +
                "<h3> Total price: "+price+" SEK</h3>\n" +
                "<h2 style=\"color:red\">Online check in will be open 24 hours before boarding</h2> \n" +
                "<button>Check in</button> \n" +
                "\n" +
                "<h4>Best wish</h4>"
        );
    }


}
