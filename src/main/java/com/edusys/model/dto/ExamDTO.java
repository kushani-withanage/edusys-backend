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
public class ExamDTO {
    private String examId;
    private String title;
    private LocalDateTime startTime;
    private Integer durationMinutes;
    private Integer totalMarks;
    private String createdBy;
}
