package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.banta.xardhr.domain.enums.PayslipStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Payslip {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private AppUser appUser;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Double allowances;
    private Double deductions;
    private Double netSalary;
    @Enumerated(EnumType.STRING)
    private PayslipStatus status;
}
