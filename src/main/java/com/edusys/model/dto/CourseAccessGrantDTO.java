package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAccessGrantDTO {
    private String id;
    private String courseId;
    private String courseName;
    private String batchCode;
    private String userIdentifier;
    private LocalDate grantedAt;
    private String status;
}
