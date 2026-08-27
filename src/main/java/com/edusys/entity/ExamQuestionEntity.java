package com.edusys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "exam_questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionEntity {

    @EmbeddedId
    private ExamQuestionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("examId")
    @JoinColumn(name = "exam_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExamEntity exam;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @Column(name = "marks_override")
    private Integer marksOverride;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ExamQuestionId implements Serializable {
        @Column(name = "exam_id", length = 36)
        private String examId;

        @Column(name = "question_id", length = 36)
        private String questionId;
    }
}
