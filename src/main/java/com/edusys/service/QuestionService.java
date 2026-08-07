package com.edusys.service;

import com.edusys.model.dto.QuestionDTO;
import java.util.List;

public interface QuestionService {
    QuestionDTO create(QuestionDTO dto);
    QuestionDTO getById(String id);
    List<QuestionDTO> getAll();
    QuestionDTO update(String id, QuestionDTO dto);
    boolean delete(String id);
    List<QuestionDTO> getQuestions(String courseId, String difficulty, String status);
    List<QuestionDTO> importQuestionsFromCsv(String courseId, String createdBy, String csvContent);
}
