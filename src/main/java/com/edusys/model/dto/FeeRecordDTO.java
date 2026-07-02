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
public class FeeRecordDTO {
    private String feeId;
    private String studentId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String feeType;
    private String status;
}
