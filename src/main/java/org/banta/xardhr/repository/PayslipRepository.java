package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {
}
