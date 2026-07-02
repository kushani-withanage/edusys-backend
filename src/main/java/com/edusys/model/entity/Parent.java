package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parents")
@PrimaryKeyJoinColumn(name = "parent_id")
@Getter
@Setter
@NoArgsConstructor
public class Parent extends User {

    private String occupation;
}
