package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.BankAccountDto;
import org.banta.xardhr.service.bank.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankService bankService;

    @PostMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
    public ResponseEntity<BankAccountDto> addBankAccount(
            @RequestParam Long userId,
            @RequestBody BankAccountDto account) {
        return ResponseEntity.ok(bankService.addBankAccount(userId, account));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
    public ResponseEntity<BankAccountDto> updateBankAccount(
            @PathVariable Long userId,
            @RequestBody BankAccountDto account) {
        return ResponseEntity.ok(bankService.updateBankAccount(userId, account));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        bankService.deleteBankAccount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employee/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
    public ResponseEntity<BankAccountDto> getUserBankAccount(@PathVariable Long userId) {
        return ResponseEntity.ok(bankService.getUserBankAccount(userId));
    }
}