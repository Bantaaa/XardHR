package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.AppUser;
import org.banta.xardhr.domain.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(AppUser user);
    void deleteByUser(AppUser user);
}