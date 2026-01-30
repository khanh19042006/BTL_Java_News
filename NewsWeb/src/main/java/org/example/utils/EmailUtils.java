package org.example.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.regex.Pattern;

public class EmailUtils {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private EmailUtils() {}

    public static boolean isValid(String email) {
        if (email == null) return false;
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean sendOTP(String toEmail, String otp) {

        final String fromEmail = "hitnews2006@gmail.com";
        final String password = "ofrdbvbtxtzahbyx"; // App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("Email Verification OTP");

            // Gửi HTML đẹp hơn
            message.setContent(
                    "<h2>Email Verification</h2>"
                            + "<p>Your OTP is: <b>" + otp + "</b></p>"
                            + "<p>This code expires in 5 minutes.</p>",
                    "text/html; charset=utf-8"
            );

            Transport.send(message);

            System.out.println("OTP sent successfully!");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
