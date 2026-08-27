package com.edusys.repository;

import com.edusys.entity.CareerTaskEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerTaskRepository extends CrudRepository<CareerTaskEntity, String> {
    @Query("SELECT t FROM CareerTaskEntity t WHERE t.level.id = :levelId")
    List<CareerTaskEntity> findByLevelId(@Param("levelId") String levelId);

    @Query("SELECT t FROM CareerTaskEntity t WHERE t.level.id = :levelId AND t.isActive = :isActive")
    List<CareerTaskEntity> findByLevelIdAndIsActive(@Param("levelId") String levelId, @Param("isActive") Boolean isActive);

    @Query("SELECT t FROM CareerTaskEntity t JOIN t.batches b WHERE b.batchId = :batchId")
    List<CareerTaskEntity> findByBatchId(@Param("batchId") String batchId);
}
