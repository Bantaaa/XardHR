package org.banta.xardhr.service.leave.impl;

import org.banta.xardhr.domain.entity.LeaveRequest;
import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.LeaveRequestDto;
import org.banta.xardhr.service.leave.LeaveService;

import java.util.List;

public class DefaultLeaveService implements LeaveService {

    @Override
    public LeaveRequestDto submitRequest(Long userId, LeaveRequestDto request) {
        return null;
    }

    @Override
    public LeaveRequestDto updateRequest(Long id, RequestStatus status) {
        return null;
    }

    @Override
    public List<LeaveRequestDto> getUserRequests(Long userId) {
        return List.of();
    }
}
