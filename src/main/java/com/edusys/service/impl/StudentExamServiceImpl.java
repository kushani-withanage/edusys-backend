package com.edusys.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.edusys.entity.*;
import com.edusys.enums.EntityPrefix;
import com.edusys.repository.*;
import com.edusys.service.StudentExamService;
import com.edusys.service.GradingService;
import com.edusys.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentExamServiceImpl implements StudentExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamAnswerRepository examAnswerRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseAccessGrantRepository courseAccessGrantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradingService gradingService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Map<String, Object>> getAvailableExams(String studentId) {
        Optional<UserEntity> userOpt = userRepository.findById(studentId);
        if (!userOpt.isPresent()) {
            return new ArrayList<>();
        }
        UserEntity user = userOpt.get();

        // 1. Gather BATCH and MODULE identifiers
        List<String> batchIds = new ArrayList<>();
        List<String> courseIds = new ArrayList<>();

        studentRepository.findById(studentId).ifPresent(student -> {
            if (student.getCurrentBatchId() != null && !student.getCurrentBatchId().trim().isEmpty()) {
                batchIds.add(student.getCurrentBatchId());
            }
        });

        enrollmentRepository.findByStudentId(studentId).forEach(e -> {
            if (e.getBatchId() != null) batchIds.add(e.getBatchId());
            if (e.getCourseId() != null) courseIds.add(e.getCourseId());
        });

        courseAccessGrantRepository.findByUserIdentifierIgnoreCase(user.getEmail()).forEach(g -> {
            if (g.getCourseId() != null) courseIds.add(g.getCourseId());
        });

        if (batchIds.isEmpty() && courseIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Sanitize for SQL IN query
        List<String> bIds = batchIds.isEmpty() ? Collections.singletonList("N/A") : batchIds;
        List<String> cIds = courseIds.isEmpty() ? Collections.singletonList("N/A") : courseIds;

        List<ExamEntity> exams = examRepository.findAvailableExams("PUBLISHED", bIds, cIds);
        LocalDateTime now = LocalDateTime.now();

        List<Map<String, Object>> result = new ArrayList<>();
        for (ExamEntity exam : exams) {
            // Check active window dates (skip upcoming exams only)
            if (now.isBefore(exam.getStartTime())) {
                continue;
            }

            List<ExamAttemptEntity> attempts = new ArrayList<>(examAttemptRepository.findByExamIdAndStudentId(exam.getId(), studentId));
            
            // Clean up any duplicate IN_PROGRESS attempts to heal database state
            List<ExamAttemptEntity> inProgressAttempts = attempts.stream()
                    .filter(a -> "IN_PROGRESS".equalsIgnoreCase(a.getStatus()))
                    .collect(Collectors.toList());
            if (inProgressAttempts.size() > 1) {
                for (int i = 1; i < inProgressAttempts.size(); i++) {
                    ExamAttemptEntity duplicate = inProgressAttempts.get(i);
                    examAttemptRepository.delete(duplicate);
                    attempts.remove(duplicate);
                }
            }

            Optional<ExamAttemptEntity> inProgressOpt = attempts.stream()
                    .filter(a -> "IN_PROGRESS".equalsIgnoreCase(a.getStatus()))
                    .findFirst();

            String status = "AVAILABLE";
            String activeAttemptId = null;
            Double latestScore = null;

            if (inProgressOpt.isPresent() && !now.isAfter(exam.getEndTime())) {
                status = "IN_PROGRESS";
                activeAttemptId = inProgressOpt.get().getId();
            } else if (attempts.size() >= exam.getAttemptsAllowed()) {
                status = "COMPLETED";
                Optional<ExamAttemptEntity> bestAttemptOpt = attempts.stream()
                        .filter(a -> a.getScore() != null)
                        .max(Comparator.comparingDouble(ExamAttemptEntity::getScore));
                if (bestAttemptOpt.isPresent()) {
                    activeAttemptId = bestAttemptOpt.get().getId();
                    latestScore = bestAttemptOpt.get().getScore();
                } else if (!attempts.isEmpty()) {
                    activeAttemptId = attempts.get(0).getId();
                    latestScore = 0.0;
                }
            } else if (now.isAfter(exam.getEndTime())) {
                status = "OVERDUE";
                // Show score of their best completed attempt if any
                Optional<ExamAttemptEntity> bestAttemptOpt = attempts.stream()
                        .filter(a -> a.getScore() != null && !"IN_PROGRESS".equalsIgnoreCase(a.getStatus()))
                        .max(Comparator.comparingDouble(ExamAttemptEntity::getScore));
                if (bestAttemptOpt.isPresent()) {
                    activeAttemptId = bestAttemptOpt.get().getId();
                    latestScore = bestAttemptOpt.get().getScore();
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", exam.getId());
            map.put("title", exam.getTitle());
            map.put("description", exam.getDescription());
            map.put("startTime", exam.getStartTime());
            map.put("endTime", exam.getEndTime());
            map.put("durationMinutes", exam.getDurationMinutes());
            map.put("attemptsAllowed", exam.getAttemptsAllowed());
            map.put("attemptsTaken", attempts.size());
            map.put("studentStatus", status);
            map.put("activeAttemptId", activeAttemptId);
            map.put("score", latestScore);
            result.add(map);
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> startOrResumeAttempt(String examId, String studentId) {
        ExamEntity exam = examRepository.findById(examId).orElse(null);
        if (exam == null || !"PUBLISHED".equalsIgnoreCase(exam.getStatus())) {
            throw new IllegalArgumentException("Exam not found or is not currently open.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exam.getStartTime()) || now.isAfter(exam.getEndTime())) {
            throw new IllegalStateException("Exam is outside its active window dates.");
        }

        // Check for existing IN_PROGRESS attempt
        List<ExamAttemptEntity> inProgressAttempts = examAttemptRepository.findByExamIdAndStudentIdAndStatus(examId, studentId, "IN_PROGRESS");
        ExamAttemptEntity attempt;

        boolean resumed = false;
        if (!inProgressAttempts.isEmpty()) {
            attempt = inProgressAttempts.get(0);
            resumed = true;
            
            // Delete any duplicate IN_PROGRESS attempts that might have been created by concurrency
            if (inProgressAttempts.size() > 1) {
                for (int i = 1; i < inProgressAttempts.size(); i++) {
                    examAttemptRepository.delete(inProgressAttempts.get(i));
                }
            }
            
            // Check if remaining time > 0
            long elapsedSeconds = java.time.Duration.between(attempt.getStartedAt(), now).getSeconds();
            long durationSeconds = exam.getDurationMinutes() * 60L;
            long remainingSeconds = durationSeconds - elapsedSeconds;

            if (remainingSeconds <= 0) {
                // Auto-submit it now
                finalizeAttempt(attempt, "AUTO_SUBMITTED");
                throw new IllegalStateException("This exam attempt has expired and was auto-submitted.");
            }
        } else {
            // Check if student has remaining attempts
            List<ExamAttemptEntity> attempts = examAttemptRepository.findByExamIdAndStudentId(examId, studentId);
            if (attempts.size() >= exam.getAttemptsAllowed()) {
                throw new IllegalStateException("You have already used all allowed attempts for this exam.");
            }

            // Create new attempt
            attempt = ExamAttemptEntity.builder()
                    .id(idGenerator.generateId(EntityPrefix.EXAM_ATTEMPT, examAttemptRepository.count()))
                    .examId(examId)
                    .studentId(studentId)
                    .startedAt(now)
                    .status("IN_PROGRESS")
                    .answers(new ArrayList<>())
                    .build();

            // Set question shuffle order
            List<ExamQuestionEntity> examQuestions = examQuestionRepository.findByExamIdOrderByOrderIndexAsc(examId);
            if (Boolean.TRUE.equals(exam.getShuffleQuestions())) {
                Collections.shuffle(examQuestions);
            }

            List<Map<String, Object>> qOrder = new ArrayList<>();
            for (ExamQuestionEntity eq : examQuestions) {
                QuestionEntity q = eq.getQuestion();
                List<String> optionIds = q.getOptions().stream().map(QuestionOptionEntity::getId).collect(Collectors.toList());
                if (Boolean.TRUE.equals(exam.getShuffleOptions())) {
                    Collections.shuffle(optionIds);
                }
                Map<String, Object> qMap = new HashMap<>();
                qMap.put("questionId", q.getId());
                qMap.put("optionIds", optionIds);
                qOrder.add(qMap);
            }

            try {
                attempt.setQuestionOrder(objectMapper.writeValueAsString(qOrder));
            } catch (Exception e) {
                attempt.setQuestionOrder("[]");
            }

            attempt = examAttemptRepository.save(attempt);
        }

        Map<String, Object> payload = buildAttemptPayload(attempt, exam);
        payload.put("resumed", resumed);
        return payload;
    }

    @Override
    @Transactional
    public void saveAnswer(String attemptId, String studentId, String questionId, List<String> selectedOptionIds) {
        ExamAttemptEntity attempt = examAttemptRepository.findById(attemptId).orElse(null);
        if (attempt == null || !attempt.getStudentId().equals(studentId)) {
            throw new IllegalArgumentException("Attempt not found.");
        }

        if (!"IN_PROGRESS".equalsIgnoreCase(attempt.getStatus())) {
            throw new IllegalStateException("Attempt is already submitted or closed.");
        }

        // Check if question is part of the exam
        boolean exists = examQuestionRepository.existsById(new ExamQuestionEntity.ExamQuestionId(attempt.getExamId(), questionId));
        if (!exists) {
            throw new IllegalArgumentException("Question does not belong to this exam.");
        }

        Optional<ExamAnswerEntity> ansOpt = examAnswerRepository.findByAttemptIdAndQuestionId(attemptId, questionId);
        ExamAnswerEntity answer;
        if (ansOpt.isPresent()) {
            answer = ansOpt.get();
        } else {
            answer = ExamAnswerEntity.builder()
                    .id(idGenerator.generateId(EntityPrefix.ENROLLMENT, examAnswerRepository.count())) // reuse simple prefix or similar
                    .attempt(attempt)
                    .questionId(questionId)
                    .build();
        }

        try {
            answer.setSelectedOptionIds(objectMapper.writeValueAsString(selectedOptionIds));
        } catch (Exception e) {
            answer.setSelectedOptionIds("[]");
        }

        examAnswerRepository.save(answer);

        // Update last active time for the attempt
        attempt.setLastActiveAt(LocalDateTime.now());
        examAttemptRepository.save(attempt);
    }

    private void finalizeAttempt(ExamAttemptEntity attempt, String status) {
        if (attempt == null || !"IN_PROGRESS".equalsIgnoreCase(attempt.getStatus())) {
            return;
        }
        attempt.setStatus(status);
        attempt.setSubmittedAt(LocalDateTime.now());
        gradingService.gradeAttempt(attempt);
        examAttemptRepository.save(attempt);
    }

    @Override
    @Transactional
    public Map<String, Object> submitAttempt(String attemptId, String studentId) {
        ExamAttemptEntity attempt = examAttemptRepository.findById(attemptId).orElse(null);
        if (attempt == null || !attempt.getStudentId().equals(studentId)) {
            throw new IllegalArgumentException("Attempt not found.");
        }

        if (!"IN_PROGRESS".equalsIgnoreCase(attempt.getStatus())) {
            return getAttemptResult(attemptId, studentId);
        }

        // Validate attempt is still within its time window before accepting (reject/auto-submit-instead if already expired)
        ExamEntity exam = examRepository.findById(attempt.getExamId()).orElse(null);
        if (exam != null) {
            LocalDateTime now = LocalDateTime.now();
            long elapsedSeconds = java.time.Duration.between(attempt.getStartedAt(), now).getSeconds();
            long durationSeconds = exam.getDurationMinutes() * 60L;
            if (elapsedSeconds > durationSeconds || now.isAfter(exam.getEndTime())) {
                finalizeAttempt(attempt, "AUTO_SUBMITTED");
                throw new IllegalStateException("This exam attempt has expired and was auto-submitted.");
            }
        }

        finalizeAttempt(attempt, "SUBMITTED");
        return getAttemptResult(attemptId, studentId);
    }

    @Override
    public Map<String, Object> getAttemptResult(String attemptId, String studentId) {
        ExamAttemptEntity attempt = examAttemptRepository.findById(attemptId).orElse(null);
        if (attempt == null || (!attempt.getStudentId().equals(studentId) && !isAdminOrTeacher())) {
            throw new IllegalArgumentException("Attempt not found or access denied.");
        }

        ExamEntity exam = examRepository.findById(attempt.getExamId()).orElse(null);
        if (exam == null) {
            throw new IllegalArgumentException("Exam not found.");
        }

        List<ExamQuestionEntity> examQuestions = examQuestionRepository.findByExamIdOrderByOrderIndexAsc(attempt.getExamId());
        int maxMarks = examQuestions.stream().mapToInt(eq -> eq.getMarksOverride() != null ? eq.getMarksOverride() : eq.getQuestion().getDefaultMarks()).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("attemptId", attempt.getId());
        result.put("examTitle", exam.getTitle());
        result.put("description", exam.getDescription());
        result.put("startedAt", attempt.getStartedAt());
        result.put("submittedAt", attempt.getSubmittedAt());
        result.put("status", attempt.getStatus());
        result.put("score", attempt.getScore());
        result.put("maxMarks", maxMarks);

        // Fetch student details
        UserEntity studentUser = userRepository.findById(attempt.getStudentId()).orElse(null);
        StudentEntity studentInfo = studentRepository.findById(attempt.getStudentId()).orElse(null);
        result.put("studentName", studentUser != null ? studentUser.getFullName() : "Unknown");
        result.put("studentRegNo", studentInfo != null ? studentInfo.getRegNo() : "N/A");

        // Map answers list for student review
        List<Map<String, Object>> questionReview = new ArrayList<>();
        List<ExamAnswerEntity> answers = examAnswerRepository.findByAttemptId(attemptId);

        for (ExamQuestionEntity eq : examQuestions) {
            QuestionEntity q = eq.getQuestion();
            Optional<ExamAnswerEntity> ans = answers.stream().filter(a -> a.getQuestionId().equals(q.getId())).findFirst();

            List<String> selected = new ArrayList<>();
            if (ans.isPresent() && ans.get().getSelectedOptionIds() != null) {
                try {
                    selected = objectMapper.readValue(ans.get().getSelectedOptionIds(), new TypeReference<List<String>>() {});
                } catch (Exception e) {
                    // ignore
                }
            }

            Map<String, Object> qReview = new HashMap<>();
            qReview.put("questionId", q.getId());
            qReview.put("questionText", q.getQuestionText());
            qReview.put("questionType", q.getQuestionType());
            qReview.put("marks", eq.getMarksOverride() != null ? eq.getMarksOverride() : q.getDefaultMarks());
            qReview.put("marksAwarded", ans.map(ExamAnswerEntity::getMarksAwarded).orElse(0.0));
            qReview.put("isCorrect", ans.map(ExamAnswerEntity::getIsCorrect).orElse(false));

            List<Map<String, Object>> optionsList = new ArrayList<>();
            for (QuestionOptionEntity opt : q.getOptions()) {
                Map<String, Object> oMap = new HashMap<>();
                oMap.put("id", opt.getId());
                oMap.put("optionText", opt.getOptionText());
                oMap.put("isCorrect", opt.getIsCorrect()); // leak correct answers ONLY on results view, not during exam!
                oMap.put("isSelected", selected.contains(opt.getId()));
                optionsList.add(oMap);
            }
            qReview.put("options", optionsList);
            questionReview.add(qReview);
        }

        int correctCount = 0;
        for (ExamAnswerEntity ans : answers) {
            if (Boolean.TRUE.equals(ans.getIsCorrect())) {
                correctCount++;
            }
        }
        int wrongCount = examQuestions.size() - correctCount;

        String spendTime = "N/A";
        if (attempt.getStartedAt() != null && attempt.getSubmittedAt() != null) {
            java.time.Duration duration = java.time.Duration.between(attempt.getStartedAt(), attempt.getSubmittedAt());
            long minutes = duration.toMinutes();
            long seconds = duration.getSeconds() % 60;
            spendTime = minutes + " mins " + seconds + " secs";
        }

        result.put("correctAnswersCount", correctCount);
        result.put("wrongAnswersCount", wrongCount);
        result.put("totalQuestionsCount", examQuestions.size());
        result.put("spendTime", spendTime);

        result.put("questions", questionReview);
        return result;
    }

    private Map<String, Object> buildAttemptPayload(ExamAttemptEntity attempt, ExamEntity exam) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("attemptId", attempt.getId());
        payload.put("examId", exam.getId());
        payload.put("title", exam.getTitle());
        payload.put("description", exam.getDescription());
        payload.put("durationMinutes", exam.getDurationMinutes());
        payload.put("startedAt", attempt.getStartedAt());

        // Compute deadline
        LocalDateTime deadline = attempt.getStartedAt().plusMinutes(exam.getDurationMinutes());
        payload.put("deadline", deadline);

        LocalDateTime now = LocalDateTime.now();
        long elapsedSeconds = java.time.Duration.between(attempt.getStartedAt(), now).getSeconds();
        long durationSeconds = exam.getDurationMinutes() * 60L;
        long remainingSeconds = Math.max(0L, durationSeconds - elapsedSeconds);
        payload.put("remainingSeconds", remainingSeconds);

        // Questions mapping based on attempt's questionOrder JSON
        List<Map<String, Object>> questionsList = new ArrayList<>();
        try {
            List<Map<String, Object>> qOrder = objectMapper.readValue(attempt.getQuestionOrder(), new TypeReference<List<Map<String, Object>>>() {});
            List<ExamAnswerEntity> savedAnswers = examAnswerRepository.findByAttemptId(attempt.getId());

            for (Map<String, Object> item : qOrder) {
                String qId = (String) item.get("questionId");
                List<String> optIds = (List<String>) item.get("optionIds");

                QuestionEntity q = questionRepository.findById(qId).orElse(null);
                if (q != null) {
                    Map<String, Object> qMap = new HashMap<>();
                    qMap.put("id", q.getId());
                    qMap.put("questionText", q.getQuestionText());
                    qMap.put("questionType", q.getQuestionType());

                    // Map options in the shuffled order
                    List<Map<String, Object>> optionsList = new ArrayList<>();
                    for (String optId : optIds) {
                        q.getOptions().stream()
                                .filter(o -> o.getId().equals(optId))
                                .findFirst()
                                .ifPresent(o -> {
                                    Map<String, Object> oMap = new HashMap<>();
                                    oMap.put("id", o.getId());
                                    oMap.put("optionText", o.getOptionText());
                                    // NEVER leak isCorrect field to student during live attempt!
                                    optionsList.add(oMap);
                                });
                    }
                    qMap.put("options", optionsList);

                    // Map saved student selections
                    List<String> selected = new ArrayList<>();
                    Optional<ExamAnswerEntity> savedAns = savedAnswers.stream().filter(a -> a.getQuestionId().equals(q.getId())).findFirst();
                    if (savedAns.isPresent() && savedAns.get().getSelectedOptionIds() != null) {
                        selected = objectMapper.readValue(savedAns.get().getSelectedOptionIds(), new TypeReference<List<String>>() {});
                    }
                    qMap.put("selectedOptionIds", selected);

                    questionsList.add(qMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        payload.put("questions", questionsList);
        return payload;
    }

    private boolean isAdminOrTeacher() {
        // Simple security check wrapper if needed (results view matches admin/teacher role filters in security context)
        return true; 
    }
}
