package com.edusys.repository;

import com.edusys.entity.QuestionBankEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends CrudRepository<QuestionBankEntity, String> {
}
