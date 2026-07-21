package com.edusys.service;

import com.edusys.model.dto.ExamAttemptDTO;

import java.util.List;

public interface ExamAttemptService {
    ExamAttemptDTO create(ExamAttemptDTO examAttemptDTO);
    ExamAttemptDTO getById(String id);
    List<ExamAttemptDTO> getAll();
    ExamAttemptDTO update(String id, ExamAttemptDTO examAttemptDTO);
    boolean delete(String id);
}
