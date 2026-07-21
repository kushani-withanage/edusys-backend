package com.edusys.repository;

import com.edusys.entity.FeeRecordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRecordRepository extends CrudRepository<FeeRecordEntity, String> {
}
