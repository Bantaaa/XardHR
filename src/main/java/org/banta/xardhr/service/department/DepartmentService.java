package org.banta.xardhr.service.department;

import org.banta.xardhr.dto.request.DepartmentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentDto department);
    DepartmentDto updateDepartment(Long id, DepartmentDto department);
    void deleteDepartment(Long id);
    List<DepartmentDto> getAllDepartments();
}
