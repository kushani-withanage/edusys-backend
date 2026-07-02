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
public class InquiryDTO {
    private String inquiryId;
    private String applicantName;
    private String contactInfo;
    private String status;
    private LocalDate inquiryDate;
}
