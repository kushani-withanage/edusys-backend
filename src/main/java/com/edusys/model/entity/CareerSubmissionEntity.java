package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "career_submission")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerSubmissionEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private CareerTaskEntity task;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(name = "submission_type", nullable = false, length = 20)
    private String submissionType; // LINK, IMAGE, PDF, FILE

    @Column(name = "submission_url", length = 1000)
    private String submissionUrl;

    @Column(name = "file_path", length = 1000)
    private String filePath;

    @Column(name = "status", nullable = false, length = 30)
    private String status; // PENDING, APPROVED, REJECTED, REVISION_REQUESTED

    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private UserEntity reviewer;

    @Column(name = "reviewer_comment", columnDefinition = "TEXT")
    private String reviewerComment;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
}
