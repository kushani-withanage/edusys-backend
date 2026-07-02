package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fee_records")
@Getter
@Setter
@NoArgsConstructor
public class FeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "fee_id")
    private String feeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "fee_type")
    private String feeType;

    private String status;

    @OneToMany(mappedBy = "feeRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Receipt> receipts = new ArrayList<>();
}
