package org.banta.xardhr.web.rest;

import lombok.RequiredArgsConstructor;
import org.banta.xardhr.dto.request.AttendanceDto;
import org.banta.xardhr.service.attendance.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD')")
    public ResponseEntity<AttendanceDto> checkIn(@RequestParam Long userId) {
        return ResponseEntity.ok(attendanceService.checkIn(userId));
    }

    @PostMapping("/check-out")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'DEPT_HEAD')")
    public ResponseEntity<AttendanceDto> checkOut(@RequestParam Long userId) {
        return ResponseEntity.ok(attendanceService.checkOut(userId));
    }

    @GetMapping("/employee/{userId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN') or @securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<AttendanceDto>> getUserAttendance(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getUserAttendance(userId));
    }
}