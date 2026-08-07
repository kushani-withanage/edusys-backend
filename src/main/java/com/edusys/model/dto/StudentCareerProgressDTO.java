package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCareerProgressDTO {
    private String studentId;
    private String studentName;
    private String currentLevelId;
    private Integer currentLevelNumber;
    private String currentLevelTitle;
    private Integer totalPointsAtLevel;
    private Integer levelPointsRequired;
    private LocalDateTime levelAchievedAt;
}
