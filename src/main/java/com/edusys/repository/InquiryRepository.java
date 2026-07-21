package com.edusys.repository;

import com.edusys.entity.InquiryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends CrudRepository<InquiryEntity, String> {
}
