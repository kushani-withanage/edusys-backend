package com.edusys.repository;

import com.edusys.entity.StudentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, String> {
    @Query("SELECT MAX(s.enrollmentDate) FROM StudentEntity s")
    LocalDate findLatestEnrollmentDate();

    long countByEnrollmentDateBetween(LocalDate start, LocalDate end);

    List<StudentEntity> findByCurrentBatchId(String currentBatchId);
}
