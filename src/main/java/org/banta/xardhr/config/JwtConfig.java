package org.banta.xardhr.config;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.banta.xardhr.web.errors.exception.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Getter
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        validateAndInitializeKey();
    }

    private void validateAndInitializeKey() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new JwtException("JWT secret cannot be null or empty.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));

            if (keyBytes.length < 32) {
                throw new JwtException("JWT secret key must be at least 256 bits (32 bytes) for HS256.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new JwtException("Error decoding JWT secret: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}