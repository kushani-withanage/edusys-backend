package com.edusys.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "course_id", nullable = false, length = 36)
    private String courseId;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "question_type", nullable = false, length = 20)
    private String questionType; // SINGLE_CHOICE, MULTI_CHOICE

    @Column(name = "difficulty", nullable = false, length = 10)
    private String difficulty; // EASY, MEDIUM, HARD

    @Column(name = "default_marks", nullable = false)
    private Integer defaultMarks;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // DRAFT, ACTIVE, ARCHIVED, LOCKED

    @Column(name = "created_by", nullable = false, length = 36)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestionOptionEntity> options;
}
