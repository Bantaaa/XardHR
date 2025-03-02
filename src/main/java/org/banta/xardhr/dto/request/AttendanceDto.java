package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.AttendanceStatus;

import java.time.LocalDateTime;

@Data
public class AttendanceDto {
    private String id;
    private String employeeId;
    private String employeeName;
    private String date; // Change from LocalDate to String
    private String checkIn; // Change from LocalDateTime to String
    private String checkOut; // Change from LocalDateTime to String
    private String status; // Change from enum to String
    private Double totalHours;
    private String location;
}
