package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankDTO {
    private String questionId;
    private String questionType;
    private String questionText;
    private List<String> options;
    private Integer marks;
    private List<String> correctAnswers;
    private String createdBy;
}
