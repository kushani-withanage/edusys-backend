package com.edusys.service;

import com.edusys.model.dto.ExamDTO;
import com.edusys.model.dto.QuestionDTO;
import java.util.List;
import java.util.Map;

public interface ExamService {
    ExamDTO create(ExamDTO dto);
    ExamDTO getById(String id);
    List<ExamDTO> getAll();
    ExamDTO update(String id, ExamDTO dto);
    boolean delete(String id);
    ExamDTO publish(String id);
    ExamDTO close(String id);
    List<QuestionDTO> getQuestionsForExam(String examId, boolean stripCorrectAnswers);
    Map<String, Object> getExamAnalytics(String examId);
}
