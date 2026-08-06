package com.edusys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "question_bank")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankEntity {

    @Id
    @Column(name = "question_id", length = 36)
    private String questionId;

    @Column(name = "question_type")
    private String questionType;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "marks")
    private Integer marks;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_value")
    private List<String> options;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_correct_answers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "correct_answer")
    private List<String> correctAnswers;

    @Column(name = "created_by", length = 36)
    private String createdBy;
}
