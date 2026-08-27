package com.edusys.service;

import com.edusys.model.dto.StudentCareerProgressDTO;

public interface CareerProgressionService {
    StudentCareerProgressDTO getProgress(String studentId);
}
