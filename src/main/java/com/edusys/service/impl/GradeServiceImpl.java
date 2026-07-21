package com.edusys.service.impl;

import com.edusys.entity.GradeEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.GradeDTO;
import com.edusys.repository.GradeRepository;
import com.edusys.service.GradeService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public GradeDTO create(GradeDTO gradeDTO) {
        if (gradeDTO.getGradeId() == null || gradeDTO.getGradeId().trim().isEmpty()) {
            gradeDTO.setGradeId(idGenerator.generateId(EntityPrefix.GRADE, gradeRepository.count()));
        }
        GradeEntity entity = mapper.map(gradeDTO, GradeEntity.class);
        GradeEntity saved = gradeRepository.save(entity);
        return mapper.map(saved, GradeDTO.class);
    }

    @Override
    public GradeDTO getById(String id) {
        return gradeRepository.findById(id)
                .map(entity -> mapper.map(entity, GradeDTO.class))
                .orElse(null);
    }

    @Override
    public List<GradeDTO> getAll() {
        List<GradeDTO> list = new ArrayList<>();
        gradeRepository.findAll().forEach(entity -> list.add(mapper.map(entity, GradeDTO.class)));
        return list;
    }

    @Override
    public GradeDTO update(String id, GradeDTO gradeDTO) {
        if (!gradeRepository.existsById(id)) {
            return null;
        }
        gradeDTO.setGradeId(id);
        GradeEntity entity = mapper.map(gradeDTO, GradeEntity.class);
        GradeEntity updated = gradeRepository.save(entity);
        return mapper.map(updated, GradeDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
