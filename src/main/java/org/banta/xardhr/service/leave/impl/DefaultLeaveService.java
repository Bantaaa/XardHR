package org.banta.xardhr.service.leave.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.LeaveRequest;
import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.LeaveRequestDto;
import org.banta.xardhr.repository.LeaveRequestRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.leave.LeaveService;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultLeaveService implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LeaveRequestDto submitRequest(Long userId, LeaveRequestDto request) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Validate request dates
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Start date cannot be in the past");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        // Calculate total days
        int totalDays = calculateDays(request.getStartDate(), request.getEndDate());

        // Create leave request
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setAppUser(user);
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setType(request.getType());
        leaveRequest.setReason(request.getReason());
        leaveRequest.setStatus(RequestStatus.PENDING);

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);

        // Convert to DTO with additional fields for frontend
        return enhanceLeaveRequestDto(convertToDto(saved), totalDays);
    }

    @Override
    public LeaveRequestDto updateRequest(Long id, RequestStatus status) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + id));

        leaveRequest.setStatus(status);
        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);

        // Calculate total days
        int totalDays = calculateDays(updated.getStartDate(), updated.getEndDate());

        // Convert to DTO with additional fields for frontend
        return enhanceLeaveRequestDto(convertToDto(updated), totalDays);
    }

    @Override
    public List<LeaveRequestDto> getUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        List<LeaveRequest> requests = leaveRequestRepository.findByUserIdOrderByStartDateDesc(userId);
        return requests.stream()
                .map(request -> {
                    int totalDays = calculateDays(request.getStartDate(), request.getEndDate());
                    return enhanceLeaveRequestDto(convertToDto(request), totalDays);
                })
                .collect(Collectors.toList());
    }

    // Add method to get all leave requests for admins
    public List<LeaveRequestDto> getAllLeaves() {
        List<LeaveRequest> requests = leaveRequestRepository.findAll();
        return requests.stream()
                .map(request -> {
                    int totalDays = calculateDays(request.getStartDate(), request.getEndDate());
                    return enhanceLeaveRequestDto(convertToDto(request), totalDays);
                })
                .collect(Collectors.toList());
    }

    // Calculate days between two dates (inclusive)
    private int calculateDays(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays() + 1; // +1 to include both start and end dates
    }

    // Convert entity to basic DTO
    private LeaveRequestDto convertToDto(LeaveRequest leaveRequest) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leaveRequest.getId().toString());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setType(leaveRequest.getType());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus());

        AppUser user = leaveRequest.getAppUser();
        dto.setEmployeeId(user.getEmployeeId().toString());
        dto.setEmployeeName(user.getFirstName() + " " + user.getLastName());

        return dto;
    }

    // Enhance DTO with additional fields needed by frontend
    private LeaveRequestDto enhanceLeaveRequestDto(LeaveRequestDto dto, int totalDays) {
        dto.setTotalDays(totalDays);

        // Format the applied date to match frontend expectations
        dto.setAppliedOn(LocalDate.now().format(dateFormatter));

        return dto;
    }
}