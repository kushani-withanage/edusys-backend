package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentEntity {

    @Id
    @Column(name = "assignment_id", length = 36)
    private String assignmentId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "display_description")
    private Boolean displayDescription;

    @Column(name = "activity_instructions", columnDefinition = "TEXT")
    private String activityInstructions;

    @Column(name = "additional_file_name")
    private String additionalFileName;

    @Column(name = "additional_file_url", length = 500)
    private String additionalFileUrl;

    @Column(name = "only_show_files")
    private Boolean onlyShowFiles;

    @Column(name = "allow_submissions_from")
    private LocalDateTime allowSubmissionsFrom;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "cut_off_date")
    private LocalDateTime cutOffDate;

    @Column(name = "remind_grade_by")
    private LocalDateTime remindGradeBy;

    @Column(name = "always_show_description")
    private Boolean alwaysShowDescription;

    @Column(name = "submission_type_online_text")
    private Boolean submissionTypeOnlineText;

    @Column(name = "submission_type_file")
    private Boolean submissionTypeFile;

    @Column(name = "max_files")
    private Integer maxFiles;

    @Column(name = "max_size")
    private String maxSize;

    @Column(name = "created_by", length = 36)
    private String createdBy;
}
