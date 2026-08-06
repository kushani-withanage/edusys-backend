package com.edusys.service.impl;

import com.edusys.entity.BatchEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.BatchDTO;
import com.edusys.repository.BatchRepository;
import com.edusys.repository.EnrollmentRepository;
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
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    private BatchDTO convertToDTO(BatchEntity entity) {
        if (entity == null) return null;
        BatchDTO dto = mapper.map(entity, BatchDTO.class);
        long count = enrollmentRepository.countByBatchId(entity.getBatchId());
        dto.setStudentCount((int) count);
        return dto;
    }

    @Override
    public BatchDTO create(BatchDTO batchDTO) {
        if (batchDTO.getBatchId() == null || batchDTO.getBatchId().trim().isEmpty()) {
            batchDTO.setBatchId(idGenerator.generateId(EntityPrefix.BATCH, batchRepository.count()));
        }
        if (batchDTO.getStatus() == null || batchDTO.getStatus().trim().isEmpty()) {
            batchDTO.setStatus("Active");
        }
        if (batchDTO.getTeacher() == null || batchDTO.getTeacher().trim().isEmpty()) {
            batchDTO.setTeacher("Mr. Kasun Jayasuriya");
        }
        if (batchDTO.getCourseName() == null || batchDTO.getCourseName().trim().isEmpty()) {
            batchDTO.setCourseName("Programming Fundamentals");
        }
        BatchEntity entity = mapper.map(batchDTO, BatchEntity.class);
        BatchEntity saved = batchRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public BatchDTO getById(String id) {
        return batchRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<BatchDTO> getAll() {
        List<BatchDTO> list = new ArrayList<>();
        batchRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
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
        return convertToDTO(updated);
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
