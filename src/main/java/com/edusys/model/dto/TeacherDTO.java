package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private String teacherId;
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private LocalDateTime createdAt;
    private String specialization;
    private LocalDate joinDate;
}
