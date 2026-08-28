package com.edusys.service.impl;

import com.edusys.entity.*;
import com.edusys.model.dto.CareerStudentTaskStatusDTO;
import com.edusys.repository.*;
import com.edusys.service.CareerMarkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class CareerMarkingServiceImpl implements CareerMarkingService {

    @Autowired
    private CareerTaskRepository careerTaskRepository;

    @Autowired
    private CareerStudentTaskStatusRepository careerStudentTaskStatusRepository;

    @Autowired
    private StudentCareerProgressRepository progressRepository;

    @Autowired
    private CareerLevelRepository levelRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CareerStudentTaskStatusDTO> getStudentsForTask(String taskId) {
        CareerTaskEntity task = careerTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found: " + taskId));

        // Find all students in the batches assigned to this task
        List<StudentEntity> students = new ArrayList<>();
        if (task.getBatches() != null) {
            for (BatchEntity batch : task.getBatches()) {
                students.addAll(studentRepository.findByCurrentBatchId(batch.getBatchId()));
            }
        }

        // Fetch existing marking statuses
        List<CareerStudentTaskStatusEntity> existingStatuses = careerStudentTaskStatusRepository.findByTask_Id(taskId);
        Map<String, CareerStudentTaskStatusEntity> statusMap = new HashMap<>();
        for (CareerStudentTaskStatusEntity status : existingStatuses) {
            statusMap.put(status.getStudent().getStudentId(), status);
        }

        List<CareerStudentTaskStatusDTO> dtoList = new ArrayList<>();
        for (StudentEntity student : students) {
            CareerStudentTaskStatusEntity statusEntity = statusMap.get(student.getStudentId());
            
            String studentName = userRepository.findById(student.getStudentId())
                    .map(UserEntity::getFullName)
                    .orElse(student.getStudentId());
            
            CareerStudentTaskStatusDTO.CareerStudentTaskStatusDTOBuilder builder = CareerStudentTaskStatusDTO.builder()
                    .taskId(taskId)
                    .taskTitle(task.getTitle())
                    .taskDescription(task.getDescription())
                    .pointsValue(task.getPointsValue())
                    .studentId(student.getStudentId())
                    .studentName(studentName)
                    .regNo(student.getRegNo());

            if (statusEntity != null) {
                builder.id(statusEntity.getId())
                        .status(statusEntity.getStatus())
                        .pointsAwarded(statusEntity.getPointsAwarded())
                        .markedBy(statusEntity.getMarkedBy() != null ? statusEntity.getMarkedBy().getUserId() : null)
                        .markedByName(statusEntity.getMarkedBy() != null ? statusEntity.getMarkedBy().getFullName() : null)
                        .markedAt(statusEntity.getMarkedAt())
                        .comment(statusEntity.getComment());
            } else {
                builder.status("NOT_STARTED")
                        .pointsAwarded(null);
            }
            dtoList.add(builder.build());
        }

        return dtoList;
    }

    @Override
    @Transactional
    public CareerStudentTaskStatusDTO markStudentTask(String taskId, String studentId, CareerStudentTaskStatusDTO dto, String markerUserId) {
        CareerTaskEntity task = careerTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found: " + taskId));

        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId));

        UserEntity marker = userRepository.findById(markerUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marker user not found: " + markerUserId));

        // Get or create Student Progress
        StudentCareerProgressEntity progress = progressRepository.findById(studentId).orElse(null);
        if (progress == null) {
            CareerLevelEntity level1 = levelRepository.findByLevelNumber(1)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Career Level 1 not configured in system."));
            progress = StudentCareerProgressEntity.builder()
                    .studentId(studentId)
                    .currentLevel(level1)
                    .pointsAtLevel(0)
                    .build();
            progress = progressRepository.save(progress);
        }

        // Get or create existing task status
        CareerStudentTaskStatusEntity existingStatus = careerStudentTaskStatusRepository.findByTask_IdAndStudent_StudentId(taskId, studentId)
                .orElse(null);

        if (existingStatus == null) {
            existingStatus = CareerStudentTaskStatusEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .task(task)
                    .student(student)
                    .status("NOT_STARTED")
                    .build();
        }

        int oldPoints = 0;
        if ("COMPLETED".equalsIgnoreCase(existingStatus.getStatus())) {
            oldPoints = existingStatus.getPointsAwarded() != null ? existingStatus.getPointsAwarded() : 0;
        }

        int newPoints = 0;
        if ("COMPLETED".equalsIgnoreCase(dto.getStatus())) {
            if (dto.getPointsAwarded() == null || dto.getPointsAwarded() < 0 || dto.getPointsAwarded() > task.getPointsValue()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Points awarded must be between 0 and " + task.getPointsValue());
            }
            newPoints = dto.getPointsAwarded();
        }

        int delta = newPoints - oldPoints;

        existingStatus.setStatus(dto.getStatus());
        existingStatus.setPointsAwarded("COMPLETED".equalsIgnoreCase(dto.getStatus()) ? newPoints : null);
        existingStatus.setMarkedBy(marker);
        existingStatus.setMarkedAt(LocalDateTime.now());
        existingStatus.setComment(dto.getComment());
        
        CareerStudentTaskStatusEntity savedStatus = careerStudentTaskStatusRepository.save(existingStatus);

        if (delta != 0) {
            int currentPoints = progress.getPointsAtLevel();
            currentPoints += delta;

            CareerLevelEntity currentLvl = progress.getCurrentLevel();

            if (currentPoints >= 0) {
                while (currentPoints >= currentLvl.getPointsRequired()) {
                    CareerLevelEntity nextLvl = levelRepository.findByLevelNumber(currentLvl.getLevelNumber() + 1).orElse(null);
                    if (nextLvl == null) {
                        break;
                    }
                    currentPoints -= currentLvl.getPointsRequired();
                    currentLvl = nextLvl;
                }
            } else {
                while (currentPoints < 0) {
                    CareerLevelEntity prevLvl = levelRepository.findByLevelNumber(currentLvl.getLevelNumber() - 1).orElse(null);
                    if (prevLvl == null) {
                        currentPoints = 0;
                        break;
                    }
                    currentLvl = prevLvl;
                    currentPoints += currentLvl.getPointsRequired();
                }
            }

            progress.setCurrentLevel(currentLvl);
            progress.setPointsAtLevel(currentPoints);
            progressRepository.save(progress);
        }

        String studentName = userRepository.findById(studentId)
                .map(UserEntity::getFullName)
                .orElse(studentId);

        return CareerStudentTaskStatusDTO.builder()
                .id(savedStatus.getId())
                .taskId(taskId)
                .taskTitle(task.getTitle())
                .taskDescription(task.getDescription())
                .pointsValue(task.getPointsValue())
                .studentId(studentId)
                .studentName(studentName)
                .regNo(student.getRegNo())
                .status(savedStatus.getStatus())
                .pointsAwarded(savedStatus.getPointsAwarded())
                .markedBy(markerUserId)
                .markedByName(marker.getFullName())
                .markedAt(savedStatus.getMarkedAt())
                .comment(savedStatus.getComment())
                .build();
    }
}
