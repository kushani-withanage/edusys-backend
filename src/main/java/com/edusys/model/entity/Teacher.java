package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "teacher_id")
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {

    private String specialization;

    @Column(name = "join_date")
    private LocalDate joinDate;
}
