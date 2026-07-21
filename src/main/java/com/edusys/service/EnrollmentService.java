package com.edusys.service;

import com.edusys.model.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDTO create(EnrollmentDTO enrollmentDTO);
    EnrollmentDTO getById(String id);
    List<EnrollmentDTO> getAll();
    EnrollmentDTO update(String id, EnrollmentDTO enrollmentDTO);
    boolean delete(String id);
}
