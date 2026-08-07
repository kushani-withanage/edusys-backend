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
public class CareerSubmissionDTO {
    private String id;
    private String taskId;
    private String taskTitle;
    private Integer taskPointsValue;
    private String studentId;
    private String studentName;
    private String submissionType;
    private String submissionUrl;
    private String filePath;
    private String submissionText;
    private String status; // PENDING, APPROVED, REJECTED, REVISION_REQUESTED
    private Integer pointsAwarded;
    private String reviewerId;
    private String reviewerComment;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String resubmissionOf;
}
