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
public class EvaluationDTO {
    private String evaluationId;
    private String submissionId;
    private String reviewerId;
    private String comments;
    private String overrideReason;
    private LocalDate evaluationDate;
    private Boolean overrideFlag;
}
