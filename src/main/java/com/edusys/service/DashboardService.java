package com.edusys.service;

import com.edusys.model.dto.DashboardStatsDTO;

public interface DashboardService {
    long getTotalStudents();
    long getTotalTeachers();
    long getActiveBatchesCount();
    long getNewAdmissionIntake();
    long getOverduePaymentsCount();
    long getPendingPaymentsCount();
    DashboardStatsDTO getCombinedStats();
}
