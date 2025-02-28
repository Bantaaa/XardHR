package org.banta.xardhr.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.Department;
import org.banta.xardhr.domain.enums.EmployeeStatus;
import org.banta.xardhr.domain.enums.UserRole;
import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.dto.response.AppUserDto;
import org.banta.xardhr.repository.DepartmentRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.user.AppUserService;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultAppUserService implements AppUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDto createEmployee(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setContactNumber(request.getContactNumber());
        user.setPosition(request.getPosition());
        user.setRole(UserRole.EMPLOYEE);
        user.setStatus(EmployeeStatus.ACTIVE);
        user.setJoiningDate(LocalDate.now());
        user.setEmployeeId(UUID.randomUUID());
        user.setIsActive(true);

        // Handle department if specified
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            user.setDepartment(department);
        }

        AppUser savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public AppUserDto updateEmployee(Long id, RegisterRequest request) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update the user fields
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setContactNumber(request.getContactNumber());
        user.setPosition(request.getPosition());

        // Update email/username if provided
        if (request.getUsername() != null && !request.getUsername().isEmpty()
                && !request.getUsername().equals(user.getUsername())) {
            // Check if new username is available
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new BadRequestException("Username/email already exists");
            }
            user.setUsername(request.getUsername());
        }

        // Update department if provided
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            user.setDepartment(department);
        }

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        AppUser updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Override
    public void deactivateEmployee(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsActive(false);
        user.setStatus(EmployeeStatus.TERMINATED);
        userRepository.save(user);
    }

    @Override
    public AppUserDto getEmployee(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return convertToDto(user);
    }

    @Override
    public Page<AppUserDto> getAllEmployees(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    private AppUserDto convertToDto(AppUser user) {
        AppUserDto dto = new AppUserDto();
        dto.setId(user.getId()); // Add ID for frontend
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setGender(user.getGender());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setStatus(user.getStatus());
        dto.setJoiningDate(user.getJoiningDate());
        dto.setBaseSalary(user.getBaseSalary());
        dto.setPosition(user.getPosition());
        dto.setContactNumber(user.getContactNumber());
        dto.setEmail(user.getUsername()); // Set email same as username for frontend

        if (user.getDepartment() != null) {
            dto.setDepartmentId(user.getDepartment().getId());
            dto.setDepartment(user.getDepartment().getName()); // Include department name
        }

        return dto;
    }
}