package com.edusys.service;

import com.edusys.model.dto.AssignmentSubmissionDTO;

import java.util.List;

public interface AssignmentSubmissionService {
    AssignmentSubmissionDTO create(AssignmentSubmissionDTO dto);
    AssignmentSubmissionDTO getById(String id);
    List<AssignmentSubmissionDTO> getAll();
    AssignmentSubmissionDTO update(String id, AssignmentSubmissionDTO dto);
    boolean delete(String id);
    AssignmentSubmissionDTO getByAssignmentAndStudent(String assignmentId, String studentId);
    List<AssignmentSubmissionDTO> getByAssignment(String assignmentId);
}
