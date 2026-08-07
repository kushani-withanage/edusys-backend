package com.edusys.service.impl;

import com.edusys.entity.CareerLevelEntity;
import com.edusys.entity.CareerSubmissionEntity;
import com.edusys.entity.StudentCareerProgressEntity;
import com.edusys.entity.StudentEntity;
import com.edusys.entity.UserEntity;
import com.edusys.model.dto.StudentCareerProgressDTO;
import com.edusys.repository.CareerLevelRepository;
import com.edusys.repository.CareerSubmissionRepository;
import com.edusys.repository.StudentCareerProgressRepository;
import com.edusys.repository.StudentRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.CareerProgressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Transactional
public class CareerProgressionServiceImpl implements CareerProgressionService {

    @Autowired
    private StudentCareerProgressRepository progressRepository;

    @Autowired
    private CareerLevelRepository levelRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CareerSubmissionRepository submissionRepository;

    @Override
    public StudentCareerProgressDTO getProgress(String studentId) {
        StudentCareerProgressEntity progress = progressRepository.findById(studentId).orElse(null);
        if (progress == null) {
            // Lazy initialize progress at Level 1
            StudentEntity student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId));
            
            // Find Level 1
            CareerLevelEntity level1 = levelRepository.findByLevelNumber(1)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Career Level 1 not configured in system. Please configure levels first."));
            
            progress = StudentCareerProgressEntity.builder()
                    .studentId(studentId)
                    .currentLevel(level1)
                    .pointsAtLevel(0)
                    .build();
            progress = progressRepository.save(progress);
        }

        String studentName = userRepository.findById(studentId)
                .map(UserEntity::getFullName)
                .orElse("Student");

        return StudentCareerProgressDTO.builder()
                .studentId(studentId)
                .studentName(studentName)
                .currentLevelId(progress.getCurrentLevel().getId())
                .currentLevelNumber(progress.getCurrentLevel().getLevelNumber())
                .currentLevelTitle(progress.getCurrentLevel().getTitle())
                .totalPointsAtLevel(progress.getPointsAtLevel())
                .levelPointsRequired(progress.getCurrentLevel().getPointsRequired())
                .build();
    }

    @Override
    public void awardPoints(CareerSubmissionEntity submission, int points, String reviewerId, String comment) {
        if (points < 0 || points > submission.getTask().getPointsValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Points awarded must be between 0 and " + submission.getTask().getPointsValue());
        }

        UserEntity reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviewer not found: " + reviewerId));

        // Save submission review details
        submission.setStatus("APPROVED");
        submission.setPointsAwarded(points);
        submission.setReviewer(reviewer);
        submission.setReviewerComment(comment);
        submission.setReviewedAt(LocalDateTime.now());
        submissionRepository.save(submission);

        // Update student progress
        String studentId = submission.getStudent().getStudentId();
        StudentCareerProgressEntity progress = progressRepository.findById(studentId).orElse(null);
        if (progress == null) {
            CareerLevelEntity level1 = levelRepository.findByLevelNumber(1)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Career Level 1 not configured in system."));
            progress = StudentCareerProgressEntity.builder()
                    .studentId(studentId)
                    .currentLevel(level1)
                    .pointsAtLevel(0)
                    .build();
        }

        int newPoints = progress.getPointsAtLevel() + points;
        CareerLevelEntity currentLvl = progress.getCurrentLevel();

        // Level progression loop
        while (newPoints >= currentLvl.getPointsRequired()) {
            CareerLevelEntity nextLvl = levelRepository.findByLevelNumber(currentLvl.getLevelNumber() + 1).orElse(null);
            if (nextLvl == null) {
                // Maximum level reached, cap points or continue to accumulate
                break;
            }
            newPoints -= currentLvl.getPointsRequired();
            currentLvl = nextLvl;
        }

        progress.setCurrentLevel(currentLvl);
        progress.setPointsAtLevel(newPoints);
        progressRepository.save(progress);
    }
}
