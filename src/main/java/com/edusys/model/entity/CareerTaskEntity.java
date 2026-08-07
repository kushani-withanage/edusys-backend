package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "career_task")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerTaskEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private CareerLevelEntity level;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "submission_type", nullable = false, length = 20)
    private String submissionType; // LINK, IMAGE, PDF, FILE

    @Column(name = "points_value", nullable = false)
    private Integer pointsValue;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity creator;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
