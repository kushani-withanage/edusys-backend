package com.edusys.repository;

import com.edusys.entity.ExamEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends CrudRepository<ExamEntity, String> {
}
