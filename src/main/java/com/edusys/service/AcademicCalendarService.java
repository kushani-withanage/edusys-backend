package com.edusys.service;

import com.edusys.model.dto.AcademicCalendarDTO;

import java.util.List;

public interface AcademicCalendarService {
    AcademicCalendarDTO create(AcademicCalendarDTO academicCalendarDTO);
    AcademicCalendarDTO getById(String id);
    List<AcademicCalendarDTO> getAll();
    AcademicCalendarDTO update(String id, AcademicCalendarDTO academicCalendarDTO);
    boolean delete(String id);
}
