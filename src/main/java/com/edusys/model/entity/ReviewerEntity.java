package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviewers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerEntity {

    @Id
    @Column(name = "reviewer_id", length = 36)
    private String reviewerId;

    @Column(name = "expertise_area")
    private String expertiseArea;
}
