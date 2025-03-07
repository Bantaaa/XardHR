package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.RegisterRequest;
import org.banta.xardhr.dto.response.AppUserDto;
import org.banta.xardhr.service.user.AppUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final AppUserService appUserService;

    @GetMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Page<AppUserDto>> getAllEmployees(Pageable pageable) {
        return ResponseEntity.ok(appUserService.getAllEmployees(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<AppUserDto> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(appUserService.getEmployee(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<AppUserDto> createEmployee(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(appUserService.createEmployee(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<AppUserDto> updateEmployee(@PathVariable Long id, @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(appUserService.updateEmployee(id, request));
    }

    @PutMapping("/profile/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id)")
    public ResponseEntity<AppUserDto> updateOwnProfile(
            @PathVariable Long id,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(appUserService.updateEmployeeProfile(id, request));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable Long id) {
        appUserService.deactivateEmployee(id);
        return ResponseEntity.ok().build();
    }
}