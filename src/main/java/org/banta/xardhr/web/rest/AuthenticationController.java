package org.banta.xardhr.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.PasswordUpdateRequest;
import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.service.security.AuthenticationService;
import org.banta.xardhr.dto.request.AuthRequest;
import org.banta.xardhr.dto.response.AuthResponse;
import org.banta.xardhr.service.user.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateRequest request) {
        appUserService.updatePassword(id, request.getPassword(), request.isResetPasswordRequired());
        return ResponseEntity.ok().build();
    }
}