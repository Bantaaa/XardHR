package org.banta.xardhr.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional.ofNullable(extractToken(request))
                .filter(this::isValidToken)
                .ifPresent(token -> {
                    Claims claims = extractClaims(token);
                    List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    claims.getSubject(),
                                    null,
                                    authorities
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ")
                ? header.substring(7)
                : null;
    }

    private boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        // Example of extracting roles/authorities from claims
        // Modify based on your actual token structure
        Object roles = claims.get("roles");
        return roles != null
                ? Collections.singletonList(new SimpleGrantedAuthority(roles.toString()))
                : Collections.emptyList();
    }
}