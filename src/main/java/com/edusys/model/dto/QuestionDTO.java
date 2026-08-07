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
public class QuestionDTO {
    private String id;
    private String courseId;
    private String questionText;
    private String questionType; // SINGLE_CHOICE, MULTI_CHOICE
    private String difficulty;    // EASY, MEDIUM, HARD
    private Integer defaultMarks;
    private String status;        // DRAFT, ACTIVE, ARCHIVED, LOCKED
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuestionOptionDTO> options;
}
