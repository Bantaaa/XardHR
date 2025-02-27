package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.ExpenseType;
import org.banta.xardhr.domain.enums.RequestStatus;

import java.time.LocalDate;

@Data
public class ExpenseRequestDto {
    private String id;
    private String employeeId;
    private String employeeName;
    private ExpenseType type;
    private Double amount;
    private String description;
    private RequestStatus status;
    private String submittedDate;
    private String approvedDate;

    private LocalDate submitDate;
}