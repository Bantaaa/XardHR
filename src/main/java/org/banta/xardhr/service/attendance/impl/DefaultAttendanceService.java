package org.banta.xardhr.service.attendance.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.Attendance;
import org.banta.xardhr.dto.request.AttendanceDto;
import org.banta.xardhr.repository.AttendanceRepository;
import org.banta.xardhr.service.attendance.AttendanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultAttendanceService implements AttendanceService {
    private final AttendanceRepository attendanceRepository;

    @Override
    public AttendanceDto checkIn(UUID userId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        if (attendanceRepository.findByEmployeeIdAndCheckInBetween(userId, startOfDay, endOfDay).isPresent()) {
            throw new RuntimeException("User already checked in");
        } else {
            AttendanceDto attendanceDto = new AttendanceDto();
            attendanceDto.setUserId(userId);
            attendanceDto.setCheckIn(LocalDateTime.now());
            Attendance attendance = convertToEntity(attendanceDto);
            attendanceRepository.save(attendance);
            return attendanceDto;
        }
    }

    private Attendance convertToEntity(AttendanceDto attendanceDto) {
        Attendance attendance = new Attendance();
        attendance.setCheckIn(attendanceDto.getCheckIn());
        attendance.setCheckOut(attendanceDto.getCheckOut());
        attendance.setStatus(attendanceDto.getStatus());
        attendance.setLocation(attendanceDto.getLocation());
        return attendance;
    }

    @Override
    public AttendanceDto checkOut(UUID userId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Attendance attendance = attendanceRepository.findByEmployeeIdAndCheckInBetween(userId, startOfDay, endOfDay)
                .orElseThrow(() -> new RuntimeException("User has not checked in"));
        attendance.setCheckOut(LocalDateTime.now());
        attendanceRepository.save(attendance);
        return convertToDto(attendance);

    }

    private AttendanceDto convertToDto(Attendance attendance) {
        AttendanceDto dto = new AttendanceDto();
        dto.setUserId(attendance.getEmployee().getId());
        dto.setCheckIn(attendance.getCheckIn());
        dto.setCheckOut(attendance.getCheckOut());
        dto.setStatus(attendance.getStatus());
        dto.setLocation(attendance.getLocation());
        return dto;
    }

    @Override
    public List<AttendanceDto> getUserAttendance(UUID userId) {
        return attendanceRepository.findByEmployeeIdOrderByCheckInDesc(userId);
    }
}
