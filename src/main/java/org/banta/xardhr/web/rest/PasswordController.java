package org.banta.xardhr.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.NewPasswordRequest;
import org.banta.xardhr.dto.request.PasswordResetRequest;
import org.banta.xardhr.service.user.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;


    @PostMapping("/reset-request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request) {

        passwordService.requestPasswordReset(request.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "If the email exists in our system, a password reset link has been sent");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestParam String token) {
        boolean valid = passwordService.validatePasswordResetToken(token);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", valid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody NewPasswordRequest request) {

        passwordService.resetPassword(request.getToken(), request.getNewPassword());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password has been reset successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-for-user/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> generatePasswordForUser(
            @PathVariable Long userId) {

        passwordService.createUserWithRandomPassword(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Temporary password generated and sent to the user");
        return ResponseEntity.ok(response);
    }
}