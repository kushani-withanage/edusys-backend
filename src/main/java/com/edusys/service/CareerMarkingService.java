package com.edusys.service;

import com.edusys.model.dto.CareerStudentTaskStatusDTO;

import java.util.List;

public interface CareerMarkingService {
    List<CareerStudentTaskStatusDTO> getStudentsForTask(String taskId);
    CareerStudentTaskStatusDTO markStudentTask(String taskId, String studentId, CareerStudentTaskStatusDTO dto, String markerUserId);
}
