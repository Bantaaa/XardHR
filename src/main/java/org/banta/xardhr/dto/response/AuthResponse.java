package org.banta.xardhr.dto.response;

import lombok.Builder;
import lombok.Data;
import org.banta.xardhr.domain.enums.UserRole;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private UserRole role;
}