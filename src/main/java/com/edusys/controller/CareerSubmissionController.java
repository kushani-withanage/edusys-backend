package com.edusys.controller;

import com.edusys.entity.CareerSubmissionEntity;
import com.edusys.entity.UserEntity;
import com.edusys.model.dto.CareerSubmissionDTO;
import com.edusys.model.dto.CareerTaskDTO;
import com.edusys.model.dto.StudentCareerProgressDTO;
import com.edusys.repository.CareerSubmissionRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.CareerProgressionService;
import com.edusys.service.CareerSubmissionService;
import com.edusys.service.CareerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/career")
@CrossOrigin
public class CareerSubmissionController {

    @Autowired
    private CareerSubmissionService careerSubmissionService;

    @Autowired
    private CareerTaskService careerTaskService;

    @Autowired
    private CareerProgressionService careerProgressionService;

    @Autowired
    private CareerSubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/tasks/{taskId}/submissions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitWork(
            @PathVariable String taskId,
            @RequestParam("submissionType") String submissionType,
            @RequestParam(value = "submissionUrl", required = false) String submissionUrl,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = auth.getPrincipal().toString();

        CareerSubmissionDTO dto = new CareerSubmissionDTO();
        dto.setSubmissionType(submissionType);
        dto.setSubmissionUrl(submissionUrl);

        // File validation if file is uploaded
        if (file != null && !file.isEmpty()) {
            // Max size: 10MB
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(Map.of("message", "File size exceeds the maximum limit of 10MB."));
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid file name."));
            }

            String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

            // Enforce file extension check against type
            if ("IMAGE".equalsIgnoreCase(submissionType)) {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Invalid file type. Expected an image file."));
                }
            } else if ("PDF".equalsIgnoreCase(submissionType)) {
                if (!"pdf".equalsIgnoreCase(ext)) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Invalid file type. Expected a PDF document."));
                }
            } else if ("FILE".equalsIgnoreCase(submissionType)) {
                if ("exe".equalsIgnoreCase(ext) || "bat".equalsIgnoreCase(ext) || "sh".equalsIgnoreCase(ext)) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Uploaded file type is restricted."));
                }
            }

            try {
                File uploadDir = new File("uploads/career_submissions");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String savedFileName = System.currentTimeMillis() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
                File dest = new File(uploadDir, savedFileName);
                file.transferTo(dest.getAbsoluteFile());
                dto.setFilePath("/uploads/career_submissions/" + savedFileName);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "File upload failed. Please try again."));
            }
        }

        try {
            CareerSubmissionDTO created = careerSubmissionService.createSubmission(studentId, taskId, dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/submissions/mine")
    public ResponseEntity<List<CareerSubmissionDTO>> getMySubmissions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = auth.getPrincipal().toString();
        return ResponseEntity.ok(careerSubmissionService.getStudentSubmissions(studentId));
    }

    @GetMapping("/progress")
    public ResponseEntity<StudentCareerProgressDTO> getStudentProgress() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String studentId = auth.getPrincipal().toString();
        return ResponseEntity.ok(careerProgressionService.getProgress(studentId));
    }

    @GetMapping("/submissions")
    public ResponseEntity<List<CareerSubmissionDTO>> getSubmissions(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String levelId) {
        
        List<CareerSubmissionDTO> list;
        if (status != null && !status.trim().isEmpty()) {
            list = careerSubmissionService.getSubmissionsByStatus(status.toUpperCase());
        } else {
            list = careerSubmissionService.getAllSubmissions();
        }

        if (levelId != null && !levelId.trim().isEmpty()) {
            list = list.stream()
                    .filter(s -> {
                        CareerTaskDTO task = careerTaskService.getById(s.getTaskId());
                        return task != null && levelId.equals(task.getLevelId());
                    })
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(list);
    }

    @PutMapping("/submissions/{id}/review")
    public ResponseEntity<?> reviewSubmission(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String reviewerId = auth.getPrincipal().toString();

        Integer points = (Integer) body.get("points");
        String status = (String) body.get("status");
        String comment = (String) body.get("comment");

        if (points == null || status == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Points and status are required."));
        }

        CareerSubmissionDTO submissionDto = careerSubmissionService.getById(id);
        if (submissionDto == null) {
            return ResponseEntity.notFound().build();
        }

        // Concurrency lock validation
        if (!"PENDING".equalsIgnoreCase(submissionDto.getStatus())) {
            return ResponseEntity.badRequest().body(Map.of("message", "This submission has already been reviewed."));
        }

        try {
            CareerSubmissionEntity submissionEntity = submissionRepository.findById(id).orElse(null);
            if (submissionEntity != null) {
                if ("APPROVED".equalsIgnoreCase(status)) {
                    careerProgressionService.awardPoints(submissionEntity, points, reviewerId, comment);
                } else {
                    submissionEntity.setStatus(status.toUpperCase());
                    UserEntity reviewer = userRepository.findById(reviewerId).orElse(null);
                    submissionEntity.setReviewer(reviewer);
                    submissionEntity.setReviewerComment(comment);
                    submissionEntity.setReviewedAt(LocalDateTime.now());
                    submissionRepository.save(submissionEntity);
                }
                return ResponseEntity.ok(careerSubmissionService.getById(id));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @Autowired
    private com.edusys.repository.StudentCareerProgressRepository progressRepository;

    @Autowired
    private com.edusys.repository.CareerLevelRepository levelRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        try {
            List<com.edusys.entity.CareerLevelEntity> levels = levelRepository.findAllOrderByLevelNumberAsc();
            if (levels.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "industryReadyCount", 0,
                    "levelStats", List.of()
                ));
            }

            int highestLevelNum = levels.get(levels.size() - 1).getLevelNumber();
            
            // Fetch all progress records
            Iterable<com.edusys.entity.StudentCareerProgressEntity> progressList = progressRepository.findAll();
            
            int industryReady = 0;
            java.util.Map<Integer, Integer> levelCompletedCounts = new java.util.HashMap<>();
            
            for (com.edusys.entity.CareerLevelEntity lvl : levels) {
                levelCompletedCounts.put(lvl.getLevelNumber(), 0);
            }

            for (com.edusys.entity.StudentCareerProgressEntity progress : progressList) {
                int studentLevelNum = progress.getCurrentLevel().getLevelNumber();
                if (studentLevelNum >= highestLevelNum) {
                    industryReady++;
                }
                
                for (com.edusys.entity.CareerLevelEntity lvl : levels) {
                    if (studentLevelNum >= lvl.getLevelNumber()) {
                        levelCompletedCounts.put(lvl.getLevelNumber(), levelCompletedCounts.get(lvl.getLevelNumber()) + 1);
                    }
                }
            }

            List<Map<String, Object>> levelStats = levels.stream().map(lvl -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("levelId", lvl.getId());
                map.put("levelNumber", lvl.getLevelNumber());
                map.put("title", lvl.getTitle());
                map.put("completedCount", levelCompletedCounts.get(lvl.getLevelNumber()));
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                "industryReadyCount", industryReady,
                "levelStats", levelStats
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/progress/override")
    public ResponseEntity<?> overrideLevel(
            @RequestParam String studentId,
            @RequestParam String levelId,
            @RequestParam(required = false) String reason) {
        try {
            com.edusys.entity.CareerLevelEntity level = levelRepository.findById(levelId)
                    .orElseThrow(() -> new IllegalArgumentException("Level not found"));
            com.edusys.entity.StudentCareerProgressEntity progress = progressRepository.findById(studentId).orElse(null);
            if (progress == null) {
                progress = com.edusys.entity.StudentCareerProgressEntity.builder()
                        .studentId(studentId)
                        .currentLevel(level)
                        .pointsAtLevel(0)
                        .build();
            } else {
                progress.setCurrentLevel(level);
                progress.setPointsAtLevel(0);
            }
            progressRepository.save(progress);
            return ResponseEntity.ok(Map.of("message", "Level override successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
