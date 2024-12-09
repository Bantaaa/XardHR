package org.banta.xardhr.service.attendance;

import org.banta.xardhr.dto.request.AttendanceDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface AttendanceService {
    AttendanceDto checkIn(UUID userId);
    AttendanceDto checkOut(UUID userId);
    List<AttendanceDto> getUserAttendance(UUID userId);
}
