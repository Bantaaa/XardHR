package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne
    private AppUser departmentHead;
    private String description;
    @ManyToOne
    private Department parentDepartment;
}