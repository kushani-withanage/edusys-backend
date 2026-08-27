package com.edusys.controller;

import com.edusys.entity.UserEntity;
import com.edusys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/teachers/dashboard")
@CrossOrigin
public class TeacherDashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        UserEntity user = userOpt.get();
        String email = user.getEmail();

        // 1. Assigned Courses Count
        String coursesSql = "SELECT COUNT(DISTINCT course_id) FROM course_access_grants WHERE LOWER(user_identifier) = LOWER(?)";
        Integer modulesCount = jdbcTemplate.queryForObject(coursesSql, Integer.class, email);
        if (modulesCount == null) {
            modulesCount = 0;
        }

        // 2. Uploaded Materials Count (created by teacher)
        String materialsSql = "SELECT COUNT(*) FROM assignments WHERE created_by = ?";
        Integer materialsCount = jdbcTemplate.queryForObject(materialsSql, Integer.class, userId);
        if (materialsCount == null) {
            materialsCount = 0;
        }

        // 3. Scheduled Exams Count (created by teacher)
        String examsSql = "SELECT COUNT(*) FROM exams WHERE created_by = ?";
        Integer examsCount = jdbcTemplate.queryForObject(examsSql, Integer.class, userId);
        if (examsCount == null) {
            examsCount = 0;
        }

        // 4. Pending Gradings Count (submissions where marks is null for assignments created by this teacher)
        String pendingSql = "SELECT COUNT(*) FROM assignment_submissions s " +
                            "JOIN assignments a ON s.assignment_id = a.assignment_id " +
                            "WHERE a.created_by = ? AND s.marks IS NULL";
        Integer gradesPending = jdbcTemplate.queryForObject(pendingSql, Integer.class, userId);
        if (gradesPending == null) {
            gradesPending = 0;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("modulesCount", modulesCount);
        response.put("materialsCount", materialsCount);
        response.put("examsCount", examsCount);
        response.put("gradesPending", gradesPending);

        return ResponseEntity.ok(response);
    }
}
