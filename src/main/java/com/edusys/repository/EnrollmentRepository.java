package com.edusys.repository;

import com.edusys.entity.EnrollmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends CrudRepository<EnrollmentEntity, String> {
    long countByBatchId(String batchId);
    List<EnrollmentEntity> findByStudentId(String studentId);
}
