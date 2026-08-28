package com.edusys.service.impl;

import com.edusys.entity.AssignmentSubmissionEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.AssignmentSubmissionDTO;
import com.edusys.repository.AssignmentSubmissionRepository;
import com.edusys.service.AssignmentSubmissionService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    @Autowired
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public AssignmentSubmissionDTO create(AssignmentSubmissionDTO dto) {
        if (dto.getSubmissionId() == null || dto.getSubmissionId().trim().isEmpty()) {
            dto.setSubmissionId(idGenerator.generateId(EntityPrefix.ASSIGNMENT_SUBMISSION, assignmentSubmissionRepository.count()));
        }
        if (dto.getSubmitDate() == null) {
            dto.setSubmitDate(java.time.LocalDateTime.now());
        }
        AssignmentSubmissionEntity entity = mapper.map(dto, AssignmentSubmissionEntity.class);
        AssignmentSubmissionEntity saved = assignmentSubmissionRepository.save(entity);
        return mapper.map(saved, AssignmentSubmissionDTO.class);
    }

    @Override
    public AssignmentSubmissionDTO getById(String id) {
        return assignmentSubmissionRepository.findById(id)
                .map(entity -> mapper.map(entity, AssignmentSubmissionDTO.class))
                .orElse(null);
    }

    @Override
    public List<AssignmentSubmissionDTO> getAll() {
        List<AssignmentSubmissionDTO> list = new ArrayList<>();
        assignmentSubmissionRepository.findAll().forEach(entity -> list.add(mapper.map(entity, AssignmentSubmissionDTO.class)));
        return list;
    }

    @Override
    public AssignmentSubmissionDTO update(String id, AssignmentSubmissionDTO dto) {
        if (!assignmentSubmissionRepository.existsById(id)) {
            return null;
        }
        dto.setSubmissionId(id);
        AssignmentSubmissionEntity entity = mapper.map(dto, AssignmentSubmissionEntity.class);
        AssignmentSubmissionEntity updated = assignmentSubmissionRepository.save(entity);
        return mapper.map(updated, AssignmentSubmissionDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (assignmentSubmissionRepository.existsById(id)) {
            assignmentSubmissionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public AssignmentSubmissionDTO getByAssignmentAndStudent(String assignmentId, String studentId) {
        return assignmentSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
                .map(entity -> mapper.map(entity, AssignmentSubmissionDTO.class))
                .orElse(null);
    }

    @Override
    public List<AssignmentSubmissionDTO> getByAssignment(String assignmentId) {
        List<AssignmentSubmissionDTO> list = new ArrayList<>();
        assignmentSubmissionRepository.findByAssignmentId(assignmentId)
                .forEach(entity -> list.add(mapper.map(entity, AssignmentSubmissionDTO.class)));
        return list;
    }

    @Override
    public List<AssignmentSubmissionDTO> getByStudent(String studentId) {
        List<AssignmentSubmissionDTO> list = new ArrayList<>();
        assignmentSubmissionRepository.findByStudentId(studentId)
                .forEach(entity -> list.add(mapper.map(entity, AssignmentSubmissionDTO.class)));
        return list;
    }
}
