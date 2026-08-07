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
public class CareerTaskDTO {
    private String id;
    private String levelId;
    private Integer levelNumber;
    private String levelTitle;
    private String title;
    private String description;
    private String instructions;
    private Integer pointsValue;
    private String submissionType; // LINK, IMAGE, PDF, FILE, TEXT
    private Boolean isActive;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
