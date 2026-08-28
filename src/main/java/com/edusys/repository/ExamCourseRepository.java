package com.edusys.repository;

import com.edusys.entity.ExamCourseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamCourseRepository extends CrudRepository<ExamCourseEntity, String> {
}
