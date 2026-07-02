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
public class ParentStudentLinkDTO {
    private String linkId;
    private String parentId;
    private String studentId;
    private String relationshipType;
    private LocalDate linkedDate;
}
