package org.banta.xardhr.service.user;

import org.springframework.stereotype.Service;

@Service
public interface PasswordService {
    void requestPasswordReset(String email);
    boolean validatePasswordResetToken(String token);
    void resetPassword(String token, String newPassword);
    String generateRandomPassword();
    void createUserWithRandomPassword(Long userId);
}