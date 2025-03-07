package org.banta.xardhr.service.user;

import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.dto.response.AppUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AppUserService {
    AppUserDto createEmployee(RegisterRequest request);
    AppUserDto updateEmployee(Long id, RegisterRequest request);
    void deactivateEmployee(Long id);
    AppUserDto getEmployee(Long id);
    AppUserDto updateEmployeeProfile(Long id, RegisterRequest request);
    Page<AppUserDto> getAllEmployees(Pageable pageable);
    void updatePassword(Long id, String currentPassword, String newPassword, boolean resetPasswordRequired);}
