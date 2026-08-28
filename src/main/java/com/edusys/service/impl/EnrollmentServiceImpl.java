package com.edusys.service.impl;

import com.edusys.entity.EnrollmentEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.EnrollmentDTO;
import com.edusys.repository.EnrollmentRepository;
import com.edusys.service.EnrollmentService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private com.edusys.repository.BatchRepository batchRepository;

    @Override
    public EnrollmentDTO create(EnrollmentDTO enrollmentDTO) {
        if (enrollmentDTO.getBatchId() != null) {
            batchRepository.findById(enrollmentDTO.getBatchId()).ifPresent(batch -> {
                if ("Finished".equalsIgnoreCase(batch.getStatus())) {
                    throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, 
                        "Cannot enroll students in a finished batch"
                    );
                }
            });
        }
        if (enrollmentDTO.getEnrollmentId() == null || enrollmentDTO.getEnrollmentId().trim().isEmpty()) {
            enrollmentDTO.setEnrollmentId(idGenerator.generateId(EntityPrefix.ENROLLMENT, enrollmentRepository.count()));
        }
        EnrollmentEntity entity = mapper.map(enrollmentDTO, EnrollmentEntity.class);
        EnrollmentEntity saved = enrollmentRepository.save(entity);
        return mapper.map(saved, EnrollmentDTO.class);
    }

    @Override
    public EnrollmentDTO getById(String id) {
        return enrollmentRepository.findById(id)
                .map(entity -> mapper.map(entity, EnrollmentDTO.class))
                .orElse(null);
    }

    @Override
    public List<EnrollmentDTO> getAll() {
        List<EnrollmentDTO> list = new ArrayList<>();
        enrollmentRepository.findAll().forEach(entity -> list.add(mapper.map(entity, EnrollmentDTO.class)));
        return list;
    }

    @Override
    public EnrollmentDTO update(String id, EnrollmentDTO enrollmentDTO) {
        if (!enrollmentRepository.existsById(id)) {
            return null;
        }
        enrollmentDTO.setEnrollmentId(id);
        EnrollmentEntity entity = mapper.map(enrollmentDTO, EnrollmentEntity.class);
        EnrollmentEntity updated = enrollmentRepository.save(entity);
        return mapper.map(updated, EnrollmentDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (enrollmentRepository.existsById(id)) {
            enrollmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
