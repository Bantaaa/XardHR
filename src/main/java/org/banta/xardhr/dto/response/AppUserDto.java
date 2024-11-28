package org.banta.xardhr.dto.response;

import lombok.Data;
import org.banta.xardhr.domain.enums.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AppUserDto {
    private String username;
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
}