package org.banta.xardhr.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.PasswordResetToken;
import org.banta.xardhr.repository.PasswordResetTokenRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.email.EmailService;
import org.banta.xardhr.service.user.PasswordService;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultPasswordService implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        log.info("Processing password reset request for email: {}", email);

        try {
            AppUser user = userRepository.findByUsername(email)
                    .orElseThrow(() -> {
                        log.warn("Password reset requested for non-existent email: {}", email);
                        return new ResourceNotFoundException("User not found with email: " + email);
                    });

            // Delete any existing tokens for this user
            tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

            // Create new token
            PasswordResetToken token = new PasswordResetToken(user);
            tokenRepository.save(token);

            // Send email
            try {
                emailService.sendPasswordResetEmail(email, token.getToken());
                log.info("Password reset email sent successfully to: {}", email);
            } catch (Exception e) {
                log.error("Failed to send password reset email: {}", e.getMessage(), e);
                throw e;
            }
        } catch (ResourceNotFoundException ex) {
            // For security reasons, don't expose that the email doesn't exist
            log.warn("Password reset attempted for non-existent user: {}", email);
            // Still return normally to prevent user enumeration attacks
        } catch (Exception e) {
            log.error("Error in password reset process: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new BadRequestException("Token has expired");
        }

        return true;
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // Validate token first
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new BadRequestException("Token has expired");
        }

        // Update password
        AppUser user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetRequired(false); // Reset the flag
        userRepository.save(user);

        // Delete the used token
        tokenRepository.delete(resetToken);

        log.info("Password reset successful for user: {}", user.getUsername());
    }

    @Override
    public String generateRandomPassword() {
        // Generate a random password with 4 letters, 2 numbers, and 2 special characters
        String letters = RandomStringUtils.randomAlphabetic(4);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specials = RandomStringUtils.random(2, "!@#$%^&*()");

        return letters + numbers + specials;
    }

    @Override
    @Transactional
    public void createUserWithRandomPassword(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Generate random password
        String randomPassword = generateRandomPassword();

        // Update user
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setPasswordResetRequired(true);
        userRepository.save(user);

        // Send email with credentials
        emailService.sendNewEmployeeCredentials(user.getUsername(), user.getUsername(), randomPassword);

        log.info("New employee credentials created for: {}", user.getUsername());
    }
}