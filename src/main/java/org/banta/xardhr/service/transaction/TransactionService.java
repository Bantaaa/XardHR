package org.banta.xardhr.service.transaction;

import org.banta.xardhr.dto.request.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    List<TransactionDto> getUserTransactions(Long userId);
    TransactionDto addTransaction(Long accountId, TransactionDto transaction);
}
