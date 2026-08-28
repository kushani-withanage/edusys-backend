package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerStudentTaskStatusDTO {
    private String id;
    private String taskId;
    private String taskTitle;
    private String taskDescription;
    private Integer pointsValue;
    private String studentId;
    private String studentName;
    private String regNo;
    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED
    private Integer pointsAwarded;
    private String markedBy;
    private String markedByName;
    private LocalDateTime markedAt;
    private String comment;
}
