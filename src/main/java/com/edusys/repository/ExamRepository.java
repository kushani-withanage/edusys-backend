package com.edusys.repository;

import com.edusys.entity.ExamEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends CrudRepository<ExamEntity, String> {
    List<ExamEntity> findByCourseId(String courseId);
    List<ExamEntity> findByStatus(String status);

    @Query("SELECT e FROM ExamEntity e JOIN e.audiences a " +
           "WHERE e.status = :status " +
           "AND ((a.targetType = 'BATCH' AND a.targetId IN :batchIds) " +
           "OR (a.targetType = 'MODULE' AND a.targetId IN :courseIds))")
    List<ExamEntity> findAvailableExams(
            @Param("status") String status,
            @Param("batchIds") List<String> batchIds,
            @Param("courseIds") List<String> courseIds
    );
}
