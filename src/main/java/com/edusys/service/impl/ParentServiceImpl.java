package com.edusys.service.impl;

import com.edusys.entity.ParentEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ParentDTO;
import com.edusys.repository.ParentRepository;
import com.edusys.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    private ParentDTO convertToDTO(ParentEntity entity) {
        if (entity == null) return null;
        ParentDTO dto = mapper.map(entity, ParentDTO.class);
        userRepository.findById(entity.getParentId()).ifPresent(user -> {
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setCreatedAt(user.getCreatedAt());
        });
        return dto;
    }

    @Override
    public ParentDTO create(ParentDTO parentDTO) {
        if (parentDTO.getParentId() == null || parentDTO.getParentId().trim().isEmpty()) {
            parentDTO.setParentId(idGenerator.generateId(EntityPrefix.PARENT, parentRepository.count()));
        }
        ParentEntity entity = mapper.map(parentDTO, ParentEntity.class);
        ParentEntity saved = parentRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public ParentDTO getById(String id) {
        return parentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<ParentDTO> getAll() {
        List<ParentDTO> list = new ArrayList<>();
        parentRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
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
        
        userRepository.findById(id).ifPresent(user -> {
            if (parentDTO.getFullName() != null) user.setFullName(parentDTO.getFullName());
            if (parentDTO.getEmail() != null) user.setEmail(parentDTO.getEmail());
            if (parentDTO.getPhone() != null) user.setPhone(parentDTO.getPhone());
            if (parentDTO.getStatus() != null) user.setStatus(parentDTO.getStatus());
            userRepository.save(user);
        });
        
        return convertToDTO(updated);
    }

    @Override
    public boolean delete(String id) {
        if (parentRepository.existsById(id)) {
            parentRepository.deleteById(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
