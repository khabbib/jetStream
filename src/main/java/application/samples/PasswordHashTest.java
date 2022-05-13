package application.samples;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This is a test class to hash a sample password.
 * Source: https://www.javatpoint.com/how-to-encrypt-password-in-java
 * @author Sossio.
 */
public class PasswordHashTest {

    /**
     * @param password password in plain text.
     * @author Sossio.
     */
    public static void hashPassword(String password) {
        /* Plain-text password initialization. */
        String encryptedpassword = null;
        try{
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(password.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i = 0; i < bytes.length ; i++) {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        /* Display the unencrypted and encrypted passwords. */
        System.out.println("Plain-text password: " + password);
        System.out.println("Encrypted password using MD5: " + encryptedpassword);
    }

    /* Driver Code */
    public static void main(String[] args) {
        hashPassword("password");
    }
}
