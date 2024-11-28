package org.banta.xardhr.service.expense.impl;

import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.ExpenseRequestDto;
import org.banta.xardhr.service.expense.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultExpenseService implements ExpenseService {

    @Override
    public ExpenseRequestDto submitRequest(Long userId, ExpenseRequestDto request) {
        return null;
    }

    @Override
    public ExpenseRequestDto updateRequest(Long id, RequestStatus status) {
        return null;
    }

    @Override
    public List<ExpenseRequestDto> getUserRequests(Long userId) {
        return List.of();
    }
}
