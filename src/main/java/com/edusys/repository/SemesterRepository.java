package com.edusys.repository;

import com.edusys.entity.SemesterEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends CrudRepository<SemesterEntity, String> {
}
