package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionDTO {
    private String studentId;
    private LocalDateTime startTime;
    private Map<String, List<String>> answers;
}
