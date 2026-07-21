package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "evaluations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationEntity {

    @Id
    @Column(name = "evaluation_id", length = 36)
    private String evaluationId;

    @Column(name = "submission_id", nullable = false, unique = true, length = 36)
    private String submissionId;

    @Column(name = "reviewer_id", length = 36)
    private String reviewerId;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "override_reason", columnDefinition = "TEXT")
    private String overrideReason;

    @Column(name = "evaluation_date")
    private LocalDate evaluationDate;

    @Column(name = "override_flag")
    private Boolean overrideFlag;
}
