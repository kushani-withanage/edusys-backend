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
    private String submissionId;
    private String taskId;
    private String studentId;
    private String status;
    private String submittedFile;
    private LocalDateTime submitDate;
}
