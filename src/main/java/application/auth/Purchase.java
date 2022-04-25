package application.auth;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * This class sends email to every user who booked a seat.
 */
public class Purchase {

    /**
     * @param nbr
     * @param firstName
     * @param lastName
     * @param month
     * @param year
     * @param cvc
     * @return
     */
    public static boolean purchaseTicket(String nbr, String firstName, String lastName, String month, String year, String cvc) {
        // CAN BE DEVELOPED MORE...
        return true;
    }

    /**
     * @param receiver
     * @param user
     * @param flightnbr
     * @param seatNbr
     * @param price
     * @return
     */
    public static boolean sendEmail(String receiver, String user,String flightnbr, String seatNbr,String price) {
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

            message.setFrom(new InternetAddress("anonym@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("kadestalkasper@gmail.com"));

            /*
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("kadestalkasper@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sosiosos2002@gmail.com"));
            message.setSubject("Critic POINT!");
             */

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
     * @param user
     * @param flightnbr
     * @param seatnbr
     * @param price
     * @return
     */
    private static String buildTemplate(String user, String flightnbr, String seatnbr, String price) {
        return (
                "<h1>Verification</h1>\n" +
                "<p> Hi <span class=\"color:red;\">"+ user+",</p> \n" +
                "<h2>More details about your ticket</h2>\n" +
                "<p>Flight number: "+flightnbr+"</p>\n" +
                "<p>Seat: "+seatnbr+"</p>\n" +
                "<h3> Total price: "+price+" SEK</h3>\n" +
                "<h2 style=\"color:red\">Please verify your email</h2> \n" +
                "<button>Verify</button> \n" +
                "\n" +
                "<h4>Best wish</h4>"
        );
    }

}
