package org.banta.xardhr.service.expense;

import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.ExpenseRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenseService {
    ExpenseRequestDto submitRequest(Long userId, ExpenseRequestDto request);
    ExpenseRequestDto updateRequest(Long id, RequestStatus status);
    List<ExpenseRequestDto> getUserRequests(Long userId);
    void deleteExpenseRequest(Long id);
}
