package com.edusys.repository;

import com.edusys.entity.ReceiptEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends CrudRepository<ReceiptEntity, String> {
}
