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

    private String id;
    private String firstName;
    private String lastName;
    private String employeeId;
    private String position;
    private String contactNumber;
    private String status;
    private String departmentId;
    private Boolean passwordResetRequired;
}