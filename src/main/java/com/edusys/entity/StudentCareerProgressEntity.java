package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_career_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCareerProgressEntity {

    @Id
    @Column(name = "student_id", length = 36)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "current_level_id", nullable = false)
    private CareerLevelEntity currentLevel;

    @Column(name = "points_at_level", nullable = false)
    private Integer pointsAtLevel;
}
