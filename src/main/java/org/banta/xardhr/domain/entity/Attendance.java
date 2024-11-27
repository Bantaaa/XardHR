package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.banta.xardhr.domain.enums.AttendanceStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
