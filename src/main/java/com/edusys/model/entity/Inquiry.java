package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "inquiries")
@Getter
@Setter
@NoArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "inquiry_id")
    private String inquiryId;

    @Column(name = "applicant_name", nullable = false)
    private String applicantName;

    @Column(name = "contact_info")
    private String contactInfo;

    private String status;

    @Column(name = "inquiry_date")
    private LocalDate inquiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;
}
