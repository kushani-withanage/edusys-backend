package com.edusys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviewers/dashboard")
@CrossOrigin
public class ReviewerDashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        // 1. Pending evaluations count (NOT_STARTED or IN_PROGRESS)
        String pendingSql = "SELECT COUNT(*) FROM career_student_task_status WHERE status IN ('NOT_STARTED', 'IN_PROGRESS')";
        Integer pendingReviews = jdbcTemplate.queryForObject(pendingSql, Integer.class);
        if (pendingReviews == null) {
            pendingReviews = 0;
        }

        // 2. Evaluated submissions count (COMPLETED)
        String completedSql = "SELECT COUNT(*) FROM career_student_task_status WHERE status = 'COMPLETED'";
        Integer reviewedCount = jdbcTemplate.queryForObject(completedSql, Integer.class);
        if (reviewedCount == null) {
            reviewedCount = 0;
        }

        // 3. Overrides applied count
        String overridesSql = "SELECT COUNT(*) FROM career_level_overrides";
        Integer overridesApplied = jdbcTemplate.queryForObject(overridesSql, Integer.class);
        if (overridesApplied == null) {
            overridesApplied = 0;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("pendingReviews", pendingReviews);
        response.put("reviewedCount", reviewedCount);
        response.put("overridesApplied", overridesApplied);

        return ResponseEntity.ok(response);
    }
}
