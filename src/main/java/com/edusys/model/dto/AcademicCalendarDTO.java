package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicCalendarDTO {
    private String calendarId;
    private String eventName;
    private LocalDate eventDate;
    private String description;
    private String status;
}
