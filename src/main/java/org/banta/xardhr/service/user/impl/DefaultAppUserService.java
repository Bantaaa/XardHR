package org.banta.xardhr.service.user.impl;

import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.dto.response.AppUserDto;
import org.banta.xardhr.service.user.AppUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DefaultAppUserService implements AppUserService {

    @Override
    public AppUserDto createEmployee(RegisterRequest request) {
        return null;
    }

    @Override
    public AppUserDto updateEmployee(Long id, RegisterRequest request) {
        return null;
    }

    @Override
    public void deactivateEmployee(Long id) {

    }

    @Override
    public AppUserDto getEmployee(Long id) {
        return null;
    }

    @Override
    public Page<AppUserDto> getAllEmployees(Pageable pageable) {
        return null;
    }
}
