package com.edusys.service;

import com.edusys.model.dto.CareerTaskDTO;

import java.util.List;

public interface CareerTaskService {
    CareerTaskDTO create(CareerTaskDTO dto);
    CareerTaskDTO getById(String id);
    List<CareerTaskDTO> getAll();
    CareerTaskDTO update(String id, CareerTaskDTO dto);
    boolean delete(String id);
}
