package org.banta.xardhr.service.attendance.impl;

import org.banta.xardhr.dto.request.AttendanceDto;
import org.banta.xardhr.service.attendance.AttendanceService;

import java.util.List;

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
