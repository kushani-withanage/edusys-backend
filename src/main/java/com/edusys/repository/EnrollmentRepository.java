package com.edusys.repository;

import com.edusys.entity.EnrollmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends CrudRepository<EnrollmentEntity, String> {
}
