package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.AttendanceStatus;

import java.time.LocalDateTime;

@Data
public class AttendanceDto {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private AttendanceStatus status;
    private String location;
}
