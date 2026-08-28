package com.edusys.service;

import com.edusys.model.dto.ExamCourseDTO;
import java.util.List;

public interface ExamCourseService {
    ExamCourseDTO create(ExamCourseDTO examCourseDTO);
    ExamCourseDTO getById(String id);
    List<ExamCourseDTO> getAll();
    ExamCourseDTO update(String id, ExamCourseDTO examCourseDTO);
    boolean delete(String id);
}
