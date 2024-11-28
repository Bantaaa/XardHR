package org.banta.xardhr.service.department.impl;

import org.banta.xardhr.domain.entity.Department;
import org.banta.xardhr.dto.request.DepartmentDto;
import org.banta.xardhr.service.department.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultDepartmentService implements DepartmentService {

    @Override
    public DepartmentDto createDepartment(DepartmentDto department) {
        return null;
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto department) {
        return null;
    }

    @Override
    public void deleteDepartment(Long id) {

    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return List.of();
    }
}
