package com.edusys.service;

import com.edusys.model.dto.FeeRecordDTO;

import java.util.List;

public interface FeeRecordService {
    FeeRecordDTO create(FeeRecordDTO feeRecordDTO);
    FeeRecordDTO getById(String id);
    List<FeeRecordDTO> getAll();
    FeeRecordDTO update(String id, FeeRecordDTO feeRecordDTO);
    boolean delete(String id);
}
