package com.edusys.service.impl;

import com.edusys.model.dto.DashboardStatsDTO;
import com.edusys.repository.StudentRepository;
import com.edusys.repository.TeacherRepository;
import com.edusys.repository.BatchRepository;
import com.edusys.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private com.edusys.repository.FeeRecordRepository feeRecordRepository;

    @Override
    public long getTotalStudents() {
        return studentRepository.count();
    }

    @Override
    public long getTotalTeachers() {
        return teacherRepository.count();
    }

    @Override
    public long getActiveBatchesCount() {
        LocalDate today = LocalDate.now();
        return batchRepository.countByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }

    @Override
    public long getNewAdmissionIntake() {
        int currentYear = LocalDate.now().getYear();
        LocalDate start = LocalDate.of(currentYear, 1, 1);
        LocalDate end = LocalDate.of(currentYear, 12, 31);
        return studentRepository.countByEnrollmentDateBetween(start, end);
    }

    @Override
    public long getOverduePaymentsCount() {
        return feeRecordRepository.countByStatusNotAndDueDateLessThan("PAID", LocalDate.now());
    }

    @Override
    public long getPendingPaymentsCount() {
        return feeRecordRepository.countByStatus("PENDING");
    }

    @Override
    public DashboardStatsDTO getCombinedStats() {
        return DashboardStatsDTO.builder()
                .totalStudents(getTotalStudents())
                .totalTeachers(getTotalTeachers())
                .activeBatchesCount(getActiveBatchesCount())
                .newAdmissionIntake(getNewAdmissionIntake())
                .overduePaymentsCount(getOverduePaymentsCount())
                .pendingPaymentsCount(getPendingPaymentsCount())
                .build();
    }
}
