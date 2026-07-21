package com.edusys.repository;

import com.edusys.entity.ParentStudentLinkEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentStudentLinkRepository extends CrudRepository<ParentStudentLinkEntity, String> {
}
