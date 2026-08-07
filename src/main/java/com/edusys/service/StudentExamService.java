package com.edusys.service;

import com.edusys.model.dto.ExamDTO;
import com.edusys.model.dto.QuestionDTO;
import java.util.List;
import java.util.Map;

public interface StudentExamService {
    List<Map<String, Object>> getAvailableExams(String studentId);
    Map<String, Object> startOrResumeAttempt(String examId, String studentId);
    void saveAnswer(String attemptId, String studentId, String questionId, List<String> selectedOptionIds);
    Map<String, Object> submitAttempt(String attemptId, String studentId);
    Map<String, Object> getAttemptResult(String attemptId, String studentId);
}
