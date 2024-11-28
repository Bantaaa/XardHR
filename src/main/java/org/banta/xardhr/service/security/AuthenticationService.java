package org.banta.xardhr.service.security;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.User;
import org.banta.xardhr.domain.enums.EmployeeStatus;
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

import java.time.LocalDate;
import java.util.UUID;

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

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.EMPLOYEE);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setContactNumber(request.getContactNumber());
        user.setPosition(request.getPosition());

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
    // password validation during user creation/password change
    public void validatePassword(String password) {
        if (!passwordValidator.isValid(password)) {
            throw new BadRequestException(passwordValidator.getPasswordRequirements());
        }
    }
}