package com.edusys.service.impl;

import com.edusys.entity.ExamEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ExamDTO;
import com.edusys.repository.ExamRepository;
import com.edusys.service.ExamService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public ExamDTO create(ExamDTO examDTO) {
        if (examDTO.getExamId() == null || examDTO.getExamId().trim().isEmpty()) {
            examDTO.setExamId(idGenerator.generateId(EntityPrefix.EXAM, examRepository.count()));
        }
        ExamEntity entity = mapper.map(examDTO, ExamEntity.class);
        ExamEntity saved = examRepository.save(entity);
        return mapper.map(saved, ExamDTO.class);
    }

    @Override
    public ExamDTO getById(String id) {
        return examRepository.findById(id)
                .map(entity -> mapper.map(entity, ExamDTO.class))
                .orElse(null);
    }

    @Override
    public List<ExamDTO> getAll() {
        List<ExamDTO> list = new ArrayList<>();
        examRepository.findAll().forEach(entity -> list.add(mapper.map(entity, ExamDTO.class)));
        return list;
    }

    @Override
    public ExamDTO update(String id, ExamDTO examDTO) {
        if (!examRepository.existsById(id)) {
            return null;
        }
        examDTO.setExamId(id);
        ExamEntity entity = mapper.map(examDTO, ExamEntity.class);
        ExamEntity updated = examRepository.save(entity);
        return mapper.map(updated, ExamDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (examRepository.existsById(id)) {
            examRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
