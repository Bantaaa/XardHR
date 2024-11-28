package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.PayslipStatus;

import java.time.LocalDate;

@Data
public class PayslipDto {
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Double allowances;
    private Double deductions;
    private Double netSalary;
    private PayslipStatus status;
}
