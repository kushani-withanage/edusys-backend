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
@Table(name = "parent_student_links")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentStudentLinkEntity {

    @Id
    @Column(name = "link_id", length = 36)
    private String linkId;

    @Column(name = "parent_id", nullable = false, length = 36)
    private String parentId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    @Column(name = "relationship_type")
    private String relationshipType;

    @Column(name = "linked_date")
    private LocalDate linkedDate;
}
