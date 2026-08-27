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
@Table(name = "career_level")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerLevelEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "level_number", nullable = false, unique = true)
    private Integer levelNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "points_required", nullable = false)
    private Integer pointsRequired;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
