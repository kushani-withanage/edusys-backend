package com.edusys.repository;

import com.edusys.entity.AcademicCalendarEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicCalendarRepository extends CrudRepository<AcademicCalendarEntity, String> {
}
