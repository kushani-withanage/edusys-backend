package com.edusys.service;

import com.edusys.model.dto.EvaluationDTO;

import java.util.List;

public interface EvaluationService {
    EvaluationDTO create(EvaluationDTO dto);
    EvaluationDTO getById(String id);
    List<EvaluationDTO> getAll();
    EvaluationDTO update(String id, EvaluationDTO dto);
    boolean delete(String id);
}
