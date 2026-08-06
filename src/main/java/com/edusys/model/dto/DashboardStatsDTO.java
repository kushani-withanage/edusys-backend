package com.edusys.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalStudents;
    private long totalTeachers;
    private long activeBatchesCount;
    private long newAdmissionIntake;
    private long overduePaymentsCount;
    private long pendingPaymentsCount;
}
