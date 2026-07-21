package com.edusys.repository;

import com.edusys.entity.EvaluationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends CrudRepository<EvaluationEntity, String> {
}
