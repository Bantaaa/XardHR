package org.banta.xardhr.service.department.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.Department;
import org.banta.xardhr.dto.request.DepartmentDto;
import org.banta.xardhr.repository.DepartmentRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.department.DepartmentService;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        // Set department head if specified
        if (departmentDto.getHeadId() != null) {
            AppUser head = userRepository.findById(Long.valueOf(departmentDto.getHeadId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Department head not found"));
            department.setDepartmentHead(head);
        }

        // Set parent department if specified
        if (departmentDto.getParentId() != null) {
            Department parent = departmentRepository.findById(Long.valueOf(departmentDto.getParentId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Parent department not found"));
            department.setParentDepartment(parent);
        }

        Department saved = departmentRepository.save(department);
        return enhanceDepartmentDto(convertToDto(saved));
    }

    @Override
    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());

        // Update department head if specified
        if (departmentDto.getHeadId() != null && !departmentDto.getHeadId().isEmpty()) {
            try {
                Long headId = Long.valueOf(departmentDto.getHeadId());
                AppUser head = userRepository.findById(headId)
                        .orElseThrow(() -> new ResourceNotFoundException("Department head not found"));
                department.setDepartmentHead(head);
            } catch (NumberFormatException e) {
                throw new ResourceNotFoundException("Invalid department head ID format");
            }
        } else {
            department.setDepartmentHead(null);
        }

        // Update parent department if specified
        if (departmentDto.getParentId() != null && !departmentDto.getParentId().isEmpty()) {
            try {
                Long parentId = Long.valueOf(departmentDto.getParentId());

                // Prevent circular reference - department can't be its own parent
                if (parentId.equals(id)) {
                    throw new ResourceNotFoundException("Department cannot be its own parent");
                }

                Department parent = departmentRepository.findById(parentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Parent department not found"));
                department.setParentDepartment(parent);
            } catch (NumberFormatException e) {
                throw new ResourceNotFoundException("Invalid parent department ID format");
            }
        } else {
            department.setParentDepartment(null);
        }

        Department updated = departmentRepository.save(department);
        return enhanceDepartmentDto(convertToDto(updated));
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }

        // Check if there are any child departments
        List<Department> children = departmentRepository.findChildDepartments(id);
        if (!children.isEmpty()) {
            throw new ResourceNotFoundException("Cannot delete department with child departments");
        }

        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(department -> enhanceDepartmentDto(convertToDto(department)))
                .collect(Collectors.toList());
    }

    // Convert entity to basic DTO
    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId().toString());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());

        if (department.getDepartmentHead() != null) {
            dto.setHeadId(department.getDepartmentHead().getId().toString());

            AppUser head = department.getDepartmentHead();
            dto.setHead(head.getFirstName() + " " + head.getLastName());
        }

        if (department.getParentDepartment() != null) {
            dto.setParentId(department.getParentDepartment().getId().toString());
        }

        return dto;
    }

    // Enhance DTO with additional fields needed by frontend
    private DepartmentDto enhanceDepartmentDto(DepartmentDto dto) {
        // Set placeholder values for employee count and projects
        // In a real implementation, you would calculate these values
        dto.setEmployeeCount(countEmployeesInDepartment(Long.valueOf(dto.getId())));
        dto.setProjects(5); // Default placeholder

        return dto;
    }

    // Count employees in a department
    private Integer countEmployeesInDepartment(Long departmentId) {
        List<AppUser> employees = userRepository.findByDepartmentId(departmentId);
        return employees.size();
    }
}