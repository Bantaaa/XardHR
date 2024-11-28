package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.ExpenseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRequestRepository extends JpaRepository<ExpenseRequest, Long> {
}
