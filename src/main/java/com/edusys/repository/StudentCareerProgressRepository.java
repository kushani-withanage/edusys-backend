package com.edusys.repository;

import com.edusys.entity.StudentCareerProgressEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCareerProgressRepository extends CrudRepository<StudentCareerProgressEntity, String> {
    @Query("SELECT COUNT(p) > 0 FROM StudentCareerProgressEntity p WHERE p.currentLevel.id = :levelId")
    boolean existsByLevelId(@Param("levelId") String levelId);
}
