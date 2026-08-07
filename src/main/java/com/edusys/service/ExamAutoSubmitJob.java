package com.edusys.service;

import com.edusys.entity.ExamAttemptEntity;
import com.edusys.entity.ExamEntity;
import com.edusys.repository.ExamAttemptRepository;
import com.edusys.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExamAutoSubmitJob {

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private GradingService gradingService;

    // Run every 30 seconds to sweep stale in-progress attempts
    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void sweepStaleAttempts() {
        // Load all attempts in IN_PROGRESS state
        List<ExamAttemptEntity> inProgressAttempts = examAttemptRepository.findByStatus("IN_PROGRESS");
        LocalDateTime now = LocalDateTime.now();

        for (ExamAttemptEntity attempt : inProgressAttempts) {
            // Double-check attempt is still in progress (idempotency guard)
            if (!"IN_PROGRESS".equalsIgnoreCase(attempt.getStatus())) {
                continue;
            }

            examRepository.findById(attempt.getExamId()).ifPresent(exam -> {
                LocalDateTime expiryTime = attempt.getStartedAt().plusMinutes(exam.getDurationMinutes());
                if (now.isAfter(expiryTime)) {
                    // Force auto-submit
                    attempt.setStatus("AUTO_SUBMITTED");
                    attempt.setSubmittedAt(now);
                    
                    // Auto-grade
                    gradingService.gradeAttempt(attempt);
                    examAttemptRepository.save(attempt);
                    
                    System.out.println("Auto-submitted stale exam attempt: " + attempt.getId() + " for student: " + attempt.getStudentId());
                }
            });
        }
    }
}
