package com.edusys.service.impl;

import com.edusys.entity.CareerLevelEntity;
import com.edusys.entity.StudentCareerProgressEntity;
import com.edusys.entity.StudentEntity;
import com.edusys.entity.UserEntity;
import com.edusys.model.dto.StudentCareerProgressDTO;
import com.edusys.repository.CareerLevelRepository;
import com.edusys.repository.StudentCareerProgressRepository;
import com.edusys.repository.StudentRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.CareerProgressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
}
