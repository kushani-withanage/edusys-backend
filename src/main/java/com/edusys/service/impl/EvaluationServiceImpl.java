package com.edusys.service.impl;

import com.edusys.entity.EvaluationEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.EvaluationDTO;
import com.edusys.repository.EvaluationRepository;
import com.edusys.service.EvaluationService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public EvaluationDTO create(EvaluationDTO dto) {
        if (dto.getEvaluationId() == null || dto.getEvaluationId().trim().isEmpty()) {
            dto.setEvaluationId(idGenerator.generateId(EntityPrefix.EVALUATION, evaluationRepository.count()));
        }
        EvaluationEntity entity = mapper.map(dto, EvaluationEntity.class);
        EvaluationEntity saved = evaluationRepository.save(entity);
        return mapper.map(saved, EvaluationDTO.class);
    }

    @Override
    public EvaluationDTO getById(String id) {
        return evaluationRepository.findById(id)
                .map(entity -> mapper.map(entity, EvaluationDTO.class))
                .orElse(null);
    }

    @Override
    public List<EvaluationDTO> getAll() {
        List<EvaluationDTO> list = new ArrayList<>();
        evaluationRepository.findAll().forEach(entity -> list.add(mapper.map(entity, EvaluationDTO.class)));
        return list;
    }

    @Override
    public EvaluationDTO update(String id, EvaluationDTO dto) {
        if (!evaluationRepository.existsById(id)) {
            return null;
        }
        dto.setEvaluationId(id);
        EvaluationEntity entity = mapper.map(dto, EvaluationEntity.class);
        EvaluationEntity updated = evaluationRepository.save(entity);
        return mapper.map(updated, EvaluationDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (evaluationRepository.existsById(id)) {
            evaluationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
