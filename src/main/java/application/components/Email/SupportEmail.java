package application.components.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * The class will send both automatic email and confirmation email from support window.
 */
public class SupportEmail {

    /**
     * this method send email to jetStream when contact/issue/feedback form filled
     * @param sender email address from sender : string
     * @param title title of the message : string
     * @param msg content of the message : string
     * @return return status of email
     * @author Khabib and Sossio.
     */
    public static boolean send(String sender, String title, String msg) {
        String receiver = "jetstream.oksh@gmail.com";
        String password = "jetstreamemail";
        String template = sendTemplate(sender, title, msg);
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
                    return new PasswordAuthentication(receiver, password);
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(template);

            Multipart multipart = new MimeMultipart();
            mimeBodyPart.setContent(template, "text/html; charset=utf-8");

            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent!");

            return true;

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * This method send an automatic confirmation email to customer who emailed
     * jetStream from support window.
     * @param sender email address to who sent the email : string
     * @author Khabib and Sossio.
     */
    public static void sendAutoEmail(String sender) {
        String receiver = "jetstream.oksh@gmail.com";
        String password = "jetstreamemail";
        String template = autoTemplate();
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
                    return new PasswordAuthentication(receiver, password);
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sender));

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(template);

            Multipart multipart = new MimeMultipart();
            mimeBodyPart.setContent(template, "text/html; charset=utf-8");

            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent!");

        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Template for send method
     * @param sender email address : string
     * @param title title : string
     * @param msg message : string
     * @return return HTML tage : string
     * @author Khabib and Sossio.
     */
    private static String sendTemplate(String sender, String title, String msg) {
        return (
                "<h1>New Support Message!</h1>\n" +
                        "<p>" + title + "</p> \n" +
                        "<p>" + sender + "</p> \n" +
                        "<h2>More details:</h2>\n" +
                        "<p>Message: "+  msg + "</p>\n"
        );
    }


    /**
     * Template for automatic email
     * @return return HTML tag : string
     * @author Khabib and Sossio.
     */
    private static String autoTemplate() {
        return (
                "<h1>Support Confirmation!</h1>\n" +
                        "<p> Thanks for contacting JetStream! We will be back in one hour. </p> \n"
        );
    }


}
