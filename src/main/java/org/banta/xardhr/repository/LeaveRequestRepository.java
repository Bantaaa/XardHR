package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("SELECT l FROM LeaveRequest l WHERE l.appUser.id = ?1 ORDER BY l.startDate DESC")
    List<LeaveRequest> findByUserIdOrderByStartDateDesc(Long userId);

    @Query("SELECT l FROM LeaveRequest l ORDER BY l.status, l.startDate DESC")
    List<LeaveRequest> findAllOrderByStatusAndStartDate();
}