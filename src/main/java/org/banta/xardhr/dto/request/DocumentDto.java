package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.DocumentType;

import java.time.LocalDate;

@Data
public class DocumentDto {
    private String id;
    private String title;
    private String type; // Change from enum to String
    private String employeeId;
    private String employeeName;
    private String uploadDate; // Change from LocalDate to String
    private String fileUrl;
    private String fileSize;
    private Boolean isVerified;
    private String expiryDate; // Change from LocalDate to String
}
