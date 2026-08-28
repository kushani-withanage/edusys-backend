package com.edusys.service.impl;

import com.edusys.entity.CourseAccessGrantEntity;
import com.edusys.model.dto.CourseAccessGrantDTO;
import com.edusys.repository.CourseAccessGrantRepository;
import com.edusys.service.CourseAccessGrantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseAccessGrantServiceImpl implements CourseAccessGrantService {

    @Autowired
    private CourseAccessGrantRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private com.edusys.repository.BatchRepository batchRepository;

    @Override
    public CourseAccessGrantDTO create(CourseAccessGrantDTO dto) {
        if (dto.getBatchCode() != null) {
            batchRepository.findById(dto.getBatchCode()).ifPresent(batch -> {
                if ("Finished".equalsIgnoreCase(batch.getStatus())) {
                    throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, 
                        "Cannot grant course access for a finished batch"
                    );
                }
            });
        }
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            dto.setId(UUID.randomUUID().toString());
        }
        if (dto.getGrantedAt() == null) {
            dto.setGrantedAt(LocalDate.now());
        }
        CourseAccessGrantEntity entity = mapper.map(dto, CourseAccessGrantEntity.class);
        if (entity.getStatus() == null || entity.getStatus().trim().isEmpty()) {
            entity.setStatus("ongoing");
        }
        CourseAccessGrantEntity saved = repository.save(entity);
        return mapper.map(saved, CourseAccessGrantDTO.class);
    }

    @Override
    public CourseAccessGrantDTO getById(String id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, CourseAccessGrantDTO.class))
                .orElse(null);
    }

    @Override
    public List<CourseAccessGrantDTO> getAll() {
        List<CourseAccessGrantDTO> list = new ArrayList<>();
        repository.findAll().forEach(entity -> list.add(mapper.map(entity, CourseAccessGrantDTO.class)));
        return list;
    }

    @Override
    public List<CourseAccessGrantDTO> getByUserIdentifier(String email) {
        return repository.findByUserIdentifierIgnoreCase(email).stream()
                .map(entity -> mapper.map(entity, CourseAccessGrantDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
