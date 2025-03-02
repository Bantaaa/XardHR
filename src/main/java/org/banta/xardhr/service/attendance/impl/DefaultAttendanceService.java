package org.banta.xardhr.service.attendance.impl;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.Attendance;
import org.banta.xardhr.domain.enums.AttendanceStatus;
import org.banta.xardhr.dto.request.AttendanceDto;
import org.banta.xardhr.repository.AttendanceRepository;
import org.banta.xardhr.repository.UserRepository;
import org.banta.xardhr.service.attendance.AttendanceService;
import org.banta.xardhr.web.errors.exception.BadRequestException;
import org.banta.xardhr.web.errors.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAttendanceService implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Override
    public AttendanceDto checkIn(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Check if the user already has an active attendance record for today
        LocalDate today = LocalDate.now();
        List<Attendance> todaysRecords = attendanceRepository.findByEmployeeIdAndDate(userId, today);

        for (Attendance record : todaysRecords) {
            if (record.getCheckOut() == null) {
                throw new BadRequestException("User already has an active check-in");
            }
        }

        // Create new attendance record
        Attendance attendance = new Attendance();
        attendance.setEmployee(user);
        attendance.setCheckIn(LocalDateTime.now());
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance.setDate(today);

        Attendance saved = attendanceRepository.save(attendance);
        return convertToDto(saved);
    }

    @Override
    public AttendanceDto checkOut(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Find the user's active attendance record
        LocalDate today = LocalDate.now();
        List<Attendance> todaysRecords = attendanceRepository.findByEmployeeIdAndDate(userId, today);

        Attendance activeRecord = todaysRecords.stream()
                .filter(record -> record.getCheckOut() == null)
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No active check-in found"));

        // Check out
        LocalDateTime checkOutTime = LocalDateTime.now();
        activeRecord.setCheckOut(checkOutTime);

        // Calculate duration and update status if needed
        Duration duration = Duration.between(activeRecord.getCheckIn(), checkOutTime);
        double hours = duration.toMinutes() / 60.0;

        if (hours < 4.0) {
            activeRecord.setStatus(AttendanceStatus.HALF_DAY);
        } else if (activeRecord.getCheckIn().getHour() > 9) {
            activeRecord.setStatus(AttendanceStatus.LATE);
        } else {
            activeRecord.setStatus(AttendanceStatus.PRESENT);
        }

        Attendance saved = attendanceRepository.save(activeRecord);
        return convertToDto(saved);
    }

    @Override
    public List<AttendanceDto> getUserAttendance(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        List<Attendance> records = attendanceRepository.findByEmployeeIdOrderByDateDesc(userId);
        return records.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceDto> getTodayAttendance() {
        LocalDate today = LocalDate.now();
        List<Attendance> records = attendanceRepository.findByDateOrderByCheckInDesc(today);
        return records.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceDto> getAllAttendance() {
        List<Attendance> records = attendanceRepository.findAll();
        return records.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDto convertToDto(Attendance attendance) {
        AttendanceDto dto = new AttendanceDto();
        dto.setId(attendance.getId().toString());
        dto.setEmployeeId(attendance.getEmployee().getEmployeeId().toString());
        dto.setEmployeeName(attendance.getEmployee().getFirstName() + " " +
                attendance.getEmployee().getLastName());
        dto.setDate(attendance.getDate().toString());
        dto.setCheckIn(attendance.getCheckIn() != null ? attendance.getCheckIn().toString() : null);
        dto.setCheckOut(attendance.getCheckOut() != null ? attendance.getCheckOut().toString() : null);
        dto.setStatus(attendance.getStatus() != null ? attendance.getStatus().toString() : null);

        // Calculate total hours if check-out is recorded
        if (attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
            Duration duration = Duration.between(attendance.getCheckIn(), attendance.getCheckOut());
            double hours = duration.toMinutes() / 60.0;
            dto.setTotalHours(Math.round(hours * 100.0) / 100.0); // Round to 2 decimal places
        } else {
            dto.setTotalHours(0.0);
        }

        dto.setLocation(attendance.getLocation());

        return dto;
    }
}