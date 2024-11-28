package org.banta.xardhr.dto.request;

import lombok.Data;

@Data
public class BankAccountDto {
    private String accountNumber;
    private String bankName;
    private String branchCode;
}
