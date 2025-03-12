package org.banta.xardhr.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.PasswordUpdateRequest;
import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.service.security.AuthenticationService;
import org.banta.xardhr.dto.request.AuthRequest;
import org.banta.xardhr.dto.response.AuthResponse;
import org.banta.xardhr.service.security.SecurityService;
import org.banta.xardhr.service.user.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AppUserService appUserService;
    private final SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("@securityService.isCurrentUser(#id)")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateRequest request) {
        // Admin doesn't need to provide current password
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin && !securityService.isCurrentUser(id)) {
            // Admin changing someone else's password
            appUserService.updatePassword(id, null, request.getPassword(), request.isResetPasswordRequired());
        } else {
            // User changing own password or admin changing own password
            appUserService.updatePassword(id, request.getCurrentPassword(), request.getPassword(), request.isResetPasswordRequired());
        }
        return ResponseEntity.ok().build();
    }
}