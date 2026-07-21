package com.edusys.repository;

import com.edusys.entity.AdminEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<AdminEntity, String> {
}
