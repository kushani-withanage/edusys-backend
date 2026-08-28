package com.edusys.service.impl;

import com.edusys.entity.*;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ExamDTO;
import com.edusys.model.dto.ExamAudienceDTO;
import com.edusys.model.dto.QuestionDTO;
import com.edusys.model.dto.QuestionOptionDTO;
import com.edusys.repository.*;
import com.edusys.service.ExamService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ExamAudienceRepository examAudienceRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    @Transactional
    public ExamDTO create(ExamDTO dto) {
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            long count = examRepository.count();
            String generatedId;
            do {
                generatedId = idGenerator.generateId(EntityPrefix.EXAM, count++);
            } while (examRepository.existsById(generatedId));
            dto.setId(generatedId);
        }
        dto.setStatus("DRAFT");
        dto.setCreatedAt(LocalDateTime.now());

        ExamEntity entity = mapper.map(dto, ExamEntity.class);
        entity.setExamQuestions(new ArrayList<>());
        entity.setAudiences(new ArrayList<>());

        // Add questions
        if (dto.getQuestionIds() != null) {
            int index = 0;
            for (String qId : dto.getQuestionIds()) {
                QuestionEntity q = questionRepository.findById(qId).orElse(null);
                if (q != null) {
                    ExamQuestionEntity.ExamQuestionId eqId = new ExamQuestionEntity.ExamQuestionId(entity.getId(), qId);
                    ExamQuestionEntity eq = ExamQuestionEntity.builder()
                            .id(eqId)
                            .exam(entity)
                            .question(q)
                            .orderIndex(index++)
                            .build();
                    entity.getExamQuestions().add(eq);
                }
            }
        }

        // Add audiences
        if (dto.getAudiences() != null) {
            for (ExamAudienceDTO audDto : dto.getAudiences()) {
                audDto.setId(UUID.randomUUID().toString());
                ExamAudienceEntity audEntity = mapper.map(audDto, ExamAudienceEntity.class);
                audEntity.setExam(entity);
                entity.getAudiences().add(audEntity);
            }
        }

        ExamEntity saved = examRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public ExamDTO getById(String id) {
        return examRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<ExamDTO> getAll() {
        List<ExamDTO> list = new ArrayList<>();
        examRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    @Transactional
    public ExamDTO update(String id, ExamDTO dto) {
        ExamEntity existing = examRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        if (!"DRAFT".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Only exams in DRAFT status can be modified.");
        }

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setCourseId(dto.getCourseId());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setDurationMinutes(dto.getDurationMinutes());
        existing.setShuffleQuestions(dto.getShuffleQuestions() != null ? dto.getShuffleQuestions() : false);
        existing.setShuffleOptions(dto.getShuffleOptions() != null ? dto.getShuffleOptions() : false);
        existing.setAttemptsAllowed(dto.getAttemptsAllowed() != null ? dto.getAttemptsAllowed() : 1);
        existing.setPassMarks(dto.getPassMarks() != null ? dto.getPassMarks() : 40);

        // Update questions
        examQuestionRepository.deleteAll(existing.getExamQuestions());
        existing.getExamQuestions().clear();
        if (dto.getQuestionIds() != null) {
            int index = 0;
            for (String qId : dto.getQuestionIds()) {
                QuestionEntity q = questionRepository.findById(qId).orElse(null);
                if (q != null) {
                    ExamQuestionEntity.ExamQuestionId eqId = new ExamQuestionEntity.ExamQuestionId(id, qId);
                    ExamQuestionEntity eq = ExamQuestionEntity.builder()
                            .id(eqId)
                            .exam(existing)
                            .question(q)
                            .orderIndex(index++)
                            .build();
                    existing.getExamQuestions().add(eq);
                }
            }
        }

        // Update audiences
        examAudienceRepository.deleteAll(existing.getAudiences());
        existing.getAudiences().clear();
        if (dto.getAudiences() != null) {
            for (ExamAudienceDTO audDto : dto.getAudiences()) {
                audDto.setId(UUID.randomUUID().toString());
                ExamAudienceEntity audEntity = mapper.map(audDto, ExamAudienceEntity.class);
                audEntity.setExam(existing);
                existing.getAudiences().add(audEntity);
            }
        }

        ExamEntity updated = examRepository.save(existing);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        ExamEntity existing = examRepository.findById(id).orElse(null);
        if (existing != null) {
            if (!"DRAFT".equalsIgnoreCase(existing.getStatus())) {
                throw new IllegalStateException("Only exams in DRAFT status can be deleted.");
            }
            examRepository.delete(existing);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ExamDTO publish(String id) {
        ExamEntity existing = examRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        if (existing.getStartTime().isAfter(existing.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        if (existing.getExamQuestions() == null || existing.getExamQuestions().isEmpty()) {
            throw new IllegalStateException("Cannot publish an exam with no questions.");
        }

        existing.setStatus("PUBLISHED");

        // Lock all attached questions
        for (ExamQuestionEntity eq : existing.getExamQuestions()) {
            QuestionEntity q = eq.getQuestion();
            q.setStatus("LOCKED");
            questionRepository.save(q);
        }

        ExamEntity saved = examRepository.save(existing);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public ExamDTO close(String id) {
        ExamEntity existing = examRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setStatus("CLOSED");
        ExamEntity saved = examRepository.save(existing);
        return convertToDTO(saved);
    }

    @Override
    public List<QuestionDTO> getQuestionsForExam(String examId, boolean stripCorrectAnswers) {
        List<ExamQuestionEntity> examQuestions = examQuestionRepository.findByExamIdOrderByOrderIndexAsc(examId);
        List<QuestionDTO> list = new ArrayList<>();
        for (ExamQuestionEntity eq : examQuestions) {
            QuestionEntity qEntity = eq.getQuestion();
            QuestionDTO qDto = mapper.map(qEntity, QuestionDTO.class);
            if (eq.getMarksOverride() != null) {
                qDto.setDefaultMarks(eq.getMarksOverride());
            }
            
            // Map and sort options
            if (qEntity.getOptions() != null) {
                List<QuestionOptionDTO> optionDTOs = qEntity.getOptions().stream()
                        .map(o -> {
                            QuestionOptionDTO oDto = mapper.map(o, QuestionOptionDTO.class);
                            if (stripCorrectAnswers) {
                                oDto.setIsCorrect(false); // Never leak answers to client
                            }
                            return oDto;
                        })
                        .sorted(Comparator.comparing(QuestionOptionDTO::getOrderIndex))
                        .collect(Collectors.toList());
                qDto.setOptions(optionDTOs);
            }
            list.add(qDto);
        }
        return list;
    }

    @Override
    public Map<String, Object> getExamAnalytics(String examId) {
        Map<String, Object> analytics = new HashMap<>();
        ExamEntity exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            return analytics;
        }

        List<ExamAttemptEntity> attempts = examAttemptRepository.findByExamId(examId);
        List<ExamQuestionEntity> examQuestions = examQuestionRepository.findByExamIdOrderByOrderIndexAsc(examId);

        // Core stats
        int totalAttempts = attempts.size();
        double avgScore = 0.0;
        int passedCount = 0;
        int maxExamMarks = examQuestions.stream().mapToInt(eq -> eq.getMarksOverride() != null ? eq.getMarksOverride() : eq.getQuestion().getDefaultMarks()).sum();

        // Score distributions: 0-20, 20-40, 40-60, 60-80, 80-100
        int[] ranges = new int[5];

        List<Map<String, Object>> studentAttemptsList = new ArrayList<>();

        int passMarks = exam.getPassMarks() != null ? exam.getPassMarks() : 40;
        for (ExamAttemptEntity attempt : attempts) {
            double finalScore = attempt.getScore() != null ? attempt.getScore() : 0.0;
            avgScore += finalScore;

            if (finalScore >= passMarks) {
                passedCount++;
            }

            int rangeIndex = (int) (finalScore / 20.0);
            if (rangeIndex >= 5) rangeIndex = 4;
            ranges[rangeIndex]++;

            // Student profile details
            Optional<UserEntity> studentUser = userRepository.findById(attempt.getStudentId());
            Map<String, Object> map = new HashMap<>();
            map.put("attemptId", attempt.getId());
            map.put("studentId", attempt.getStudentId());
            map.put("studentName", studentUser.map(UserEntity::getFullName).orElse("Unknown student"));
            map.put("studentEmail", studentUser.map(UserEntity::getEmail).orElse(""));
            map.put("startedAt", attempt.getStartedAt());
            map.put("submittedAt", attempt.getSubmittedAt());
            map.put("status", attempt.getStatus());
            map.put("score", finalScore);
            studentAttemptsList.add(map);
        }

        avgScore = totalAttempts > 0 ? (avgScore / totalAttempts) : 0.0;
        double passRate = totalAttempts > 0 ? ((double) passedCount * 100.0 / totalAttempts) : 0.0;

        analytics.put("title", exam.getTitle());
        analytics.put("status", exam.getStatus());
        analytics.put("totalAttempts", totalAttempts);
        analytics.put("averageScore", avgScore);
        analytics.put("passRate", passRate);
        analytics.put("maxMarks", maxExamMarks);
        analytics.put("passMarks", passMarks);
        analytics.put("ranges", ranges);
        analytics.put("attempts", studentAttemptsList);

        // Correct rate per question
        List<Map<String, Object>> questionStats = new ArrayList<>();
        for (ExamQuestionEntity eq : examQuestions) {
            Map<String, Object> qStat = new HashMap<>();
            qStat.put("questionId", eq.getQuestion().getId());
            qStat.put("questionText", eq.getQuestion().getQuestionText());

            // Count correct answers
            int correctAnswersCount = 0;
            int totalQuestionAnswers = 0;
            for (ExamAttemptEntity attempt : attempts) {
                if (attempt.getAnswers() != null) {
                    for (ExamAnswerEntity ans : attempt.getAnswers()) {
                        if (ans.getQuestionId().equals(eq.getQuestion().getId())) {
                            totalQuestionAnswers++;
                            if (Boolean.TRUE.equals(ans.getIsCorrect())) {
                                correctAnswersCount++;
                            }
                        }
                    }
                }
            }

            double correctRate = totalQuestionAnswers > 0 ? ((double) correctAnswersCount * 100.0 / totalQuestionAnswers) : 0.0;
            qStat.put("correctRate", correctRate);
            qStat.put("totalAnswers", totalQuestionAnswers);
            questionStats.add(qStat);
        }
        analytics.put("questionStats", questionStats);

        return analytics;
    }

    private ExamDTO convertToDTO(ExamEntity entity) {
        ExamDTO dto = mapper.map(entity, ExamDTO.class);
        
        // Map questionIds
        if (entity.getExamQuestions() != null) {
            List<String> qIds = entity.getExamQuestions().stream()
                    .map(eq -> eq.getQuestion().getId())
                    .collect(Collectors.toList());
            dto.setQuestionIds(qIds);

            // Compute total marks
            int total = entity.getExamQuestions().stream()
                    .mapToInt(eq -> eq.getMarksOverride() != null ? eq.getMarksOverride() : eq.getQuestion().getDefaultMarks())
                    .sum();
            dto.setTotalMarks(total);
        }

        // Map audiences
        if (entity.getAudiences() != null) {
            List<ExamAudienceDTO> audiences = entity.getAudiences().stream()
                    .map(a -> mapper.map(a, ExamAudienceDTO.class))
                    .collect(Collectors.toList());
            dto.setAudiences(audiences);
        }

        return dto;
    }
}
