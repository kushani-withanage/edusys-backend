package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDTO {
    private String id;
    private String title;
    private String description;
    private String courseId; // module_id
    private String createdBy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private Boolean shuffleQuestions;
    private Boolean shuffleOptions;
    private Integer attemptsAllowed;
    private String status; // DRAFT, PUBLISHED, CLOSED
    private LocalDateTime createdAt;
    
    // Auxiliary fields for payload
    private List<String> questionIds;
    private List<ExamAudienceDTO> audiences;
    private Integer totalMarks;
}
