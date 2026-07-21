package com.edusys.service.impl;

import com.edusys.entity.ReceiptEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ReceiptDTO;
import com.edusys.repository.ReceiptRepository;
import com.edusys.service.ReceiptService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public ReceiptDTO create(ReceiptDTO receiptDTO) {
        if (receiptDTO.getReceiptId() == null || receiptDTO.getReceiptId().trim().isEmpty()) {
            receiptDTO.setReceiptId(idGenerator.generateId(EntityPrefix.RECEIPT, receiptRepository.count()));
        }
        ReceiptEntity entity = mapper.map(receiptDTO, ReceiptEntity.class);
        ReceiptEntity saved = receiptRepository.save(entity);
        return mapper.map(saved, ReceiptDTO.class);
    }

    @Override
    public ReceiptDTO getById(String id) {
        return receiptRepository.findById(id)
                .map(entity -> mapper.map(entity, ReceiptDTO.class))
                .orElse(null);
    }

    @Override
    public List<ReceiptDTO> getAll() {
        List<ReceiptDTO> list = new ArrayList<>();
        receiptRepository.findAll().forEach(entity -> list.add(mapper.map(entity, ReceiptDTO.class)));
        return list;
    }

    @Override
    public ReceiptDTO update(String id, ReceiptDTO receiptDTO) {
        if (!receiptRepository.existsById(id)) {
            return null;
        }
        receiptDTO.setReceiptId(id);
        ReceiptEntity entity = mapper.map(receiptDTO, ReceiptEntity.class);
        ReceiptEntity updated = receiptRepository.save(entity);
        return mapper.map(updated, ReceiptDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (receiptRepository.existsById(id)) {
            receiptRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
