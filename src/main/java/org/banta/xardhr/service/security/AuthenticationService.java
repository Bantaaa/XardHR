package org.banta.xardhr.service.security;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.enums.UserRole;
import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.dto.request.AuthRequest;
import org.banta.xardhr.dto.response.AuthResponse;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordValidator passwordValidator;


    public AuthResponse register(RegisterRequest request) {
        if (!passwordValidator.isValid(request.getPassword())) {
            throw new BadRequestException(passwordValidator.getPasswordRequirements());
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(request.getUsername());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRole(UserRole.EMPLOYEE);
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setContactNumber(request.getContactNumber());
        appUser.setPosition(request.getPosition());

        AppUser savedAppUser = userRepository.save(appUser);
        String token = jwtService.generateToken(savedAppUser);

        return AuthResponse.builder()
                .token(token)
                .username(savedAppUser.getUsername())
                .role(savedAppUser.getRole())
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        AppUser appUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwtService.generateToken(appUser);

        return AuthResponse.builder()
                .token(token)
                .username(appUser.getUsername())
                .role(appUser.getRole())
                .build();
    }
    // password validation during user creation/password change
    public void validatePassword(String password) {
        if (!passwordValidator.isValid(password)) {
            throw new BadRequestException(passwordValidator.getPasswordRequirements());
        }
    }
}