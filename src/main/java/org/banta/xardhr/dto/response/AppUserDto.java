package org.banta.xardhr.dto.response;

import lombok.Data;
import org.banta.xardhr.domain.enums.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AppUserDto {
    private String id;
    private String username;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String gender;
    private String employeeId;
    private String status;
    private String joiningDate;
    private Double baseSalary;
    private String position;
    private String contactNumber;
    private String departmentId;
    private String department;
    private String email;
}