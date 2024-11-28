package org.banta.xardhr.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private LocalDateTime timestamp;
    private String type;
    private Double amount;
    private String description;
}
