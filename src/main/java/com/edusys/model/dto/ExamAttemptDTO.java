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
public class ExamAttemptDTO {
    private String attemptId;
    private String examId;
    private String studentId;
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    private String status;
    private Double score;
}
