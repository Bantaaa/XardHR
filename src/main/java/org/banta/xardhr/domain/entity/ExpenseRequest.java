package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.banta.xardhr.domain.enums.ExpenseType;
import org.banta.xardhr.domain.enums.RequestStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ExpenseRequest {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    private Double amount;
    private String description;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @Enumerated(EnumType.STRING)
    private ExpenseType type;
    private LocalDate submitDate;
    private String receiptUrl;
}