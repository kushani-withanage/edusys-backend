package com.edusys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamEntity {

    @Id
    @Column(name = "exam_id", length = 36)
    private String examId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "total_marks")
    private Integer totalMarks;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "exam_question",
        joinColumns = @JoinColumn(name = "exam_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<QuestionBankEntity> questions;

    @Column(name = "created_by", length = 36)
    private String createdBy;
}
