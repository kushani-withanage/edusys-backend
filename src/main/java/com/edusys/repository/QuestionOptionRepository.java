package com.edusys.repository;

import com.edusys.entity.QuestionOptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionOptionRepository extends CrudRepository<QuestionOptionEntity, String> {
    List<QuestionOptionEntity> findByQuestionId(String questionId);
}
