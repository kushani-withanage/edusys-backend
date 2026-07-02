package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "career_points_ledger")
@Getter
@Setter
@NoArgsConstructor
public class CareerPointsLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ledger_id")
    private String ledgerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private CareerTask careerTask;

    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    private String remarks;
}
