package com.edusys.repository;

import com.edusys.entity.CareerStudentTaskStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerStudentTaskStatusRepository extends CrudRepository<CareerStudentTaskStatusEntity, String> {
    Optional<CareerStudentTaskStatusEntity> findByTask_IdAndStudent_StudentId(String taskId, String studentId);
    List<CareerStudentTaskStatusEntity> findByStudent_StudentId(String studentId);
    List<CareerStudentTaskStatusEntity> findByTask_Id(String taskId);
}
