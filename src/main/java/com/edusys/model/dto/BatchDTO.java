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
public class BatchDTO {
    private String batchId;
    private String batchName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private java.util.List<CourseDTO> courses;
    private Integer studentCount;
}
