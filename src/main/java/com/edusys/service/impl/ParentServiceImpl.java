package com.edusys.service.impl;

import com.edusys.entity.ParentEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ParentDTO;
import com.edusys.repository.ParentRepository;
import com.edusys.service.ParentService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public ParentDTO create(ParentDTO parentDTO) {
        if (parentDTO.getParentId() == null || parentDTO.getParentId().trim().isEmpty()) {
            parentDTO.setParentId(idGenerator.generateId(EntityPrefix.PARENT, parentRepository.count()));
        }
        ParentEntity entity = mapper.map(parentDTO, ParentEntity.class);
        ParentEntity saved = parentRepository.save(entity);
        return mapper.map(saved, ParentDTO.class);
    }

    @Override
    public ParentDTO getById(String id) {
        return parentRepository.findById(id)
                .map(entity -> mapper.map(entity, ParentDTO.class))
                .orElse(null);
    }

    @Override
    public List<ParentDTO> getAll() {
        List<ParentDTO> list = new ArrayList<>();
        parentRepository.findAll().forEach(entity -> list.add(mapper.map(entity, ParentDTO.class)));
        return list;
    }

    @Override
    public ParentDTO update(String id, ParentDTO parentDTO) {
        if (!parentRepository.existsById(id)) {
            return null;
        }
        parentDTO.setParentId(id);
        ParentEntity entity = mapper.map(parentDTO, ParentEntity.class);
        ParentEntity updated = parentRepository.save(entity);
        return mapper.map(updated, ParentDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (parentRepository.existsById(id)) {
            parentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
