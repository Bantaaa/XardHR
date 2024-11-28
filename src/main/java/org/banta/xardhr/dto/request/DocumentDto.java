package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.DocumentType;

import java.time.LocalDate;

@Data
public class DocumentDto {
    private String title;
    private DocumentType type;
    private String fileUrl;
    private LocalDate uploadDate;
    private LocalDate expiryDate;
    private Boolean isVerified;
}
