package org.banta.xardhr.dto.response;

import lombok.Data;
import org.banta.xardhr.domain.enums.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AppUserDto {
    private String id; // Change from Long to String
    private String username;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String gender; // Change from enum to String
    private String employeeId; // Change from UUID to String
    private String status; // Change from enum to String
    private String joiningDate; // Change from LocalDate to String
    private Double baseSalary;
    private String position;
    private String contactNumber;
    private String departmentId; // Change from Long to String
    private String department;
    private String email; // Add email field (same as username)
}