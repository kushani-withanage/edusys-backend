package com.edusys.service.impl;

import com.edusys.entity.SemesterEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.SemesterDTO;
import com.edusys.repository.SemesterRepository;
import com.edusys.service.SemesterService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public SemesterDTO create(SemesterDTO semesterDTO) {
        if (semesterDTO.getSemesterId() == null || semesterDTO.getSemesterId().trim().isEmpty()) {
            semesterDTO.setSemesterId(idGenerator.generateId(EntityPrefix.SEMESTER, semesterRepository.count()));
        }
        SemesterEntity entity = mapper.map(semesterDTO, SemesterEntity.class);
        SemesterEntity saved = semesterRepository.save(entity);
        return mapper.map(saved, SemesterDTO.class);
    }

    @Override
    public SemesterDTO getById(String id) {
        return semesterRepository.findById(id)
                .map(entity -> mapper.map(entity, SemesterDTO.class))
                .orElse(null);
    }

    @Override
    public List<SemesterDTO> getAll() {
        List<SemesterDTO> list = new ArrayList<>();
        semesterRepository.findAll().forEach(entity -> list.add(mapper.map(entity, SemesterDTO.class)));
        return list;
    }

    @Override
    public SemesterDTO update(String id, SemesterDTO semesterDTO) {
        if (!semesterRepository.existsById(id)) {
            return null;
        }
        semesterDTO.setSemesterId(id);
        SemesterEntity entity = mapper.map(semesterDTO, SemesterEntity.class);
        SemesterEntity updated = semesterRepository.save(entity);
        return mapper.map(updated, SemesterDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (semesterRepository.existsById(id)) {
            semesterRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
