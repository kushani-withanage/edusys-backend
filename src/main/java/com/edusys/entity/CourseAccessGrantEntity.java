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
@Table(name = "course_access_grants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAccessGrantEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "course_id", length = 36, nullable = false)
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "batch_code", nullable = false)
    private String batchCode;

    @Column(name = "user_identifier", nullable = false)
    private String userIdentifier;

    @Column(name = "granted_at")
    private LocalDate grantedAt;

    @Column(name = "status", length = 50)
    private String status;
}
