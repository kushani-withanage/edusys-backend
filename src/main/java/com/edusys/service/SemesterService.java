package com.edusys.service;

import com.edusys.model.dto.SemesterDTO;

import java.util.List;

public interface SemesterService {
    SemesterDTO create(SemesterDTO semesterDTO);
    SemesterDTO getById(String id);
    List<SemesterDTO> getAll();
    SemesterDTO update(String id, SemesterDTO semesterDTO);
    boolean delete(String id);
}
