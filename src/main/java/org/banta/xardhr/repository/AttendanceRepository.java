package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
