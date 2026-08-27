package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmissionEntity {

    @Id
    @Column(name = "submission_id", length = 36)
    private String submissionId;

    @Column(name = "assignment_id", nullable = false, length = 36)
    private String assignmentId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "submit_date")
    private LocalDateTime submitDate;

    @Column(name = "submitted_file")
    private String submittedFile;

    @Column(name = "marks")
    private Double marks;

    @Column(name = "graded_by", length = 36)
    private String gradedBy;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
}
