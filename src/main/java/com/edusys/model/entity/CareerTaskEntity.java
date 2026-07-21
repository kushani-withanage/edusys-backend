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
@Table(name = "career_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerTaskEntity {

    @Id
    @Column(name = "task_id", length = 36)
    private String taskId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "rubric_criteria", columnDefinition = "TEXT")
    private String rubricCriteria;

    @Column(name = "point_value")
    private Integer pointValue;
}
