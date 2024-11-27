package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.banta.xardhr.domain.enums.LeaveType;
import org.banta.xardhr.domain.enums.RequestStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class LeaveRequest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Employee employee;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @Enumerated(EnumType.STRING)
    private LeaveType type;
    private String reason;
    private String comments;
}
