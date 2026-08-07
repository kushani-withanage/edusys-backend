package com.edusys.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.edusys.entity.ExamAttemptEntity;
import com.edusys.entity.ExamAnswerEntity;
import com.edusys.entity.ExamQuestionEntity;
import com.edusys.entity.QuestionEntity;
import com.edusys.entity.QuestionOptionEntity;
import com.edusys.repository.ExamAnswerRepository;
import com.edusys.repository.ExamQuestionRepository;
import com.edusys.repository.QuestionRepository;
import com.edusys.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GradingServiceImpl implements GradingService {

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamAnswerRepository examAnswerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public void gradeAttempt(ExamAttemptEntity attempt) {
        List<ExamQuestionEntity> examQuestions = examQuestionRepository.findByExamIdOrderByOrderIndexAsc(attempt.getExamId());
        List<ExamAnswerEntity> answers = examAnswerRepository.findByAttemptId(attempt.getId());

        double totalMarksAwarded = 0.0;

        for (ExamQuestionEntity eq : examQuestions) {
            QuestionEntity q = eq.getQuestion();
            int questionMaxMarks = eq.getMarksOverride() != null ? eq.getMarksOverride() : q.getDefaultMarks();

            // Find student answer
            Optional<ExamAnswerEntity> ansOpt = answers.stream()
                    .filter(a -> a.getQuestionId().equals(q.getId()))
                    .findFirst();

            if (!ansOpt.isPresent()) {
                // Unanswered question gets 0 marks and is explicitly stored in db
                ExamAnswerEntity unanswered = ExamAnswerEntity.builder()
                        .id(java.util.UUID.randomUUID().toString())
                        .attempt(attempt)
                        .questionId(q.getId())
                        .selectedOptionIds("[]")
                        .isCorrect(false)
                        .marksAwarded(0.0)
                        .build();
                examAnswerRepository.save(unanswered);
                continue;
            }

            ExamAnswerEntity answer = ansOpt.get();
            List<String> studentSelections = new ArrayList<>();
            if (answer.getSelectedOptionIds() != null && !answer.getSelectedOptionIds().trim().isEmpty()) {
                try {
                    studentSelections = objectMapper.readValue(answer.getSelectedOptionIds(), new TypeReference<List<String>>() {});
                } catch (Exception e) {
                    // ignore
                }
            }

            // Get correct options
            List<String> correctOptionIds = q.getOptions().stream()
                    .filter(QuestionOptionEntity::getIsCorrect)
                    .map(QuestionOptionEntity::getId)
                    .collect(Collectors.toList());

            boolean isCorrect = false;

            if ("SINGLE_CHOICE".equalsIgnoreCase(q.getQuestionType())) {
                if (studentSelections.size() == 1 && correctOptionIds.contains(studentSelections.get(0))) {
                    isCorrect = true;
                }
            } else {
                // MULTI_CHOICE exact set match rule
                Set<String> studentSet = new HashSet<>(studentSelections);
                Set<String> correctSet = new HashSet<>(correctOptionIds);
                if (studentSet.equals(correctSet)) {
                    isCorrect = true;
                }
            }

            double marksAwarded = isCorrect ? (double) questionMaxMarks : 0.0;
            totalMarksAwarded += marksAwarded;

            answer.setIsCorrect(isCorrect);
            answer.setMarksAwarded(marksAwarded);
            examAnswerRepository.save(answer);
        }

        attempt.setScore(totalMarksAwarded);
    }
}
