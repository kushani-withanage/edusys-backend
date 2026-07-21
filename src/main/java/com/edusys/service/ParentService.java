package com.edusys.service;

import com.edusys.model.dto.ParentDTO;

import java.util.List;

public interface ParentService {
    ParentDTO create(ParentDTO parentDTO);
    ParentDTO getById(String id);
    List<ParentDTO> getAll();
    ParentDTO update(String id, ParentDTO parentDTO);
    boolean delete(String id);
}
