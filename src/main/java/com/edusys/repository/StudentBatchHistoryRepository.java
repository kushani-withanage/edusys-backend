package com.edusys.repository;

import com.edusys.entity.StudentBatchHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentBatchHistoryRepository extends CrudRepository<StudentBatchHistoryEntity, String> {
    Optional<StudentBatchHistoryEntity> findByStudentIdAndEndDateIsNull(String studentId);
    List<StudentBatchHistoryEntity> findByStudentId(String studentId);
}
