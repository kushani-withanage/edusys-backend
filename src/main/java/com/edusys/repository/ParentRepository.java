package com.edusys.repository;

import com.edusys.entity.ParentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends CrudRepository<ParentEntity, String> {
}
