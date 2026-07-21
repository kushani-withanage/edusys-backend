package com.edusys.service;

import com.edusys.model.dto.BatchDTO;

import java.util.List;

public interface BatchService {
    BatchDTO create(BatchDTO batchDTO);
    BatchDTO getById(String id);
    List<BatchDTO> getAll();
    BatchDTO update(String id, BatchDTO batchDTO);
    boolean delete(String id);
}
