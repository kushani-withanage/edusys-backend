package com.edusys.repository;

import com.edusys.entity.GradeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends CrudRepository<GradeEntity, String> {
}
