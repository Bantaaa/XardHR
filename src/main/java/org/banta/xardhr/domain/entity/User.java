package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import org.banta.xardhr.domain.enums.UserRole;

import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private LocalDateTime lastLogin;
    private Boolean isActive;
}
