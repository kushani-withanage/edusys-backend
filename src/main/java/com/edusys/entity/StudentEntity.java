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
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {

    @Id
    @Column(name = "student_id", length = 36)
    private String studentId;

    @Column(name = "address")
    private String address;

    @Column(name = "reg_no", unique = true)
    private String regNo;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nic", nullable = false, unique = true)
    private String nic;

    @Column(name = "current_batch_id", length = 36)
    private String currentBatchId;
}
