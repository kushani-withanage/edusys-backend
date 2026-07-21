package com.edusys.repository;

import com.edusys.entity.CareerPointsLedgerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerPointsLedgerRepository extends CrudRepository<CareerPointsLedgerEntity, String> {
}
