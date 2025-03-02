package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.LeaveType;
import org.banta.xardhr.domain.enums.RequestStatus;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private String id;
    private String employeeId;
    private String employeeName;
    private String type; // Change from enum to String
    private String startDate; // Change from LocalDate to String
    private String endDate; // Change from LocalDate to String
    private String status; // Change from enum to String
    private String reason;
    private String appliedOn; // This is already in the existing DTO
    private Integer totalDays;
}
