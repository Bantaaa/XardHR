package org.banta.xardhr.dto.request;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String currentPassword;
    private String password;
    private boolean resetPasswordRequired;
}