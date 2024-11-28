package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.ExpenseRequestDto;
import org.banta.xardhr.service.expense.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD')")
    public ResponseEntity<ExpenseRequestDto> submitRequest(
            @RequestParam Long userId,
            @RequestBody ExpenseRequestDto request) {
        return ResponseEntity.ok(expenseService.submitRequest(userId, request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'DEPT_HEAD', 'ADMIN')")
    public ResponseEntity<ExpenseRequestDto> updateRequest(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        return ResponseEntity.ok(expenseService.updateRequest(id, status));
    }

    @GetMapping("/employee/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<ExpenseRequestDto>> getUserRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getUserRequests(userId));
    }
}