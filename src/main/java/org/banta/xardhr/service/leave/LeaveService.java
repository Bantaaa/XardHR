package org.banta.xardhr.service.leave;

import org.banta.xardhr.domain.enums.RequestStatus;
import org.banta.xardhr.dto.request.LeaveRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaveService {
    LeaveRequestDto submitRequest(Long userId, LeaveRequestDto request);
    LeaveRequestDto updateRequest(Long id, RequestStatus status);
    List<LeaveRequestDto> getUserRequests(Long userId);
}
