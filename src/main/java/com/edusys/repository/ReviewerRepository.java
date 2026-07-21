package com.edusys.repository;

import com.edusys.entity.ReviewerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewerRepository extends CrudRepository<ReviewerEntity, String> {
}
