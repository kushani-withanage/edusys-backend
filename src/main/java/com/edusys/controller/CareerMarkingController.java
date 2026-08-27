package com.edusys.controller;

import com.edusys.entity.*;
import com.edusys.model.dto.CareerStudentTaskStatusDTO;
import com.edusys.model.dto.StudentCareerProgressDTO;
import com.edusys.repository.*;
import com.edusys.service.CareerMarkingService;
import com.edusys.service.CareerProgressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/career")
@CrossOrigin
public class CareerMarkingController {

    @Autowired
    private CareerMarkingService careerMarkingService;

    @Autowired
    private CareerProgressionService careerProgressionService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CareerTaskRepository careerTaskRepository;

    @Autowired
    private CareerStudentTaskStatusRepository careerStudentTaskStatusRepository;

    @Autowired
    private StudentCareerProgressRepository progressRepository;

    @Autowired
    private CareerLevelRepository levelRepository;

    @Autowired
    private UserRepository userRepository;

    // --- Staff endpoints ---

    @GetMapping("/tasks/{taskId}/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'REVIEWER')")
    public ResponseEntity<List<CareerStudentTaskStatusDTO>> getStudentsForTask(@PathVariable String taskId) {
        return ResponseEntity.ok(careerMarkingService.getStudentsForTask(taskId));
    }

    @PutMapping("/tasks/{taskId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'REVIEWER')")
    public ResponseEntity<CareerStudentTaskStatusDTO> markStudentTask(
            @PathVariable String taskId,
            @PathVariable String studentId,
            @RequestBody CareerStudentTaskStatusDTO dto) {
        String markerUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(careerMarkingService.markStudentTask(taskId, studentId, dto, markerUserId));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'REVIEWER')")
    public ResponseEntity<Map<String, Object>> getStats() {
        Iterable<StudentCareerProgressEntity> progresses = progressRepository.findAll();
        Iterable<CareerLevelEntity> levels = levelRepository.findAll();

        int maxLevelNum = 0;
        String maxLevelId = null;
        for (CareerLevelEntity lvl : levels) {
            if (lvl.getLevelNumber() > maxLevelNum) {
                maxLevelNum = lvl.getLevelNumber();
                maxLevelId = lvl.getId();
            }
        }

        Map<String, Long> levelCounts = new HashMap<>();
        long industryReadyCount = 0;

        for (StudentCareerProgressEntity p : progresses) {
            String lvlId = p.getCurrentLevel().getId();
            levelCounts.put(lvlId, levelCounts.getOrDefault(lvlId, 0L) + 1);
            if (lvlId.equals(maxLevelId)) {
                industryReadyCount++;
            }
        }

        List<Map<String, Object>> levelStats = new ArrayList<>();
        for (CareerLevelEntity lvl : levels) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("levelId", lvl.getId());
            stat.put("levelNumber", lvl.getLevelNumber());
            stat.put("title", lvl.getTitle());
            stat.put("studentCount", levelCounts.getOrDefault(lvl.getId(), 0L));
            levelStats.add(stat);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("levelStats", levelStats);
        response.put("industryReadyCount", industryReadyCount);

        return ResponseEntity.ok(response);
    }

    // --- Student endpoints ---

    @GetMapping({"/my-progress", "/progress"})
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentCareerProgressDTO> getMyProgress() {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(careerProgressionService.getProgress(studentId));
    }

    @GetMapping("/my-tasks")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CareerStudentTaskStatusDTO>> getMyTasks() {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        StudentEntity student = studentRepository.findById(studentId).orElse(null);
        if (student == null || student.getCurrentBatchId() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        String batchId = student.getCurrentBatchId();
        List<CareerTaskEntity> tasks = careerTaskRepository.findByBatchId(batchId);
        
        List<CareerStudentTaskStatusEntity> statuses = careerStudentTaskStatusRepository.findByStudent_StudentId(studentId);
        Map<String, CareerStudentTaskStatusEntity> statusMap = new HashMap<>();
        for (CareerStudentTaskStatusEntity status : statuses) {
            statusMap.put(status.getTask().getId(), status);
        }

        List<CareerStudentTaskStatusDTO> dtoList = new ArrayList<>();
        for (CareerTaskEntity task : tasks) {
            CareerStudentTaskStatusEntity statusEntity = statusMap.get(task.getId());
            
            String studentName = userRepository.findById(studentId)
                    .map(UserEntity::getFullName)
                    .orElse(studentId);
            
            CareerStudentTaskStatusDTO.CareerStudentTaskStatusDTOBuilder builder = CareerStudentTaskStatusDTO.builder()
                    .taskId(task.getId())
                    .taskTitle(task.getTitle())
                    .taskDescription(task.getDescription())
                    .pointsValue(task.getPointsValue())
                    .studentId(studentId)
                    .studentName(studentName);

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

        return ResponseEntity.ok(dtoList);
    }
}
