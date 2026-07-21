package com.edusys.service.impl;

import com.edusys.entity.FeeRecordEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.FeeRecordDTO;
import com.edusys.repository.FeeRecordRepository;
import com.edusys.service.FeeRecordService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeeRecordServiceImpl implements FeeRecordService {

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public FeeRecordDTO create(FeeRecordDTO feeRecordDTO) {
        if (feeRecordDTO.getFeeId() == null || feeRecordDTO.getFeeId().trim().isEmpty()) {
            feeRecordDTO.setFeeId(idGenerator.generateId(EntityPrefix.FEE_RECORD, feeRecordRepository.count()));
        }
        FeeRecordEntity entity = mapper.map(feeRecordDTO, FeeRecordEntity.class);
        FeeRecordEntity saved = feeRecordRepository.save(entity);
        return mapper.map(saved, FeeRecordDTO.class);
    }

    @Override
    public FeeRecordDTO getById(String id) {
        return feeRecordRepository.findById(id)
                .map(entity -> mapper.map(entity, FeeRecordDTO.class))
                .orElse(null);
    }

    @Override
    public List<FeeRecordDTO> getAll() {
        List<FeeRecordDTO> list = new ArrayList<>();
        feeRecordRepository.findAll().forEach(entity -> list.add(mapper.map(entity, FeeRecordDTO.class)));
        return list;
    }

    @Override
    public FeeRecordDTO update(String id, FeeRecordDTO feeRecordDTO) {
        if (!feeRecordRepository.existsById(id)) {
            return null;
        }
        feeRecordDTO.setFeeId(id);
        FeeRecordEntity entity = mapper.map(feeRecordDTO, FeeRecordEntity.class);
        FeeRecordEntity updated = feeRecordRepository.save(entity);
        return mapper.map(updated, FeeRecordDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (feeRecordRepository.existsById(id)) {
            feeRecordRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
