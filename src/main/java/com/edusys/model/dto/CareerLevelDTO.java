package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerLevelDTO {
    private String id;
    private Integer levelNumber;
    private String title;
    private String description;
    private Integer pointsRequired;
    private Boolean isActive;
}
