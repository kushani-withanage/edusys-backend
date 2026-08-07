package com.edusys.repository;

import com.edusys.entity.ExamAttemptEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptRepository extends CrudRepository<ExamAttemptEntity, String> {
    List<ExamAttemptEntity> findByExamId(String examId);
    List<ExamAttemptEntity> findByStudentId(String studentId);
    List<ExamAttemptEntity> findByExamIdAndStudentId(String examId, String studentId);
    List<ExamAttemptEntity> findByExamIdAndStudentIdAndStatus(String examId, String studentId, String status);
    List<ExamAttemptEntity> findByStatus(String status);
    List<ExamAttemptEntity> findByStatusAndStartedAtBefore(String status, LocalDateTime thresholdTime);
}
