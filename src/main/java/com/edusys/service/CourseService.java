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
    List<com.edusys.model.dto.BatchDTO> getBatchesForCourse(String courseId);
    void grantAccess(String courseId, String batchId, List<String> userIds);
    List<com.edusys.model.dto.CourseAccessUserDTO> getCourseAccessList(String courseId, String batchId);
}
