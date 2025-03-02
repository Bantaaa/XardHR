package org.banta.xardhr.service.expense.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.ExpenseRequest;
import org.banta.xardhr.domain.enums.ExpenseType;
import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.ExpenseRequestDto;
import org.banta.xardhr.repository.ExpenseRequestRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.expense.ExpenseService;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultExpenseService implements ExpenseService {

    private final ExpenseRequestRepository expenseRequestRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public ExpenseRequestDto submitRequest(Long userId, ExpenseRequestDto requestDto) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        ExpenseRequest expenseRequest = new ExpenseRequest();
        expenseRequest.setAppUser(user);
        expenseRequest.setAmount(requestDto.getAmount());
        expenseRequest.setType(ExpenseType.valueOf(requestDto.getType())); // Parse string to enum
        expenseRequest.setDescription(requestDto.getDescription());
        expenseRequest.setStatus(RequestStatus.PENDING);
        expenseRequest.setSubmitDate(LocalDate.now());

        ExpenseRequest saved = expenseRequestRepository.save(expenseRequest);
        return convertToDto(saved);
    }

    @Override
    public ExpenseRequestDto updateRequest(Long id, RequestStatus status) {
        ExpenseRequest expenseRequest = expenseRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense request not found with ID: " + id));

        expenseRequest.setStatus(status);
        ExpenseRequest updated = expenseRequestRepository.save(expenseRequest);

        return enhanceExpenseDto(convertToDto(updated));
    }

    @Override
    public List<ExpenseRequestDto> getUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        List<ExpenseRequest> requests = expenseRequestRepository.findByUserIdOrderBySubmitDateDesc(userId);
        return requests.stream()
                .map(request -> enhanceExpenseDto(convertToDto(request)))
                .collect(Collectors.toList());
    }

    // Add method to get all expense requests for admins
    public List<ExpenseRequestDto> getAllExpenses() {
        List<ExpenseRequest> requests = expenseRequestRepository.findAllOrderByStatusAndSubmitDate();
        return requests.stream()
                .map(request -> enhanceExpenseDto(convertToDto(request)))
                .collect(Collectors.toList());
    }


    private ExpenseRequestDto convertToDto(ExpenseRequest expenseRequest) {
        ExpenseRequestDto dto = new ExpenseRequestDto();
        dto.setId(expenseRequest.getId().toString());
        dto.setAmount(expenseRequest.getAmount());
        dto.setType(expenseRequest.getType().toString());
        dto.setDescription(expenseRequest.getDescription());
        dto.setStatus(expenseRequest.getStatus().toString());

        // Handle the date fields correctly
        if (expenseRequest.getSubmitDate() != null) {
            dto.setSubmittedDate(expenseRequest.getSubmitDate().format(dateFormatter));
        } else {
            dto.setSubmittedDate(LocalDate.now().format(dateFormatter));
        }

        // Add approved date if status is approved
        if (expenseRequest.getStatus() == RequestStatus.APPROVED) {
            dto.setApprovedDate(LocalDate.now().format(dateFormatter));
        }

        AppUser user = expenseRequest.getAppUser();
        dto.setEmployeeId(user.getEmployeeId().toString());
        dto.setEmployeeName(user.getFirstName() + " " + user.getLastName());

        return dto;
    }

    // Remove the enhanceExpenseDto method that's causing conflicts, or modify it:
    private ExpenseRequestDto enhanceExpenseDto(ExpenseRequestDto dto) {
        // Add approved date if not present and status is approved
        if (dto.getApprovedDate() == null && "APPROVED".equals(dto.getStatus())) {
            dto.setApprovedDate(LocalDate.now().format(dateFormatter));
        }
        return dto;
    }
}