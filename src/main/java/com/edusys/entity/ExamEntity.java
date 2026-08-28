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
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "course_id", length = 36)
    private String courseId; // module_id

    @Column(name = "created_by", nullable = false, length = 36)
    private String createdBy;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "shuffle_questions", nullable = false)
    private Boolean shuffleQuestions;

    @Column(name = "shuffle_options", nullable = false)
    private Boolean shuffleOptions;

    @Column(name = "attempts_allowed", nullable = false)
    private Integer attemptsAllowed;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // DRAFT, PUBLISHED, CLOSED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "pass_marks")
    private Integer passMarks;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<ExamQuestionEntity> examQuestions;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ExamAudienceEntity> audiences;
}
