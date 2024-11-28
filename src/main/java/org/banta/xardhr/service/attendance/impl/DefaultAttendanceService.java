package org.banta.xardhr.service.attendance.impl;

import org.banta.xardhr.dto.request.AttendanceDto;
import org.banta.xardhr.service.attendance.AttendanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAttendanceService implements AttendanceService {

    @Override
    public AttendanceDto checkIn(Long userId) {
        return null;
    }

    @Override
    public AttendanceDto checkOut(Long userId) {
        return null;
    }

    @Override
    public List<AttendanceDto> getUserAttendance(Long userId) {
        return List.of();
    }
}
