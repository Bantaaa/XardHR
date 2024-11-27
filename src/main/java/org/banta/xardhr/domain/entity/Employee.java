package org.banta.xardhr.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import org.banta.xardhr.domain.enums.EmployeeStatus;
import org.banta.xardhr.domain.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Employee extends User {
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private UUID employeeId;
    @ManyToOne
    private Department department;
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;
    private LocalDate joiningDate;
    private Double baseSalary;
    private String position;
    private String contactNumber;
}
