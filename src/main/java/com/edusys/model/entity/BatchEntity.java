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
@Table(name = "batches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchEntity {

    @Id
    @Column(name = "batch_id", length = 36)
    private String batchId;

    @Column(name = "batch_name", nullable = false)
    private String batchName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "course_name")
    private String courseName;
}
