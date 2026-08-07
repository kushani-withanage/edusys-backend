package com.edusys.repository;

import com.edusys.entity.CareerLevelEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareerLevelRepository extends CrudRepository<CareerLevelEntity, String> {
    Optional<CareerLevelEntity> findByLevelNumber(Integer levelNumber);
    
    @Query("SELECT l FROM CareerLevelEntity l ORDER BY l.levelNumber ASC")
    List<CareerLevelEntity> findAllOrderByLevelNumberAsc();
}
