package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private String courseId;
    private String courseName;
    private Integer credits;
    private Integer durationWeeks;
    private String description;
    private String batchCode;
    private String level;
    private Boolean isCompulsory;
    private String certReqs;
    private String qualifyIntro;
    private String qualifyReqs;
    private String sections;
    private String status;
    private String instructor;
}
