package com.edusys.controller;

import com.edusys.service.StudentExamService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/student-exams")
@CrossOrigin
public class StudentExamController {

    @Autowired
    private StudentExamService studentExamService;

    @GetMapping("/available")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Map<String, Object>>> getAvailableExams() {
        String studentId = getAuthenticatedUserId();
        return ResponseEntity.ok(studentExamService.getAvailableExams(studentId));
    }

    @PostMapping("/{examId}/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> startOrResumeAttempt(@PathVariable String examId) {
        String studentId = getAuthenticatedUserId();
        try {
            return ResponseEntity.ok(studentExamService.startOrResumeAttempt(examId, studentId));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/attempts/{attemptId}/answer")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> saveAnswer(
            @PathVariable String attemptId,
            @RequestBody AnswerRequest request
    ) {
        String studentId = getAuthenticatedUserId();
        try {
            studentExamService.saveAnswer(attemptId, studentId, request.getQuestionId(), request.getSelectedOptionIds());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/attempts/{attemptId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> submitAttempt(@PathVariable String attemptId) {
        String studentId = getAuthenticatedUserId();
        try {
            return ResponseEntity.ok(studentExamService.submitAttempt(attemptId, studentId));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/attempts/{attemptId}/result")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN', 'TEACHER')")
    public ResponseEntity<Map<String, Object>> getAttemptResult(@PathVariable String attemptId) {
        String studentId = getAuthenticatedUserId();
        try {
            return ResponseEntity.ok(studentExamService.getAttemptResult(attemptId, studentId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "anonymous";
    }

    @Data
    public static class AnswerRequest {
        private String questionId;
        private List<String> selectedOptionIds;
    }
}
