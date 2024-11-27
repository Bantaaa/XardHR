package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;

@Entity
public class Department {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne
    private Employee departmentHead;
    private String description;
    @ManyToOne
    private Department parentDepartment;
}