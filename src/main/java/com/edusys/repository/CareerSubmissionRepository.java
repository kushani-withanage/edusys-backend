package com.edusys.repository;

import com.edusys.entity.CareerSubmissionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerSubmissionRepository extends CrudRepository<CareerSubmissionEntity, String> {
    @Query("SELECT s FROM CareerSubmissionEntity s WHERE s.student.studentId = :studentId ORDER BY s.submittedAt DESC")
    List<CareerSubmissionEntity> findByStudentId(@Param("studentId") String studentId);

    @Query("SELECT s FROM CareerSubmissionEntity s WHERE s.status = :status ORDER BY s.submittedAt ASC")
    List<CareerSubmissionEntity> findByStatus(@Param("status") String status);

    @Query("SELECT s FROM CareerSubmissionEntity s ORDER BY s.submittedAt DESC")
    List<CareerSubmissionEntity> findAllOrderBySubmittedAtDesc();

    @Query("SELECT COUNT(s) > 0 FROM CareerSubmissionEntity s WHERE s.student.studentId = :studentId AND s.task.id = :taskId AND s.status IN ('PENDING', 'REVISION_REQUESTED')")
    boolean hasActiveSubmission(@Param("studentId") String studentId, @Param("taskId") String taskId);

    @Query("SELECT s FROM CareerSubmissionEntity s WHERE s.task.id = :taskId")
    List<CareerSubmissionEntity> findByTaskId(@Param("taskId") String taskId);

    @Query("SELECT s FROM CareerSubmissionEntity s WHERE s.task.id = :taskId AND s.student.studentId = :studentId")
    Optional<CareerSubmissionEntity> findByTaskIdAndStudentId(@Param("taskId") String taskId, @Param("studentId") String studentId);
    
    @Query("SELECT s FROM CareerSubmissionEntity s WHERE s.student.studentId = :studentId AND s.task.id = :taskId")
    Optional<CareerSubmissionEntity> findByStudentIdAndTaskId(@Param("studentId") String studentId, @Param("taskId") String taskId);
}
