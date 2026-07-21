package com.edusys.service;

import com.edusys.model.dto.CareerPointsLedgerDTO;

import java.util.List;

public interface CareerPointsLedgerService {
    CareerPointsLedgerDTO create(CareerPointsLedgerDTO dto);
    CareerPointsLedgerDTO getById(String id);
    List<CareerPointsLedgerDTO> getAll();
    CareerPointsLedgerDTO update(String id, CareerPointsLedgerDTO dto);
    boolean delete(String id);
}
