package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeRecordEntity {

    @Id
    @Column(name = "fee_id", length = 36)
    private String feeId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "fee_type")
    private String feeType;

    @Column(name = "status")
    private String status;
}
