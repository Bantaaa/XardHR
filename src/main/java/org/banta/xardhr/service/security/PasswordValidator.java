package org.banta.xardhr.service.security;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }

    public String getPasswordRequirements() {
        return """
            Password must contain:
            - At least 8 characters
            - At least one digit
            - At least one lowercase letter
            - At least one uppercase letter
            - At least one special character (@#$%^&+=)
            - No whitespace
            """;
    }
}