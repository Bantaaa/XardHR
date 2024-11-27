package org.banta.xardhr.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BankAccount {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Employee employee;
    private String accountNumber;
    private String bankName;
    private String branchCode;
}