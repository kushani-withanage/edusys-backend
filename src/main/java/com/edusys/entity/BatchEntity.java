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

    @jakarta.persistence.ManyToMany
    @jakarta.persistence.JoinTable(
        name = "batch_course",
        joinColumns = @jakarta.persistence.JoinColumn(name = "batch_id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "course_id")
    )
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private java.util.Set<com.edusys.entity.CourseEntity> courses;
}
