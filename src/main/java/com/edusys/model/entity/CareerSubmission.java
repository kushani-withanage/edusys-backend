package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "career_submissions")
@Getter
@Setter
@NoArgsConstructor
public class CareerSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "submission_id")
    private String submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private CareerTask careerTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String status;

    @Column(name = "submitted_file")
    private String submittedFile;

    @Column(name = "submit_date")
    private LocalDateTime submitDate;

    @OneToOne(mappedBy = "careerSubmission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Evaluation evaluation;
}
