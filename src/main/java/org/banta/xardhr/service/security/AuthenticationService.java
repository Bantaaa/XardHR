package org.banta.xardhr.service.security;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
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
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
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
        appUser.setStatus(EmployeeStatus.ACTIVE);
        appUser.setJoiningDate(LocalDate.now());
        appUser.setEmployeeId(UUID.randomUUID());
        appUser.setIsActive(true);

        AppUser savedAppUser = userRepository.save(appUser);
        String token = jwtService.generateToken(savedAppUser);

        return AuthResponse.builder()
                .token(token)
                .username(savedAppUser.getUsername())
                .role(savedAppUser.getRole())
                // Add additional fields expected by frontend
                .id(savedAppUser.getId().toString())
                .firstName(savedAppUser.getFirstName())
                .lastName(savedAppUser.getLastName())
                .employeeId(savedAppUser.getEmployeeId().toString())
                .position(savedAppUser.getPosition())
                .contactNumber(savedAppUser.getContactNumber())
                .status(savedAppUser.getStatus().toString())
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

        // Update last login timestamp
        appUser.setLastLogin(LocalDateTime.now());
        userRepository.save(appUser);

        String token = jwtService.generateToken(appUser);

        return AuthResponse.builder()
                .token(token)
                .username(appUser.getUsername())
                .role(appUser.getRole())
                // Add additional fields expected by frontend
                .id(appUser.getId().toString())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .employeeId(appUser.getEmployeeId().toString())
                .position(appUser.getPosition())
                .contactNumber(appUser.getContactNumber())
                .status(appUser.getStatus().toString())
                .departmentId(appUser.getDepartment() != null ?
                        appUser.getDepartment().getId().toString() : null)
                .build();
    }
}