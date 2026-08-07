package com.edusys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exam_audiences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAudienceEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExamEntity exam;

    @Column(name = "target_type", nullable = false, length = 20)
    private String targetType; // BATCH, MODULE

    @Column(name = "target_id", nullable = false, length = 36)
    private String targetId;
}
