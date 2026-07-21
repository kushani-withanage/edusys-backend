package com.edusys.service;

import com.edusys.model.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    StudentDTO create(StudentDTO studentDTO);
    StudentDTO getById(String id);
    List<StudentDTO> getAll();
    StudentDTO update(String id, StudentDTO studentDTO);
    boolean delete(String id);
}
