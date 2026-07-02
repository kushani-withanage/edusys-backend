package com.edusys.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "admin_id")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    private String department;
}
