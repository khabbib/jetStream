package application.samples;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * This is a test class to send an email.
 * @author Khabib.
 */
public class Mail {
    private String to = "abcd@gmail.com";
    private String from = "web@gmail.com";
    private String host = "localhost";

    public static void main(String[] args) {
        Mail mail = new Mail("kadestalkasper@gmail.com","kadestalkasper@gmail.com","localhost");
    }

    /**
     * Class constructor.
     * @param to user
     * @param from user.
     * @param host is host of email.
     */
    public Mail(String to, String from, String host) {
        this.to = to;
        this.from = from;
        this.host = host;

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
