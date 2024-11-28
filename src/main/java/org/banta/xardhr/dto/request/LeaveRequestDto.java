package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.LeaveType;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType type;
    private String reason;
}
