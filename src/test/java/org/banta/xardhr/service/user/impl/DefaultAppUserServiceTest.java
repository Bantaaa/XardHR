package org.banta.xardhr.service.user.impl;

import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.Department;
import org.banta.xardhr.domain.enums.EmployeeStatus;
import org.banta.xardhr.domain.enums.UserRole;
import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.dto.response.AppUserDto;
import org.banta.xardhr.repository.DepartmentRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.user.PasswordService;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultAppUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private DefaultAppUserService appUserService;

    private RegisterRequest validRequest;
    private AppUser existingUser;
    private Department department;

    @BeforeEach
    void setUp() {
        // Setup test data
        validRequest = new RegisterRequest();
        validRequest.setUsername("test@xardhr.com");
        validRequest.setPassword("password123");
        validRequest.setFirstName("Test");
        validRequest.setLastName("User");
        validRequest.setContactNumber("0612345678");
        validRequest.setPosition("Developer");
        validRequest.setDepartmentId(1L);

        department = new Department();
        department.setId(1L);
        department.setName("IT Department");

        existingUser = new AppUser();
        existingUser.setId(1L);
        existingUser.setUsername("existing@xardhr.com");
        existingUser.setPassword("hashedPassword");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setContactNumber("0687654321");
        existingUser.setPosition("Manager");
        existingUser.setRole(UserRole.EMPLOYEE);
        existingUser.setStatus(EmployeeStatus.ACTIVE);
        existingUser.setJoiningDate(LocalDate.now().minusMonths(3));
        existingUser.setEmployeeId(UUID.randomUUID());
        existingUser.setIsActive(true);
        existingUser.setDepartment(department);
    }

    @Test
    void createEmployee_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(AppUser.class))).thenAnswer(i -> {
            AppUser user = i.getArgument(0);
            user.setId(1L);
            return user;
        });
        doNothing().when(passwordService).createUserWithRandomPassword(anyLong());

        // Act
        AppUserDto result = appUserService.createEmployee(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(validRequest.getUsername(), result.getUsername());
        assertEquals(validRequest.getFirstName(), result.getFirstName());
        assertEquals(validRequest.getLastName(), result.getLastName());
        assertEquals(validRequest.getPosition(), result.getPosition());
        assertEquals("EMPLOYEE", result.getRole().toString());
        assertEquals("ACTIVE", result.getStatus());

        verify(userRepository).findByUsername(validRequest.getUsername());
        verify(departmentRepository).findById(validRequest.getDepartmentId());
        verify(passwordEncoder).encode(validRequest.getPassword());
        verify(userRepository).save(any(AppUser.class));
        verify(passwordService).createUserWithRandomPassword(anyLong());
    }

    @Test
    void createEmployee_UsernameExists_ThrowsBadRequestException() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new AppUser()));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            appUserService.createEmployee(validRequest);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).findByUsername(validRequest.getUsername());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(passwordService);
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void createEmployee_DepartmentNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appUserService.createEmployee(validRequest);
        });

        assertEquals("Department not found", exception.getMessage());
        verify(userRepository).findByUsername(validRequest.getUsername());
        verify(departmentRepository).findById(validRequest.getDepartmentId());
        // Remove this line: verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(passwordService);
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void updateEmployee_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(userRepository.save(any(AppUser.class))).thenReturn(existingUser);

        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setUsername("updated@xardhr.com");
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");
        updateRequest.setContactNumber("0699887766");
        updateRequest.setPosition("Senior Developer");
        updateRequest.setDepartmentId(1L);

        // Act
        AppUserDto result = appUserService.updateEmployee(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updateRequest.getUsername(), result.getUsername());
        assertEquals(updateRequest.getFirstName(), result.getFirstName());
        assertEquals(updateRequest.getLastName(), result.getLastName());
        assertEquals(updateRequest.getPosition(), result.getPosition());

        verify(userRepository).findById(1L);
        verify(departmentRepository).findById(updateRequest.getDepartmentId());
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void updateEmployee_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appUserService.updateEmployee(1L, validRequest);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(departmentRepository);
    }

    @Test
    void updateEmployee_UsernameExists_ThrowsBadRequestException() {
        // Arrange
        AppUser otherUser = new AppUser();
        otherUser.setId(2L);
        otherUser.setUsername("other@xardhr.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(eq("other@xardhr.com"))).thenReturn(Optional.of(otherUser));

        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setUsername("other@xardhr.com");
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("User");

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            appUserService.updateEmployee(1L, updateRequest);
        });

        assertEquals("Username/email already exists", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).findByUsername("other@xardhr.com");
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void deactivateEmployee_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(existingUser);

        // Act
        appUserService.deactivateEmployee(1L);

        // Assert
        assertFalse(existingUser.getIsActive());
        assertEquals(EmployeeStatus.TERMINATED, existingUser.getStatus());

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void deactivateEmployee_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appUserService.deactivateEmployee(1L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void getEmployee_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Act
        AppUserDto result = appUserService.getEmployee(1L);

        // Assert
        assertNotNull(result);
        assertEquals(existingUser.getId().toString(), result.getId());
        assertEquals(existingUser.getUsername(), result.getUsername());
        assertEquals(existingUser.getFirstName(), result.getFirstName());
        assertEquals(existingUser.getLastName(), result.getLastName());
        assertEquals(existingUser.getPosition(), result.getPosition());
        assertEquals(existingUser.getRole(), result.getRole());

        verify(userRepository).findById(1L);
    }

    @Test
    void getEmployee_UserNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appUserService.getEmployee(1L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void getAllEmployees_Success() {
        // Arrange
        AppUser user2 = new AppUser();
        user2.setId(2L);
        user2.setUsername("user2@xardhr.com");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setEmployeeId(UUID.randomUUID());
        user2.setRole(UserRole.EMPLOYEE);
        user2.setStatus(EmployeeStatus.ACTIVE);
        user2.setJoiningDate(LocalDate.now());

        Page<AppUser> userPage = new PageImpl<>(Arrays.asList(existingUser, user2));
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // Act
        Page<AppUserDto> result = appUserService.getAllEmployees(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        verify(userRepository).findAll(pageable);
    }

    @Test
    void updateEmployeeProfile_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(existingUser);

        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("Profile");
        updateRequest.setContactNumber("0600112233");

        // Act
        AppUserDto result = appUserService.updateEmployeeProfile(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updateRequest.getFirstName(), result.getFirstName());
        assertEquals(updateRequest.getLastName(), result.getLastName());
        assertEquals(updateRequest.getContactNumber(), result.getContactNumber());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(AppUser.class));
    }

    @Test
    void updateEmployeeProfile_UsernameExists_ThrowsBadRequestException() {
        // Arrange
        AppUser otherUser = new AppUser();
        otherUser.setId(2L);
        otherUser.setUsername("other@xardhr.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(eq("other@xardhr.com"))).thenReturn(Optional.of(otherUser));

        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setUsername("other@xardhr.com");
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("Profile");

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            appUserService.updateEmployeeProfile(1L, updateRequest);
        });

        assertEquals("Email address already in use", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).findByUsername("other@xardhr.com");
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void updatePassword_IncorrectCurrentPassword_ThrowsBadRequestException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            appUserService.updatePassword(1L, "wrongPassword", "newPassword", false);
        });

        assertEquals("Current password is incorrect", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(passwordEncoder).matches("wrongPassword", existingUser.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(AppUser.class));
    }
}