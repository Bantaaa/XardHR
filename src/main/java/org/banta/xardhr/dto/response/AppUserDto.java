package org.banta.xardhr.dto.response;

import lombok.Data;
import org.banta.xardhr.domain.enums.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AppUserDto {
    private Long id; // Added to match frontend expectation
    private String username; // This is treated as email in the frontend
    private UserRole role;
    private String firstName;
    private String lastName;
    private Gender gender;
    private UUID employeeId;
    private EmployeeStatus status;
    private LocalDate joiningDate;
    private Double baseSalary;
    private String position;
    private String contactNumber;
    private Long departmentId;
    private String department; // Added department name for frontend
    // Added email field for frontend consistency
    private String email;
}