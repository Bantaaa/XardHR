package org.banta.xardhr.service.email.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banta.xardhr.service.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultEmailService implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${spring.mail.username:no-reply@xardhr.org}")
    private String fromEmail;

    private String createEmailTemplate(String title, String content, String actionUrl, String actionText, String footerText) {
        String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <title>%s</title>
            <style type="text/css">
                @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
                
                /* Reset styles */
                body, html {
                    margin: 0;
                    padding: 0;
                    width: 100%%;
                    font-family: 'Inter', Arial, sans-serif;
                    -webkit-font-smoothing: antialiased;
                    -moz-osx-font-smoothing: grayscale;
                    color: #333333;
                    line-height: 1.6;
                    background-color: #f7f9fc;
                }
                
                * {
                    box-sizing: border-box;
                }
                
                a {
                    color: #2563eb;
                    text-decoration: none;
                }
                
                p {
                    margin: 0 0 16px;
                    font-size: 16px;
                    color: #4b5563;
                }
                
                strong {
                    color: #111827;
                    font-weight: 600;
                }
                
                /* Main container */
                .email-container {
                    max-width: 600px;
                    margin: 0 auto;
                    background: #ffffff;
                    border-radius: 12px;
                    overflow: hidden;
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                    margin-top: 20px;
                    margin-bottom: 20px;
                    border: 1px solid #e5e7eb;
                }
                
                /* Header */
                .email-header {
                    background: linear-gradient(135deg, #1a365d 0%%, #2d5990 100%%);
                    padding: 30px;
                    text-align: center;
                    position: relative;
                }
                
                .email-header::after {
                    content: '';
                    position: absolute;
                    bottom: 0;
                    left: 0;
                    right: 0;
                    height: 6px;
                    background: linear-gradient(90deg, #3b82f6, #2563eb, #1d4ed8);
                }
                
                .logo {
                    margin-bottom: 10px;
                }
                
                .logo-image {
                    height: 40px;
                    width: auto;
                }
                
                .logo-text {
                    font-size: 30px;
                    font-weight: 700;
                    color: #ffffff;
                    letter-spacing: 1px;
                    text-transform: uppercase;
                    margin: 0;
                    padding: 0;
                }
                
                .header-title {
                    font-size: 22px;
                    font-weight: 600;
                    color: #ffffff;
                    margin-top: 15px;
                    margin-bottom: 0;
                }
                
                /* Body */
                .email-body {
                    padding: 40px 30px;
                    background-color: #ffffff;
                }
                
                .greeting {
                    font-size: 18px;
                    font-weight: 500;
                    color: #111827;
                    margin-bottom: 20px;
                }
                
                .message {
                    margin-bottom: 25px;
                }
                
                /* Credentials Box */
                .credentials-box {
                    background-color: #f9fafb;
                    border: 1px solid #e5e7eb;
                    border-radius: 8px;
                    padding: 20px;
                    margin: 20px 0;
                }
                
                .credentials-box p {
                    margin: 8px 0;
                }
                
                .credentials-label {
                    font-weight: 600;
                    color: #374151;
                    display: inline-block;
                    width: 140px;
                }
                
                .credentials-value {
                    font-family: 'Courier New', monospace;
                    background-color: #eef2ff;
                    padding: 4px 8px;
                    border-radius: 4px;
                    color: #1e40af;
                    font-weight: 500;
                    letter-spacing: 0.5px;
                }
                
                /* Button */
                .button-container {
                    text-align: center;
                    margin: 30px 0;
                }
                
                .cta-button {
                    display: inline-block;
                    background-color: #2563eb;
                    color: white !important;
                    text-decoration: none;
                    font-weight: 600;
                    font-size: 16px;
                    padding: 12px 24px;
                    border-radius: 6px;
                    transition: background-color 0.3s;
                    box-shadow: 0 2px 4px rgba(37, 99, 235, 0.3);
                    text-align: center;
                }
                
                .cta-button:hover {
                    background-color: #1d4ed8;
                }
                
                /* Divider */
                .divider {
                    height: 1px;
                    background-color: #e5e7eb;
                    margin: 30px 0;
                }
                
                /* Note box */
                .note-box {
                    background-color: #fffbeb;
                    border-left: 4px solid #f59e0b;
                    padding: 15px;
                    border-radius: 4px;
                    margin: 25px 0;
                }
                
                .note-box p {
                    margin: 0;
                    color: #78350f;
                    font-size: 14px;
                }
                
                /* Footer */
                .email-footer {
                    background-color: #f9fafb;
                    padding: 25px;
                    text-align: center;
                    border-top: 1px solid #e5e7eb;
                }
                
                .footer-links {
                    margin-bottom: 15px;
                }
                
                .footer-link {
                    color: #6b7280;
                    font-size: 14px;
                    margin: 0 8px;
                    text-decoration: none;
                }
                
                .footer-link:hover {
                    color: #4b5563;
                    text-decoration: underline;
                }
                
                .copyright {
                    color: #9ca3af;
                    font-size: 13px;
                    margin-top: 5px;
                }
                
                .address {
                    color: #9ca3af;
                    font-size: 12px;
                    margin-top: 15px;
                    line-height: 1.5;
                }
                
                /* Social media */
                .social-links {
                    margin-top: 20px;
                    margin-bottom: 15px;
                }
                
                .social-link {
                    display: inline-block;
                    margin: 0 5px;
                }
                
                .social-icon {
                    width: 32px;
                    height: 32px;
                    border-radius: 50%%;
                    background-color: #e5e7eb;
                    display: inline-flex;
                    align-items: center;
                    justify-content: center;
                }
                
                /* Responsive */
                @media screen and (max-width: 600px) {
                    .email-container {
                        width: 100%%;
                        margin-top: 0;
                        margin-bottom: 0;
                        border-radius: 0;
                    }
                    
                    .email-body, .email-header, .email-footer {
                        padding: 20px 15px;
                    }
                    
                    .credentials-label {
                        width: 120px;
                    }
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="email-header">
                    <div class="logo">
                        <h1 class="logo-text">XardHR</h1>
                    </div>
                    <h2 class="header-title">%s</h2>
                </div>
                
                <div class="email-body">
                    <p class="greeting">Hello,</p>
                    
                    <div class="message">
                        %s
                    </div>
                    
                    %s
                    
                    <div class="note-box">
                        <p>If you did not request this action, please ignore this email or contact our support team immediately if you have any concerns.</p>
                    </div>
                    
                    <p>Best regards,<br><strong>XardHR Team</strong></p>
                </div>
                
                <div class="email-footer">
                    <div class="footer-links">
                        <a href="%s" class="footer-link">Home</a>
                        <a href="%s/support" class="footer-link">Support</a>
                        <a href="%s/privacy" class="footer-link">Privacy Policy</a>
                    </div>
                    
                    <div class="social-links">
                        <a href="#" class="social-link">
                            <div class="social-icon">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="#4b5563">
                                    <path d="M22.675 0h-21.35c-.732 0-1.325.593-1.325 1.325v21.351c0 .731.593 1.324 1.325 1.324h11.495v-9.294h-3.128v-3.622h3.128v-2.671c0-3.1 1.893-4.788 4.659-4.788 1.325 0 2.463.099 2.795.143v3.24l-1.918.001c-1.504 0-1.795.715-1.795 1.763v2.313h3.587l-.467 3.622h-3.12v9.293h6.116c.73 0 1.323-.593 1.323-1.325v-21.35c0-.732-.593-1.325-1.325-1.325z"/>
                                </svg>
                            </div>
                        </a>
                        <a href="#" class="social-link">
                            <div class="social-icon">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="#4b5563">
                                    <path d="M24 4.557c-.883.392-1.832.656-2.828.775 1.017-.609 1.798-1.574 2.165-2.724-.951.564-2.005.974-3.127 1.195-.897-.957-2.178-1.555-3.594-1.555-3.179 0-5.515 2.966-4.797 6.045-4.091-.205-7.719-2.165-10.148-5.144-1.29 2.213-.669 5.108 1.523 6.574-.806-.026-1.566-.247-2.229-.616-.054 2.281 1.581 4.415 3.949 4.89-.693.188-1.452.232-2.224.084.626 1.956 2.444 3.379 4.6 3.419-2.07 1.623-4.678 2.348-7.29 2.04 2.179 1.397 4.768 2.212 7.548 2.212 9.142 0 14.307-7.721 13.995-14.646.962-.695 1.797-1.562 2.457-2.549z"/>
                                </svg>
                            </div>
                        </a>
                        <a href="#" class="social-link">
                            <div class="social-icon">
                                <svg width="16" height="16" viewBox="0 0 24 24" fill="#4b5563">
                                    <path d="M12 0c-6.627 0-12 5.373-12 12s5.373 12 12 12 12-5.373 12-12-5.373-12-12-12zm-2 16h-2v-6h2v6zm-1-6.891c-.607 0-1.1-.496-1.1-1.109 0-.612.492-1.109 1.1-1.109s1.1.497 1.1 1.109c0 .613-.493 1.109-1.1 1.109zm8 6.891h-1.998v-2.861c0-1.881-2.002-1.722-2.002 0v2.861h-2v-6h2v1.093c.872-1.616 4-1.736 4 1.548v3.359z"/>
                                </svg>
                            </div>
                        </a>
                    </div>
                    
                    <p class="copyright">© %s XardHR. All rights reserved.</p>
                    
                    <p class="address">
                        XardHR, Inc.<br>
                        123 Business Avenue, Suite 100<br>
                        San Francisco, CA 94107
                    </p>
                    
                    %s
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                title,
                title,
                content,
                getActionButtonHtml(actionUrl, actionText),
                frontendUrl,
                frontendUrl,
                frontendUrl,
                currentYear,
                footerText != null ? "<p class=\"address\">" + footerText + "</p>" : ""
        );
    }

    private String getActionButtonHtml(String actionUrl, String actionText) {
        if (actionUrl != null && !actionUrl.isEmpty() && actionText != null) {
            return String.format("""
                <div class="button-container">
                    <a href="%s" class="cta-button">%s</a>
                </div>
                """, actionUrl, actionText);
        }
        return "";
    }

    private String createCredentialsBox(String username, String password) {
        return String.format("""
            <div class="credentials-box">
                <p><span class="credentials-label">Username:</span> <span class="credentials-value">%s</span></p>
                <p><span class="credentials-label">Temporary Password:</span> <span class="credentials-value">%s</span></p>
            </div>
            """, username, password);
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
        String subject = "XardHR - Reset Your Password";

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            String content = """
                <p>We received a request to reset the password for your XardHR account.</p>
                <p>Please click the button below to create a new password. This link will expire in 1 hour for security reasons.</p>
                """;

            String htmlContent = createEmailTemplate(
                    "Reset Your Password",
                    content,
                    resetUrl,
                    "Reset Password",
                    null
            );

            helper.setText(htmlContent, true);
            emailSender.send(message);

            log.info("Password reset email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendNewEmployeeCredentials(String to, String username, String tempPassword) {
        String loginUrl = frontendUrl + "/login";
        String subject = "XardHR - Welcome to Your New Account";

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            String content = """
                <p>Welcome to XardHR! Your account has been successfully created and is ready to use.</p>
                <p>Below are your login credentials:</p>
                %s
                <p>For security reasons, you'll be required to change your password upon first login.</p>
                <p>If you encounter any issues, our support team is available to assist you. Simply reply to this email or contact our support center.</p>
                """.formatted(createCredentialsBox(username, tempPassword));

            String htmlContent = createEmailTemplate(
                    "Welcome to XardHR!",
                    content,
                    loginUrl,
                    "Login to Your Account",
                    "Please keep your login information secure and do not share it with others."
            );

            helper.setText(htmlContent, true);
            emailSender.send(message);

            log.info("New employee credentials email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send new employee credentials email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            String content = "<p>" + text.replace("\n", "<br>") + "</p>";

            String htmlContent = createEmailTemplate(
                    subject,
                    content,
                    null,
                    null,
                    null
            );

            helper.setText(htmlContent, true);
            emailSender.send(message);

            log.info("Simple email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send simple email to {}: {}", to, e.getMessage(), e);
        }
    }
}