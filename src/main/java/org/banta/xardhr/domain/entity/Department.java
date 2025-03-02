package org.banta.xardhr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser departmentHead;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department parentDepartment;

    // Use @JsonIgnore to prevent circular references in JSON
    @JsonIgnore
    @OneToMany(mappedBy = "parentDepartment")
    private Set<Department> childDepartments;
}