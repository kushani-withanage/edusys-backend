package com.edusys.repository;

import com.edusys.entity.ExamAudienceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamAudienceRepository extends CrudRepository<ExamAudienceEntity, String> {
    List<ExamAudienceEntity> findByExamId(String examId);
}
