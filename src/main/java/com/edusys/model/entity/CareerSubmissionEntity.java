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
@Table(name = "career_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerSubmissionEntity {

    @Id
    @Column(name = "submission_id", length = 36)
    private String submissionId;

    @Column(name = "task_id", nullable = false, length = 36)
    private String taskId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "status")
    private String status;

    @Column(name = "submitted_file")
    private String submittedFile;

    @Column(name = "submit_date")
    private LocalDateTime submitDate;
}
