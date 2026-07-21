package com.edusys.service.impl;

import com.edusys.entity.BatchEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.BatchDTO;
import com.edusys.repository.BatchRepository;
import com.edusys.service.BatchService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public BatchDTO create(BatchDTO batchDTO) {
        if (batchDTO.getBatchId() == null || batchDTO.getBatchId().trim().isEmpty()) {
            batchDTO.setBatchId(idGenerator.generateId(EntityPrefix.BATCH, batchRepository.count()));
        }
        BatchEntity entity = mapper.map(batchDTO, BatchEntity.class);
        BatchEntity saved = batchRepository.save(entity);
        return mapper.map(saved, BatchDTO.class);
    }

    @Override
    public BatchDTO getById(String id) {
        return batchRepository.findById(id)
                .map(entity -> mapper.map(entity, BatchDTO.class))
                .orElse(null);
    }

    @Override
    public List<BatchDTO> getAll() {
        List<BatchDTO> list = new ArrayList<>();
        batchRepository.findAll().forEach(entity -> list.add(mapper.map(entity, BatchDTO.class)));
        return list;
    }

    @Override
    public BatchDTO update(String id, BatchDTO batchDTO) {
        if (!batchRepository.existsById(id)) {
            return null;
        }
        batchDTO.setBatchId(id);
        BatchEntity entity = mapper.map(batchDTO, BatchEntity.class);
        BatchEntity updated = batchRepository.save(entity);
        return mapper.map(updated, BatchDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (batchRepository.existsById(id)) {
            batchRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
