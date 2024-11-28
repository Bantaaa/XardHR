package org.banta.xardhr.service.transaction.impl;

import org.banta.xardhr.domain.entity.Transaction;
import org.banta.xardhr.dto.request.TransactionDto;
import org.banta.xardhr.service.transaction.TransactionService;

import java.util.List;

public class DefaultTransactionService implements TransactionService {

    @Override
    public List<TransactionDto> getUserTransactions(Long userId) {
        return List.of();
    }

    @Override
    public TransactionDto addTransaction(Long accountId, TransactionDto transaction) {
        return null;
    }
}
