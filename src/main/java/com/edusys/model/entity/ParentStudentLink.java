package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "parent_student_links")
@Getter
@Setter
@NoArgsConstructor
public class ParentStudentLink {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "link_id")
    private String linkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "relationship_type")
    private String relationshipType;

    @Column(name = "linked_date")
    private LocalDate linkedDate;
}
