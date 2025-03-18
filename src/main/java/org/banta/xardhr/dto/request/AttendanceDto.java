package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.AttendanceStatus;

import java.time.LocalDateTime;

@Data
public class AttendanceDto {
    private String id;
    private String employeeId;
    private String employeeName;
    private String date;
    private String checkIn;
    private String checkOut;
    private String status;
    private Double totalHours;
    private String location;
}
