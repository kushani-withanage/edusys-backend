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
@Table(name = "grades")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeEntity {

    @Id
    @Column(name = "grade_id", length = 36)
    private String gradeId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "course_id", length = 36)
    private String courseId;

    @Column(name = "submission_id", unique = true, length = 36)
    private String submissionId;

    @Column(name = "grade_value")
    private String gradeValue;

    @Column(name = "published_date")
    private LocalDate publishedDate;
}
