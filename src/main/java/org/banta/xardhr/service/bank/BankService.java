package org.banta.xardhr.service.bank;

import org.banta.xardhr.dto.request.BankAccountDto;
import org.springframework.stereotype.Service;

@Service
public interface BankService {
    BankAccountDto addBankAccount(Long userId, BankAccountDto account);
    BankAccountDto updateBankAccount(Long id, BankAccountDto account);
    void deleteBankAccount(Long id);
    BankAccountDto getUserBankAccount(Long userId);
}
