package com.edusys.repository;

import com.edusys.entity.CareerTaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerTaskRepository extends CrudRepository<CareerTaskEntity, String> {
}
