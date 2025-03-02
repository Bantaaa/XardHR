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
    private String type; // Change from enum to String
    private Double amount;
    private String description;
    private String status; // Change from enum to String
    private String submittedDate; // IMPORTANT: Rename to match frontend expectations
    // Remove submitDate field or make it transient to avoid conflicts
    private String approvedDate; // Add this field for frontend compatibility
}