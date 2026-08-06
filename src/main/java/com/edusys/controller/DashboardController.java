package com.edusys.controller;

import com.edusys.model.dto.DashboardStatsDTO;
import com.edusys.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/total-students")
    public ResponseEntity<Long> getTotalStudents() {
        return ResponseEntity.ok(dashboardService.getTotalStudents());
    }

    @GetMapping("/total-teachers")
    public ResponseEntity<Long> getTotalTeachers() {
        return ResponseEntity.ok(dashboardService.getTotalTeachers());
    }

    @GetMapping("/active-batches-count")
    public ResponseEntity<Long> getActiveBatchesCount() {
        return ResponseEntity.ok(dashboardService.getActiveBatchesCount());
    }

    @GetMapping("/new-admission-intake")
    public ResponseEntity<Long> getNewAdmissionIntake() {
        return ResponseEntity.ok(dashboardService.getNewAdmissionIntake());
    }

    @GetMapping("/overdue-payments")
    public ResponseEntity<Long> getOverduePaymentsCount() {
        return ResponseEntity.ok(dashboardService.getOverduePaymentsCount());
    }

    @GetMapping("/pending-payments-count")
    public ResponseEntity<Long> getPendingPaymentsCount() {
        return ResponseEntity.ok(dashboardService.getPendingPaymentsCount());
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getCombinedStats() {
        return ResponseEntity.ok(dashboardService.getCombinedStats());
    }
}
