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
public class AssignmentSubmissionDTO {
    private String submissionId;
    private String assignmentId;
    private String studentId;
    private LocalDateTime submitDate;
    private String submittedFile;
    private Double marks;
    private String gradedBy;
    private String feedback;
}
