package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "created_by", length = 36)
    private String createdBy;
}
