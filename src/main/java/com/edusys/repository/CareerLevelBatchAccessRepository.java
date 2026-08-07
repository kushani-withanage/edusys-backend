package com.edusys.repository;

import com.edusys.entity.CareerLevelBatchAccessEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerLevelBatchAccessRepository extends CrudRepository<CareerLevelBatchAccessEntity, String> {
    
    @Query("SELECT a FROM CareerLevelBatchAccessEntity a WHERE a.level.id = :levelId")
    List<CareerLevelBatchAccessEntity> findByLevelId(@Param("levelId") String levelId);

    @Query("SELECT a FROM CareerLevelBatchAccessEntity a WHERE a.batch.batchId = :batchId")
    List<CareerLevelBatchAccessEntity> findByBatchId(@Param("batchId") String batchId);

    @Query("SELECT a FROM CareerLevelBatchAccessEntity a WHERE a.level.id = :levelId AND a.batch.batchId = :batchId")
    Optional<CareerLevelBatchAccessEntity> findByLevelIdAndBatchId(@Param("levelId") String levelId, @Param("batchId") String batchId);

    @Query("SELECT a FROM CareerLevelBatchAccessEntity a WHERE a.batch.batchId = :batchId AND a.level.id = :levelId AND a.isOpen = true")
    Optional<CareerLevelBatchAccessEntity> findActiveAccess(@Param("batchId") String batchId, @Param("levelId") String levelId);
}
