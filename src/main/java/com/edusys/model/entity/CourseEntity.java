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
@Table(name = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {

    @Id
    @Column(name = "course_id", length = 36)
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "duration_weeks")
    private Integer durationWeeks;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "batch_code")
    private String batchCode;

    @Column(name = "course_level")
    private String level;

    @Column(name = "is_compulsory")
    private Boolean isCompulsory;

    @Column(name = "cert_reqs", columnDefinition = "TEXT")
    private String certReqs;

    @Column(name = "qualify_intro", columnDefinition = "TEXT")
    private String qualifyIntro;

    @Column(name = "qualify_reqs", columnDefinition = "TEXT")
    private String qualifyReqs;

    @Column(name = "sections", columnDefinition = "LONGTEXT")
    private String sections;
}
