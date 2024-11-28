package org.banta.xardhr.dto.request;

import lombok.Data;
import org.banta.xardhr.domain.enums.ExpenseType;

@Data
public class ExpenseRequestDto {
    private Double amount;
    private ExpenseType type;
    private String description;
}
