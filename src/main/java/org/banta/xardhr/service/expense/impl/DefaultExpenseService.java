package org.banta.xardhr.service.expense.impl;

import org.banta.xardhr.domain.entity.ExpenseRequest;
import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.ExpenseRequestDto;
import org.banta.xardhr.service.expense.ExpenseService;

import java.util.List;

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
