package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "inquiries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryEntity {

    @Id
    @Column(name = "inquiry_id", length = 36)
    private String inquiryId;

    @Column(name = "applicant_name", nullable = false)
    private String applicantName;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "status")
    private String status;

    @Column(name = "inquiry_date")
    private LocalDate inquiryDate;

    @Column(name = "batch_id", length = 36)
    private String batchId;
}
