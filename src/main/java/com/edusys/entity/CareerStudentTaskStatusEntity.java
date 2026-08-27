package com.edusys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "career_student_task_status", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"task_id", "student_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerStudentTaskStatusEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private CareerTaskEntity task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(name = "status", nullable = false, length = 30)
    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED

    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private UserEntity markedBy;

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
}
