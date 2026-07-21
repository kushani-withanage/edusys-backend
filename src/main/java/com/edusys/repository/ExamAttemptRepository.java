package com.edusys.repository;

import com.edusys.entity.ExamAttemptEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamAttemptRepository extends CrudRepository<ExamAttemptEntity, String> {
}
