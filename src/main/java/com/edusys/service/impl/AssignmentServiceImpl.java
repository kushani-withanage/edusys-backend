package com.edusys.service.impl;

import com.edusys.entity.AssignmentEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.AssignmentDTO;
import com.edusys.repository.AssignmentRepository;
import com.edusys.service.AssignmentService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public AssignmentDTO create(AssignmentDTO assignmentDTO) {
        if (assignmentDTO.getAssignmentId() == null || assignmentDTO.getAssignmentId().trim().isEmpty()) {
            assignmentDTO.setAssignmentId(idGenerator.generateId(EntityPrefix.ASSIGNMENT, assignmentRepository.count()));
        }
        
        java.util.Optional<AssignmentEntity> existing = assignmentRepository.findById(assignmentDTO.getAssignmentId());
        AssignmentEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            mapper.map(assignmentDTO, entity);
        } else {
            entity = mapper.map(assignmentDTO, AssignmentEntity.class);
        }
        
        AssignmentEntity saved = assignmentRepository.save(entity);
        return mapper.map(saved, AssignmentDTO.class);
    }

    @Override
    public AssignmentDTO getById(String id) {
        return assignmentRepository.findById(id)
                .map(entity -> mapper.map(entity, AssignmentDTO.class))
                .orElse(null);
    }

    @Override
    public List<AssignmentDTO> getAll() {
        List<AssignmentDTO> list = new ArrayList<>();
        assignmentRepository.findAll().forEach(entity -> list.add(mapper.map(entity, AssignmentDTO.class)));
        return list;
    }

    @Override
    public AssignmentDTO update(String id, AssignmentDTO assignmentDTO) {
        if (!assignmentRepository.existsById(id)) {
            return null;
        }
        assignmentDTO.setAssignmentId(id);
        AssignmentEntity entity = mapper.map(assignmentDTO, AssignmentEntity.class);
        AssignmentEntity updated = assignmentRepository.save(entity);
        return mapper.map(updated, AssignmentDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (assignmentRepository.existsById(id)) {
            assignmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
