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
    private LeaveType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private RequestStatus status;
    private String reason;
    private String appliedOn;
    private Integer totalDays;
}
