package org.banta.xardhr.service.email.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banta.xardhr.service.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultEmailService implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${spring.mail.username:no-reply@xardhr.org}")
    private String fromEmail;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            log.info("Attempting to send email to: {}", to);
            emailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
        String subject = "XardHR Password Reset";
        String text = "Hello,\n\n" +
                "You have requested to reset your password. Please click the link below to reset your password:\n\n" +
                resetUrl + "\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Regards,\nXardHR Team";

        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendNewEmployeeCredentials(String to, String username, String tempPassword) {
        String loginUrl = frontendUrl + "/login";
        String subject = "Welcome to XardHR - Your Account Details";
        String text = "Hello,\n\n" +
                "Welcome to XardHR! Your account has been created. Please use the following credentials to log in:\n\n" +
                "Username: " + username + "\n" +
                "Temporary Password: " + tempPassword + "\n\n" +
                "Login URL: " + loginUrl + "\n\n" +
                "You will be required to change your password upon first login for security reasons.\n\n" +
                "Regards,\nXardHR Team";

        sendSimpleMessage(to, subject, text);
    }
}