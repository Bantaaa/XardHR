package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.DepartmentDto;
import org.banta.xardhr.service.department.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        // Make sure DTO has all the fields the frontend needs
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentDto department) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }
}