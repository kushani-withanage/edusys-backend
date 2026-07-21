package com.edusys.service;

import com.edusys.model.dto.CareerSubmissionDTO;

import java.util.List;

public interface CareerSubmissionService {
    CareerSubmissionDTO create(CareerSubmissionDTO dto);
    CareerSubmissionDTO getById(String id);
    List<CareerSubmissionDTO> getAll();
    CareerSubmissionDTO update(String id, CareerSubmissionDTO dto);
    boolean delete(String id);
}
