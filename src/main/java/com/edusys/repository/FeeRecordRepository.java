package com.edusys.repository;

import com.edusys.entity.FeeRecordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FeeRecordRepository extends CrudRepository<FeeRecordEntity, String> {
    long countByStatus(String status);

    long countByStatusNotAndDueDateLessThan(String status, LocalDate date);
}
