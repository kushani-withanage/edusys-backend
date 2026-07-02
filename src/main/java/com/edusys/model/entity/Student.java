package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "student_id")
@Getter
@Setter
@NoArgsConstructor
public class Student extends User {

    private String address;

    @Column(name = "reg_no", unique = true)
    private String regNo;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    private LocalDate dob;
}
