package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.ExpenseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRequestRepository extends JpaRepository<ExpenseRequest, Long> {

    @Query("SELECT e FROM ExpenseRequest e WHERE e.appUser.id = ?1 ORDER BY e.submitDate DESC")
    List<ExpenseRequest> findByUserIdOrderBySubmitDateDesc(Long userId);

    @Query("SELECT e FROM ExpenseRequest e ORDER BY e.status, e.submitDate DESC")
    List<ExpenseRequest> findAllOrderByStatusAndSubmitDate();
}