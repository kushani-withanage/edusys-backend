package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    private String receiptId;
    private String receiptNo;
    private String feeId;
    private LocalDate paymentDate;
    private BigDecimal amountPaid;
    private String paymentMethod;
}
