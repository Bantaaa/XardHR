package org.banta.xardhr.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class BankAccount {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private AppUser appUser;
    private String accountNumber;
    private String bankName;
    private String branchCode;
}