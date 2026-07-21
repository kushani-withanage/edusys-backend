package com.edusys.service;

import com.edusys.model.dto.ExamDTO;

import java.util.List;

public interface ExamService {
    ExamDTO create(ExamDTO examDTO);
    ExamDTO getById(String id);
    List<ExamDTO> getAll();
    ExamDTO update(String id, ExamDTO examDTO);
    boolean delete(String id);
}
