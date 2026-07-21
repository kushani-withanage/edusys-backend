package com.edusys.repository;

import com.edusys.entity.BatchEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends CrudRepository<BatchEntity, String> {
}
