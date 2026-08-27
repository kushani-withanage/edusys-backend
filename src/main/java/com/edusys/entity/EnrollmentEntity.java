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
@Table(name = "enrollments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentEntity {

    @Id
    @Column(name = "enrollment_id", length = 36)
    private String enrollmentId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "batch_id", nullable = false, length = 36)
    private String batchId;

    @Column(name = "course_id", nullable = false, length = 36)
    private String courseId;

    @Column(name = "enroll_date")
    private LocalDate enrollDate;

    @Column(name = "status", length = 50)
    private String status;
}
