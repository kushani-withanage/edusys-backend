package com.edusys.service;

import com.edusys.model.dto.ExamDTO;
import com.edusys.model.dto.QuestionBankDTO;
import com.edusys.model.dto.ExamAttemptDTO;
import com.edusys.model.dto.ExamSubmissionDTO;

import java.util.List;

public interface ExamService {
    ExamDTO create(ExamDTO examDTO);
    ExamDTO getById(String id);
    List<ExamDTO> getAll();
    ExamDTO update(String id, ExamDTO examDTO);
    boolean delete(String id);
    List<QuestionBankDTO> getQuestionsForExam(String examId);
    ExamAttemptDTO submitExam(String examId, ExamSubmissionDTO submission);
}
