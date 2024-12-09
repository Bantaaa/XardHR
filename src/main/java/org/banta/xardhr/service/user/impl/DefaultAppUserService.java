package org.banta.xardhr.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.enums.UserRole;
import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.dto.response.AppUserDto;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.user.AppUserService;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAppUserService implements AppUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDto createEmployee(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(UserRole.EMPLOYEE);
        user.setIsActive(true);
        user.setContactNumber(request.getContactNumber());
        user.setPosition(request.getPosition());

        return convertToDto(userRepository.save(user));
    }

    private AppUserDto convertToDto(AppUser user) {
        AppUserDto dto = new AppUserDto();
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
        dto.setDepartmentId(user.getDepartment() != null ? user.getDepartment().getId() : null);
        return dto;
    }

    @Override
    public AppUserDto updateEmployee(Long id, RegisterRequest request) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Employee not found"));

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(UserRole.EMPLOYEE);
        user.setIsActive(true);
        user.setContactNumber(request.getContactNumber());
        user.setPosition(request.getPosition());

        return convertToDto(userRepository.save(user));
    }

    @Override
    public void deactivateEmployee(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Employee not found"));

        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public AppUserDto getEmployee(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new BadRequestException("Employee not found"));
    }

    @Override
    public Page<AppUserDto> getAllEmployees(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToDto);
    }
}
