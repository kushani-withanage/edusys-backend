package com.edusys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "career_level_batch_access")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerLevelBatchAccessEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private CareerLevelEntity level;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private BatchEntity batch;

    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    @ManyToOne
    @JoinColumn(name = "opened_by", nullable = false)
    private UserEntity openedBy;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;
}
