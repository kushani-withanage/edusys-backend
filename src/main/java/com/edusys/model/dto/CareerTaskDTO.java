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
public class CareerTaskDTO {
    private String taskId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String rubricCriteria;
    private Integer pointValue;
}
