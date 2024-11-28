package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.PayslipDto;
import org.banta.xardhr.service.payroll.PayrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    @PostMapping("/generate/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<PayslipDto> generatePayslip(@PathVariable Long userId) {
        return ResponseEntity.ok(payrollService.generatePayslip(userId));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<PayslipDto> approvePayslip(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.approvePayslip(id));
    }

//    @GetMapping("/employee/{userId}")
//    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
//    public ResponseEntity<List<PayslipDto>> getUserPayslips(@PathVariable Long userId) {
//        return ResponseEntity.ok(payrollService.getUserPayslips(userId));
//    }
}