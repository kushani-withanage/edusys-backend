package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerPointsLedgerDTO {
    private String ledgerId;
    private String studentId;
    private String taskId;
    private Integer pointsAwarded;
    private LocalDate entryDate;
    private String remarks;
}
