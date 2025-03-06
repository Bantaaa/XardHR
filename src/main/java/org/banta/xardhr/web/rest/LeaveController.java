package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.LeaveRequestDto;
import org.banta.xardhr.service.leave.LeaveService;
import org.banta.xardhr.service.leave.impl.DefaultLeaveService;
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
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<LeaveRequestDto> submitRequest(
            @RequestParam Long userId,
            @RequestBody LeaveRequestDto request) {
        // Make sure to calculate totalDays and set it in the response
        LeaveRequestDto dto = leaveService.submitRequest(userId, request);
        return ResponseEntity.ok(dto);
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

    @GetMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<LeaveRequestDto>> getAllLeaves() {
        return ResponseEntity.ok(((DefaultLeaveService)leaveService).getAllLeaves());
    }

    @DeleteMapping("/request/{id}")
    @PreAuthorize("@securityService.isLeaveRequestOwner(#id)")
    public ResponseEntity<Void> cancelLeaveRequest(@PathVariable Long id) {
        leaveService.cancelLeaveRequest(id);
        return ResponseEntity.ok().build();
    }
}