package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import org.banta.xardhr.domain.enums.DocumentType;

import java.time.LocalDate;

@Entity
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Employee employee;
    private String title;
    @Enumerated(EnumType.STRING)
    private DocumentType type;
    private String fileUrl;
    private LocalDate uploadDate;
    private LocalDate expiryDate;
    private Boolean isVerified;
}
