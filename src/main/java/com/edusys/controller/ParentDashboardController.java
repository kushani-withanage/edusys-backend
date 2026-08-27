package com.edusys.controller;

import com.edusys.entity.*;
import com.edusys.model.dto.StudentDTO;
import com.edusys.repository.*;
import com.edusys.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/parent")
@CrossOrigin
public class ParentDashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCareerProgressRepository studentCareerProgressRepository;

    @Autowired
    private CareerStudentTaskStatusRepository careerStudentTaskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    private boolean verifyLink(String parentId, String studentId) {
        String sql = "SELECT COUNT(*) FROM (SELECT parent_id, student_id FROM student_parent UNION SELECT parent_id, student_id FROM parent_student_links) combined WHERE parent_id = ? AND student_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, parentId, studentId);
        return count != null && count > 0;
    }

    @GetMapping("/children")
    public ResponseEntity<?> getChildren() {
        String parentId = SecurityContextHolder.getContext().getAuthentication().getName();
        String sql = "SELECT student_id FROM (SELECT parent_id, student_id FROM student_parent UNION SELECT parent_id, student_id FROM parent_student_links) combined WHERE parent_id = ?";
        List<String> studentIds = jdbcTemplate.queryForList(sql, String.class, parentId);

        List<Map<String, Object>> children = new ArrayList<>();
        for (String id : studentIds) {
            StudentDTO dto = studentService.getById(id);
            if (dto != null) {
                Map<String, Object> childMap = new HashMap<>();
                childMap.put("studentId", dto.getStudentId());
                childMap.put("fullName", dto.getFullName());
                childMap.put("email", dto.getEmail());
                childMap.put("nic", dto.getNic());
                childMap.put("regNo", dto.getRegNo());
                childMap.put("currentBatchId", dto.getCurrentBatchId());

                String batchName = "Unassigned";
                if (dto.getCurrentBatchId() != null) {
                    Optional<BatchEntity> bOpt = batchRepository.findById(dto.getCurrentBatchId());
                    if (bOpt.isPresent()) {
                        batchName = bOpt.get().getBatchName();
                    }
                }
                childMap.put("batchName", batchName);
                children.add(childMap);
            }
        }
        return ResponseEntity.ok(children);
    }

    @GetMapping("/children/{studentId}/assignments")
    public ResponseEntity<?> getChildAssignments(@PathVariable String studentId) {
        String parentId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!verifyLink(parentId, studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Optional<StudentEntity> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        StudentEntity student = studentOpt.get();
        if (student.getCurrentBatchId() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        Optional<BatchEntity> batchOpt = batchRepository.findById(student.getCurrentBatchId());
        if (!batchOpt.isPresent()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        BatchEntity batch = batchOpt.get();
        List<Map<String, Object>> result = new ArrayList<>();

        if (batch.getCourses() != null) {
            ObjectMapper mapper = new ObjectMapper();
            for (CourseEntity course : batch.getCourses()) {
                if (course.getSections() == null) continue;
                try {
                    List<Map<String, Object>> sections = mapper.readValue(course.getSections(), List.class);
                    for (Map<String, Object> sect : sections) {
                        List<Map<String, Object>> items = (List<Map<String, Object>>) sect.get("items");
                        if (items == null) continue;
                        for (Map<String, Object> item : items) {
                            if ("assignment".equalsIgnoreCase((String) item.get("type"))) {
                                Map<String, Object> record = new HashMap<>();
                                record.put("courseId", course.getCourseId());
                                record.put("courseName", course.getCourseName());
                                record.put("assignmentId", item.get("id"));
                                record.put("title", item.get("title"));
                                record.put("dueDate", item.get("dueDate"));

                                List<Map<String, Object>> submissions = jdbcTemplate.queryForList(
                                        "SELECT submission_id, submit_date, submitted_file, marks, feedback FROM assignment_submissions WHERE student_id = ? AND assignment_id = ?",
                                        studentId, item.get("id")
                                );

                                if (!submissions.isEmpty()) {
                                    record.put("submission", submissions.get(0));
                                } else {
                                    record.put("submission", null);
                                }
                                result.add(record);
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignore parsing errors for individual course
                }
            }
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/children/{studentId}/exams")
    public ResponseEntity<?> getChildExams(@PathVariable String studentId) {
        String parentId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!verifyLink(parentId, studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        List<ExamAttemptEntity> attempts = examAttemptRepository.findByStudentId(studentId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ExamAttemptEntity attempt : attempts) {
            Optional<ExamEntity> examOpt = examRepository.findById(attempt.getExamId());
            if (examOpt.isPresent()) {
                ExamEntity exam = examOpt.get();
                Map<String, Object> record = new HashMap<>();
                record.put("attemptId", attempt.getId());
                record.put("examId", exam.getId());
                record.put("examTitle", exam.getTitle());
                record.put("startedAt", attempt.getStartedAt());
                record.put("submittedAt", attempt.getSubmittedAt());
                record.put("status", attempt.getStatus());
                record.put("score", attempt.getScore());

                String courseName = "General Exam";
                if (exam.getCourseId() != null) {
                    Optional<CourseEntity> cOpt = courseRepository.findById(exam.getCourseId());
                    if (cOpt.isPresent()) {
                        courseName = cOpt.get().getCourseName();
                    }
                }
                record.put("courseName", courseName);
                result.add(record);
            }
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/children/{studentId}/career-scale")
    public ResponseEntity<?> getChildCareerScale(@PathVariable String studentId) {
        String parentId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!verifyLink(parentId, studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Map<String, Object> progress = new HashMap<>();
        Optional<StudentCareerProgressEntity> progOpt = studentCareerProgressRepository.findById(studentId);

        if (progOpt.isPresent()) {
            StudentCareerProgressEntity scp = progOpt.get();
            progress.put("levelName", scp.getCurrentLevel().getTitle());
            progress.put("levelIndex", scp.getCurrentLevel().getLevelNumber());
            progress.put("points", scp.getPointsAtLevel());
            progress.put("pointsRequired", scp.getCurrentLevel().getPointsRequired());
        } else {
            progress.put("levelName", "Level 1");
            progress.put("levelIndex", 1);
            progress.put("points", 0);
            progress.put("pointsRequired", 100);
        }

        List<CareerStudentTaskStatusEntity> submissions = careerStudentTaskStatusRepository.findByStudent_StudentId(studentId);
        List<Map<String, Object>> submissionList = new ArrayList<>();

        for (CareerStudentTaskStatusEntity sub : submissions) {
            Map<String, Object> sMap = new HashMap<>();
            sMap.put("submissionId", sub.getId());
            sMap.put("taskTitle", sub.getTask().getTitle());
            sMap.put("status", sub.getStatus());
            sMap.put("pointsAwarded", sub.getPointsAwarded());
            sMap.put("feedback", sub.getComment());
            sMap.put("submittedAt", sub.getMarkedAt());
            submissionList.add(sMap);
        }

        progress.put("submissions", submissionList);
        return ResponseEntity.ok(progress);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> payload) {
        String parentId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOpt = userRepository.findById(parentId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userOpt.get();
        String newFullName = payload.get("fullName");
        if (newFullName != null && !newFullName.trim().isEmpty()) {
            user.setFullName(newFullName.trim());
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "userId", user.getUserId(),
            "fullName", user.getFullName(),
            "email", user.getEmail(),
            "role", user.getRole()
        ));
    }

    @PutMapping("/profile/password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> payload) {
        String parentId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOpt = userRepository.findById(parentId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = userOpt.get();
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        if (currentPassword == null || newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Current password and new password are required.");
        }

        if (user.getPassword() != null) {
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect current password.");
            }
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }
}
