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
public class BatchModuleMatrixDTO {
    private String courseId;
    private String courseName;
    private String batchId;
    private String batchName;
    private String accessType; // "Standard" or "Custom"
    private int studentCount;
    private LocalDate grantedAt;
}
