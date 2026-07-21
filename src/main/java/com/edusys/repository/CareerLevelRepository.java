package com.edusys.repository;

import com.edusys.entity.CareerLevelEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerLevelRepository extends CrudRepository<CareerLevelEntity, String> {
}
