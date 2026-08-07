package com.edusys.service;

import com.edusys.entity.CareerSubmissionEntity;
import com.edusys.model.dto.StudentCareerProgressDTO;

public interface CareerProgressionService {
    StudentCareerProgressDTO getProgress(String studentId);
    void awardPoints(CareerSubmissionEntity submission, int points, String reviewerId, String comment);
}
