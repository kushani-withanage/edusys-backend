package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "career_points_ledger")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerPointsLedgerEntity {

    @Id
    @Column(name = "ledger_id", length = 36)
    private String ledgerId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "task_id", nullable = false, length = 36)
    private String taskId;

    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "remarks")
    private String remarks;
}
