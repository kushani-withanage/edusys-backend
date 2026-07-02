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
    private String levelId;
    private String levelName;
    private String description;
    private Integer minPoints;
    private Integer maxPoints;
}
