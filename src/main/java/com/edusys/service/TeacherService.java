package com.edusys.service;

import com.edusys.model.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {
    TeacherDTO create(TeacherDTO teacherDTO);
    TeacherDTO getById(String id);
    List<TeacherDTO> getAll();
    TeacherDTO update(String id, TeacherDTO teacherDTO);
    boolean delete(String id);
}
