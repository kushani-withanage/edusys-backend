package com.edusys.repository;

import com.edusys.entity.AssignmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends CrudRepository<AssignmentEntity, String> {
}
