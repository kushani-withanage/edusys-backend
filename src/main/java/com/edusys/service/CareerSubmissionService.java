package com.edusys.service;

import com.edusys.model.dto.CareerSubmissionDTO;

import java.util.List;

public interface CareerSubmissionService {
    CareerSubmissionDTO getById(String id);
    List<CareerSubmissionDTO> getAllSubmissions();
    List<CareerSubmissionDTO> getStudentSubmissions(String studentId);
    List<CareerSubmissionDTO> getPendingSubmissions();
    List<CareerSubmissionDTO> getSubmissionsByStatus(String status);
    CareerSubmissionDTO createSubmission(String studentId, String taskId, CareerSubmissionDTO dto);
}
