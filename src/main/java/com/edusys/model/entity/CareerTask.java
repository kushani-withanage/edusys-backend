package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "career_tasks")
@Getter
@Setter
@NoArgsConstructor
public class CareerTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "task_id")
    private String taskId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "rubric_criteria", columnDefinition = "TEXT")
    private String rubricCriteria;

    @Column(name = "point_value")
    private Integer pointValue;

    @OneToMany(mappedBy = "careerTask", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CareerSubmission> submissions = new ArrayList<>();
}
