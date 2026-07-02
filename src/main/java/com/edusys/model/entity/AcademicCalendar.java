package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "academic_calendars")
@Getter
@Setter
@NoArgsConstructor
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "calendar_id")
    private String calendarId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;
}
