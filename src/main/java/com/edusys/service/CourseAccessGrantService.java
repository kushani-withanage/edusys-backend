package com.edusys.service;

import com.edusys.model.dto.CourseAccessGrantDTO;

import java.util.List;

public interface CourseAccessGrantService {
    CourseAccessGrantDTO create(CourseAccessGrantDTO dto);
    CourseAccessGrantDTO getById(String id);
    List<CourseAccessGrantDTO> getAll();
    List<CourseAccessGrantDTO> getByUserIdentifier(String email);
    boolean delete(String id);
}
