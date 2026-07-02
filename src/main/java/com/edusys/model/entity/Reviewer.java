package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviewers")
@PrimaryKeyJoinColumn(name = "reviewer_id")
@Getter
@Setter
@NoArgsConstructor
public class Reviewer extends User {

    @Column(name = "expertise_area")
    private String expertiseArea;
}
