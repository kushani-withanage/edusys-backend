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
@Table(name = "exam_attempts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptEntity {

    @Id
    @Column(name = "attempt_id", length = 36)
    private String attemptId;

    @Column(name = "exam_id", nullable = false, length = 36)
    private String examId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "submit_time")
    private LocalDateTime submitTime;

    @Column(name = "status")
    private String status;

    @Column(name = "score")
    private Double score;
}
