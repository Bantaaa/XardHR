package org.banta.xardhr.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.banta.xardhr.domain.enums.DocumentType;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private AppUser appUser;
    private String title;
    @Enumerated(EnumType.STRING)
    private DocumentType type;
    private String fileUrl;
    private LocalDate uploadDate;
    private LocalDate expiryDate;
    private Boolean isVerified;
}
