package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.LeaveRequestDto;
import org.banta.xardhr.service.leave.LeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD')")
    public ResponseEntity<LeaveRequestDto> submitRequest(
            @RequestParam Long userId,
            @RequestBody LeaveRequestDto request) {
        return ResponseEntity.ok(leaveService.submitRequest(userId, request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'DEPT_HEAD', 'ADMIN')")
    public ResponseEntity<LeaveRequestDto> updateRequest(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(leaveService.updateRequest(id, status));
    }

    @GetMapping("/employee/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<LeaveRequestDto>> getUserRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveService.getUserRequests(userId));
    }
}