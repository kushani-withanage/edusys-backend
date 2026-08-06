package com.edusys.repository;

import com.edusys.entity.BatchEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BatchRepository extends CrudRepository<BatchEntity, String> {
    long countByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate today1, LocalDate today2);
    java.util.Optional<BatchEntity> findByBatchNameIgnoreCase(String batchName);
}
