package com.edusys.repository;

import com.edusys.entity.AssignmentSubmissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentSubmissionRepository extends CrudRepository<AssignmentSubmissionEntity, String> {
    java.util.Optional<AssignmentSubmissionEntity> findByAssignmentIdAndStudentId(String assignmentId, String studentId);
    java.util.List<AssignmentSubmissionEntity> findByAssignmentId(String assignmentId);
}
