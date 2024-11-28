package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.banta.xardhr.domain.enums.EmployeeStatus;
import org.banta.xardhr.domain.enums.Gender;
import org.banta.xardhr.domain.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDateTime lastLogin;
    private Boolean isActive = true;

    // Employee specific fields
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private UUID employeeId = UUID.randomUUID();

    @ManyToOne
    private Department department;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    private LocalDate joiningDate = LocalDate.now();
    private Double baseSalary;
    private String position;
    private String contactNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}