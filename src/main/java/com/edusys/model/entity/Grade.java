package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "grade_id")
    private String gradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", unique = true)
    private AssignmentSubmission submission;

    @Column(name = "grade_value")
    private String gradeValue;

    @Column(name = "published_date")
    private LocalDate publishedDate;
}
