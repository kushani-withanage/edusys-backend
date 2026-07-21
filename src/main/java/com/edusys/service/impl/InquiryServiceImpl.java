package com.edusys.service.impl;

import com.edusys.entity.InquiryEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.InquiryDTO;
import com.edusys.repository.InquiryRepository;
import com.edusys.service.InquiryService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public InquiryDTO create(InquiryDTO inquiryDTO) {
        if (inquiryDTO.getInquiryId() == null || inquiryDTO.getInquiryId().trim().isEmpty()) {
            inquiryDTO.setInquiryId(idGenerator.generateId(EntityPrefix.INQUIRY, inquiryRepository.count()));
        }
        InquiryEntity entity = mapper.map(inquiryDTO, InquiryEntity.class);
        InquiryEntity saved = inquiryRepository.save(entity);
        return mapper.map(saved, InquiryDTO.class);
    }

    @Override
    public InquiryDTO getById(String id) {
        return inquiryRepository.findById(id)
                .map(entity -> mapper.map(entity, InquiryDTO.class))
                .orElse(null);
    }

    @Override
    public List<InquiryDTO> getAll() {
        List<InquiryDTO> list = new ArrayList<>();
        inquiryRepository.findAll().forEach(entity -> list.add(mapper.map(entity, InquiryDTO.class)));
        return list;
    }

    @Override
    public InquiryDTO update(String id, InquiryDTO inquiryDTO) {
        if (!inquiryRepository.existsById(id)) {
            return null;
        }
        inquiryDTO.setInquiryId(id);
        InquiryEntity entity = mapper.map(inquiryDTO, InquiryEntity.class);
        InquiryEntity updated = inquiryRepository.save(entity);
        return mapper.map(updated, InquiryDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (inquiryRepository.existsById(id)) {
            inquiryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
