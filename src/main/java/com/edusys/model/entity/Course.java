package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    private Integer credits;

    @Column(name = "duration_weeks")
    private Integer durationWeeks;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Batch> batches = new ArrayList<>();
}
