package com.edusys.repository;

import com.edusys.entity.QuestionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<QuestionEntity, String> {
    List<QuestionEntity> findByCourseId(String courseId);
    List<QuestionEntity> findByCourseIdAndDifficulty(String courseId, String difficulty);
    List<QuestionEntity> findByCourseIdAndStatus(String courseId, String status);
    List<QuestionEntity> findByCourseIdAndDifficultyAndStatus(String courseId, String difficulty, String status);
}
