package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import org.banta.xardhr.domain.enums.AttendanceStatus;

import java.time.LocalDateTime;

@Entity
public class Attendance {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Employee employee;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    private String notes;
    private String location;
}
