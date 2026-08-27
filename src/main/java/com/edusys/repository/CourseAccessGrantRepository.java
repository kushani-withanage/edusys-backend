package com.edusys.repository;

import com.edusys.entity.CourseAccessGrantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseAccessGrantRepository extends CrudRepository<CourseAccessGrantEntity, String> {
    List<CourseAccessGrantEntity> findByUserIdentifierIgnoreCase(String userIdentifier);
    List<CourseAccessGrantEntity> findByCourseId(String courseId);
}
