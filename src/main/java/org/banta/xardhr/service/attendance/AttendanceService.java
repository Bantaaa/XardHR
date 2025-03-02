package org.banta.xardhr.service.attendance;

import org.banta.xardhr.domain.entity.Attendance;
import org.banta.xardhr.dto.request.AttendanceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {
    AttendanceDto checkIn(Long userId);
    AttendanceDto checkOut(Long userId);
    List<AttendanceDto> getUserAttendance(Long userId);
    AttendanceDto convertToDto(Attendance attendance);
}
