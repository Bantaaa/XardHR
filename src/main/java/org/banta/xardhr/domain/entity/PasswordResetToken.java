package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser user;

    private LocalDateTime expiryDate;

    // Default 1 hour expiration
    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusHours(1);
    }

    // Constructors
    public PasswordResetToken() {
        this.token = UUID.randomUUID().toString();
        this.expiryDate = calculateExpiryDate();
    }

    public PasswordResetToken(AppUser user) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        this.expiryDate = calculateExpiryDate();
        System.out.println("Generated token: " + this.token + " for user: " + user.getUsername() + " expires at: " + this.expiryDate);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}