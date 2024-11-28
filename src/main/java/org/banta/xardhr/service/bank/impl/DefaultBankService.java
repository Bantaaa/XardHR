package org.banta.xardhr.service.bank.impl;

import org.banta.xardhr.dto.request.BankAccountDto;
import org.banta.xardhr.service.bank.BankService;

public class DefaultBankService implements BankService {

    @Override
    public BankAccountDto addBankAccount(Long userId, BankAccountDto account) {
        return null;
    }

    @Override
    public BankAccountDto updateBankAccount(Long id, BankAccountDto account) {
        return null;
    }

    @Override
    public void deleteBankAccount(Long id) {

    }

    @Override
    public BankAccountDto getUserBankAccount(Long userId) {
        return null;
    }
}
