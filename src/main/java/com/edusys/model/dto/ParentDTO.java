package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDTO {
    private String parentId;
    private String fullName;
    private String email;
    private String phone;
    private String occupation;
    private String status;
    private LocalDateTime createdAt;
}
