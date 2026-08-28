package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exam_courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamCourseEntity {

    @Id
    @Column(name = "course_id", length = 36)
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;
}
