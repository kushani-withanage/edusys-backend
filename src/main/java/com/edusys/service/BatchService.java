package com.edusys.service;

import com.edusys.model.dto.BatchDTO;
import com.edusys.model.dto.CourseDTO;

import java.util.List;

public interface BatchService {
    BatchDTO create(BatchDTO batchDTO);
    BatchDTO getById(String id);
    List<BatchDTO> getAll();
    BatchDTO update(String id, BatchDTO batchDTO);
    boolean delete(String id);
    boolean isCodeTaken(String code, String excludeId);
    List<CourseDTO> getCoursesForBatch(String batchId);
    List<com.edusys.model.dto.UserDTO> getStudentsInBatch(String batchId);
}
