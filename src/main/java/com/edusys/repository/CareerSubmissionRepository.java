package com.edusys.repository;

import com.edusys.entity.CareerSubmissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerSubmissionRepository extends CrudRepository<CareerSubmissionEntity, String> {
}
