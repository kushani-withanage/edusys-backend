package com.edusys.service;

import com.edusys.model.dto.CourseDTO;

import java.util.List;

public interface CourseService {
    CourseDTO create(CourseDTO courseDTO);
    CourseDTO getById(String id);
    List<CourseDTO> getAll();
    CourseDTO update(String id, CourseDTO courseDTO);
    boolean delete(String id);
    List<CourseDTO> getCoursesForUser(String userId);
    boolean updateCourseStatusForUser(String userId, String courseId, String status);
}
