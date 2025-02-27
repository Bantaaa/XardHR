package org.banta.xardhr.service.expense.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.ExpenseRequest;
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
        expenseRequest.setType(requestDto.getType());
        expenseRequest.setDescription(requestDto.getDescription());
        expenseRequest.setStatus(RequestStatus.PENDING);
        expenseRequest.setSubmitDate(LocalDate.now());

        ExpenseRequest saved = expenseRequestRepository.save(expenseRequest);
        return enhanceExpenseDto(convertToDto(saved));
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

    // Convert entity to basic DTO
    private ExpenseRequestDto convertToDto(ExpenseRequest expenseRequest) {
        ExpenseRequestDto dto = new ExpenseRequestDto();
        dto.setId(expenseRequest.getId().toString());
        dto.setAmount(expenseRequest.getAmount());
        dto.setType(expenseRequest.getType());
        dto.setDescription(expenseRequest.getDescription());
        dto.setStatus(expenseRequest.getStatus());

        // Add submit date and handle date formatting
        if (expenseRequest.getSubmitDate() != null) {
            dto.setSubmitDate(expenseRequest.getSubmitDate());
        }

        AppUser user = expenseRequest.getAppUser();
        dto.setEmployeeId(user.getEmployeeId().toString());
        dto.setEmployeeName(user.getFirstName() + " " + user.getLastName());

        return dto;
    }

    // Enhance DTO with additional fields needed by frontend
    private ExpenseRequestDto enhanceExpenseDto(ExpenseRequestDto dto) {
        // Format dates to match frontend expectations
        if (dto.getSubmitDate() == null && dto.getSubmittedDate() == null) {
            dto.setSubmittedDate(LocalDate.now().format(dateFormatter));
        } else if (dto.getSubmitDate() != null) {
            dto.setSubmittedDate(dto.getSubmitDate().format(dateFormatter));
        }

        return dto;
    }
}