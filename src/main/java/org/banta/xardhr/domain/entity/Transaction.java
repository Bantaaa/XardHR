package org.banta.xardhr.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private BankAccount bankAccount;
    private LocalDateTime timestamp;
    private String type;
    private Double amount;
    private String description;
}
