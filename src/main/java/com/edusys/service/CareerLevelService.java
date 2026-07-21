package com.edusys.service;

import com.edusys.model.dto.CareerLevelDTO;

import java.util.List;

public interface CareerLevelService {
    CareerLevelDTO create(CareerLevelDTO dto);
    CareerLevelDTO getById(String id);
    List<CareerLevelDTO> getAll();
    CareerLevelDTO update(String id, CareerLevelDTO dto);
    boolean delete(String id);
}
