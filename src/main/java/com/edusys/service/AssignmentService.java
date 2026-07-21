package com.edusys.service;

import com.edusys.model.dto.AssignmentDTO;

import java.util.List;

public interface AssignmentService {
    AssignmentDTO create(AssignmentDTO assignmentDTO);
    AssignmentDTO getById(String id);
    List<AssignmentDTO> getAll();
    AssignmentDTO update(String id, AssignmentDTO assignmentDTO);
    boolean delete(String id);
}
