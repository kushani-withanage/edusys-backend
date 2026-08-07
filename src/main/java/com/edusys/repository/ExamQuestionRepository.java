package com.edusys.repository;

import com.edusys.entity.ExamQuestionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends CrudRepository<ExamQuestionEntity, ExamQuestionEntity.ExamQuestionId> {
    List<ExamQuestionEntity> findByExamIdOrderByOrderIndexAsc(String examId);
    boolean existsByQuestionIdAndExamStatusIn(String questionId, List<String> statuses);
}
