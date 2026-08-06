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
public class AssignmentDTO {
    private String assignmentId;
    private String title;
    private String description;
    private Boolean displayDescription;
    private String activityInstructions;
    private String additionalFileName;
    private String additionalFileUrl;
    private Boolean onlyShowFiles;
    private LocalDateTime allowSubmissionsFrom;
    private LocalDateTime dueDate;
    private LocalDateTime cutOffDate;
    private LocalDateTime remindGradeBy;
    private Boolean alwaysShowDescription;
    private Boolean submissionTypeOnlineText;
    private Boolean submissionTypeFile;
    private Integer maxFiles;
    private String maxSize;
    private String createdBy;
}
