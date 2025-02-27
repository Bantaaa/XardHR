package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
    List<Attendance> findByEmployeeIdOrderByDateDesc(Long employeeId);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByDateOrderByCheckInDesc(LocalDate date);
}