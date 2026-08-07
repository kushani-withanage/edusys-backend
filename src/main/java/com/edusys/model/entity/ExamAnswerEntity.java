package com.edusys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAnswerEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExamAttemptEntity attempt;

    @Column(name = "question_id", nullable = false, length = 36)
    private String questionId;

    @Column(name = "selected_option_ids", columnDefinition = "TEXT")
    private String selectedOptionIds; // Stores JSON list of selected option IDs

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "marks_awarded")
    private Double marksAwarded;
}
