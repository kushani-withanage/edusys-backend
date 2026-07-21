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
@Table(name = "career_levels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerLevelEntity {

    @Id
    @Column(name = "level_id", length = 36)
    private String levelId;

    @Column(name = "level_name", nullable = false, unique = true)
    private String levelName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "min_points")
    private Integer minPoints;

    @Column(name = "max_points")
    private Integer maxPoints;
}
