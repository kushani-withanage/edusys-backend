package com.edusys.service.impl;

import com.edusys.entity.ExamEntity;
import com.edusys.entity.QuestionBankEntity;
import com.edusys.entity.ExamAttemptEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ExamDTO;
import com.edusys.model.dto.QuestionBankDTO;
import com.edusys.model.dto.ExamAttemptDTO;
import com.edusys.model.dto.ExamSubmissionDTO;
import com.edusys.repository.ExamRepository;
import com.edusys.repository.QuestionBankRepository;
import com.edusys.repository.ExamAttemptRepository;
import com.edusys.service.ExamService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public ExamDTO create(ExamDTO examDTO) {
        if (examDTO.getExamId() == null || examDTO.getExamId().trim().isEmpty()) {
            examDTO.setExamId(idGenerator.generateId(EntityPrefix.EXAM, examRepository.count()));
        }
        ExamEntity entity = mapper.map(examDTO, ExamEntity.class);
        if (examDTO.getQuestionIds() != null) {
            List<QuestionBankEntity> questionEntities = new ArrayList<>();
            for (String qId : examDTO.getQuestionIds()) {
                questionBankRepository.findById(qId).ifPresent(questionEntities::add);
            }
            entity.setQuestions(questionEntities);
        }
        ExamEntity saved = examRepository.save(entity);
        ExamDTO responseDto = mapper.map(saved, ExamDTO.class);
        if (saved.getQuestions() != null) {
            List<String> qIds = new ArrayList<>();
            for (QuestionBankEntity q : saved.getQuestions()) {
                qIds.add(q.getQuestionId());
            }
            responseDto.setQuestionIds(qIds);
        }
        return responseDto;
    }

    @Override
    public ExamDTO getById(String id) {
        return examRepository.findById(id)
                .map(entity -> {
                    ExamDTO dto = mapper.map(entity, ExamDTO.class);
                    if (entity.getQuestions() != null) {
                        List<String> qIds = new ArrayList<>();
                        for (QuestionBankEntity q : entity.getQuestions()) {
                            qIds.add(q.getQuestionId());
                        }
                        dto.setQuestionIds(qIds);
                    }
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<ExamDTO> getAll() {
        List<ExamDTO> list = new ArrayList<>();
        examRepository.findAll().forEach(entity -> {
            ExamDTO dto = mapper.map(entity, ExamDTO.class);
            if (entity.getQuestions() != null) {
                List<String> qIds = new ArrayList<>();
                for (QuestionBankEntity q : entity.getQuestions()) {
                    qIds.add(q.getQuestionId());
                }
                dto.setQuestionIds(qIds);
            }
            list.add(dto);
        });
        return list;
    }

    @Override
    public ExamDTO update(String id, ExamDTO examDTO) {
        if (!examRepository.existsById(id)) {
            return null;
        }
        examDTO.setExamId(id);
        ExamEntity entity = mapper.map(examDTO, ExamEntity.class);
        if (examDTO.getQuestionIds() != null) {
            List<QuestionBankEntity> questionEntities = new ArrayList<>();
            for (String qId : examDTO.getQuestionIds()) {
                questionBankRepository.findById(qId).ifPresent(questionEntities::add);
            }
            entity.setQuestions(questionEntities);
        }
        ExamEntity updated = examRepository.save(entity);
        ExamDTO responseDto = mapper.map(updated, ExamDTO.class);
        if (updated.getQuestions() != null) {
            List<String> qIds = new ArrayList<>();
            for (QuestionBankEntity q : updated.getQuestions()) {
                qIds.add(q.getQuestionId());
            }
            responseDto.setQuestionIds(qIds);
        }
        return responseDto;
    }

    @Override
    public boolean delete(String id) {
        if (examRepository.existsById(id)) {
            examRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<QuestionBankDTO> getQuestionsForExam(String examId) {
        ExamEntity exam = examRepository.findById(examId).orElse(null);
        if (exam == null || exam.getQuestions() == null) {
            return new ArrayList<>();
        }
        List<QuestionBankDTO> dtos = new ArrayList<>();
        for (QuestionBankEntity question : exam.getQuestions()) {
            QuestionBankDTO dto = mapper.map(question, QuestionBankDTO.class);
            dto.setCorrectAnswers(null); // Hide answers for exam-taking
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ExamAttemptDTO submitExam(String examId, ExamSubmissionDTO submission) {
        ExamEntity exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            return null;
        }

        int earnedMarks = 0;
        int examTotalMarks = 0;

        if (submission.getAnswers() != null && exam.getQuestions() != null) {
            for (QuestionBankEntity question : exam.getQuestions()) {
                int questionMarks = question.getMarks() != null ? question.getMarks() : 0;
                examTotalMarks += questionMarks;

                List<String> studentAnsList = submission.getAnswers().get(question.getQuestionId());
                List<String> correctAnsList = question.getCorrectAnswers();

                if (studentAnsList != null && !studentAnsList.isEmpty() && correctAnsList != null && !correctAnsList.isEmpty()) {
                    String studentAns = studentAnsList.get(0).trim();
                    String correctAns = correctAnsList.get(0).trim();

                    if (question.getQuestionType() != null && question.getQuestionType().equalsIgnoreCase("Short Answer")) {
                        if (studentAns.equalsIgnoreCase(correctAns)) {
                            earnedMarks += questionMarks;
                        }
                    } else {
                        // MCQ
                        if (studentAns.equals(correctAns)) {
                            earnedMarks += questionMarks;
                        }
                    }
                }
            }
        }

        Double scorePercent = examTotalMarks > 0 ? ((double) earnedMarks * 100.0) / examTotalMarks : 0.0;
        String resultStatus = scorePercent >= 50.0 ? "PASS" : "FAIL";

        ExamAttemptEntity attempt = ExamAttemptEntity.builder()
                .attemptId(idGenerator.generateId(EntityPrefix.EXAM_ATTEMPT, examAttemptRepository.count()))
                .examId(examId)
                .studentId(submission.getStudentId())
                .startTime(submission.getStartTime() != null ? submission.getStartTime() : LocalDateTime.now().minusMinutes(30))
                .submitTime(LocalDateTime.now())
                .status(resultStatus)
                .score(scorePercent)
                .build();

        ExamAttemptEntity saved = examAttemptRepository.save(attempt);
        return mapper.map(saved, ExamAttemptDTO.class);
    }
}
