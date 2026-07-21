package com.edusys.service.impl;

import com.edusys.entity.ExamAttemptEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ExamAttemptDTO;
import com.edusys.repository.ExamAttemptRepository;
import com.edusys.service.ExamAttemptService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamAttemptServiceImpl implements ExamAttemptService {

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public ExamAttemptDTO create(ExamAttemptDTO examAttemptDTO) {
        if (examAttemptDTO.getAttemptId() == null || examAttemptDTO.getAttemptId().trim().isEmpty()) {
            examAttemptDTO.setAttemptId(idGenerator.generateId(EntityPrefix.EXAM_ATTEMPT, examAttemptRepository.count()));
        }
        ExamAttemptEntity entity = mapper.map(examAttemptDTO, ExamAttemptEntity.class);
        ExamAttemptEntity saved = examAttemptRepository.save(entity);
        return mapper.map(saved, ExamAttemptDTO.class);
    }

    @Override
    public ExamAttemptDTO getById(String id) {
        return examAttemptRepository.findById(id)
                .map(entity -> mapper.map(entity, ExamAttemptDTO.class))
                .orElse(null);
    }

    @Override
    public List<ExamAttemptDTO> getAll() {
        List<ExamAttemptDTO> list = new ArrayList<>();
        examAttemptRepository.findAll().forEach(entity -> list.add(mapper.map(entity, ExamAttemptDTO.class)));
        return list;
    }

    @Override
    public ExamAttemptDTO update(String id, ExamAttemptDTO examAttemptDTO) {
        if (!examAttemptRepository.existsById(id)) {
            return null;
        }
        examAttemptDTO.setAttemptId(id);
        ExamAttemptEntity entity = mapper.map(examAttemptDTO, ExamAttemptEntity.class);
        ExamAttemptEntity updated = examAttemptRepository.save(entity);
        return mapper.map(updated, ExamAttemptDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (examAttemptRepository.existsById(id)) {
            examAttemptRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
