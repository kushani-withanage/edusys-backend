package com.edusys.repository;

import com.edusys.entity.ExamAnswerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAnswerRepository extends CrudRepository<ExamAnswerEntity, String> {
    List<ExamAnswerEntity> findByAttemptId(String attemptId);
    Optional<ExamAnswerEntity> findByAttemptIdAndQuestionId(String attemptId, String questionId);
}
