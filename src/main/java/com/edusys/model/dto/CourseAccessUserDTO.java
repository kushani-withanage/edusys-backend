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
public class CourseAccessUserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String batchId;
    private String batchName;
    private String accessType; // "Standard" or "Custom"
    private String grantId; // if Custom, the id of the CourseAccessGrantEntity
    private LocalDate grantedAt;
}
