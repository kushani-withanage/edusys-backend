package com.edusys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exam_attempts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "exam_id", nullable = false, length = 36)
    private String examId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // IN_PROGRESS, SUBMITTED, AUTO_SUBMITTED

    @Column(name = "score")
    private Double score;

    @Column(name = "question_order", columnDefinition = "TEXT")
    private String questionOrder; // Stores JSON list mapping shuffled order of options & questions for the student

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ExamAnswerEntity> answers;
}
