package com.edusys.service;

import com.edusys.model.dto.GradeDTO;

import java.util.List;

public interface GradeService {
    GradeDTO create(GradeDTO gradeDTO);
    GradeDTO getById(String id);
    List<GradeDTO> getAll();
    GradeDTO update(String id, GradeDTO gradeDTO);
    boolean delete(String id);
}
