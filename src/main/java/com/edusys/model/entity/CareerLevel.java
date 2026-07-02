package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "career_levels")
@Getter
@Setter
@NoArgsConstructor
public class CareerLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "level_id")
    private String levelId;

    @Column(name = "level_name", nullable = false, unique = true)
    private String levelName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "min_points")
    private Integer minPoints;

    @Column(name = "max_points")
    private Integer maxPoints;
}
