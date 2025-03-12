package org.banta.xardhr.service.email;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendPasswordResetEmail(String to, String resetToken);
    void sendNewEmployeeCredentials(String to, String username, String tempPassword);
}