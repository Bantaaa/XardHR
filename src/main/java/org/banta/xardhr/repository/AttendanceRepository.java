package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.Attendance;
import org.banta.xardhr.dto.request.AttendanceDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeIdAndCheckInBetween(
            UUID employeeId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    List<AttendanceDto> findByEmployeeIdOrderByCheckInDesc(UUID employeeId);

}
