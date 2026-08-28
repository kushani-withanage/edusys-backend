package com.edusys.service.impl;

import com.edusys.entity.ExamCourseEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ExamCourseDTO;
import com.edusys.repository.ExamCourseRepository;
import com.edusys.service.ExamCourseService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamCourseServiceImpl implements ExamCourseService {

    @Autowired
    private ExamCourseRepository examCourseRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public ExamCourseDTO create(ExamCourseDTO examCourseDTO) {
        if (examCourseDTO.getCourseId() == null || examCourseDTO.getCourseId().trim().isEmpty()) {
            examCourseDTO.setCourseId(idGenerator.generateId(EntityPrefix.EXAM_COURSE, examCourseRepository.count()));
        }
        ExamCourseEntity entity = mapper.map(examCourseDTO, ExamCourseEntity.class);
        ExamCourseEntity saved = examCourseRepository.save(entity);
        return mapper.map(saved, ExamCourseDTO.class);
    }

    @Override
    public ExamCourseDTO getById(String id) {
        return examCourseRepository.findById(id)
                .map(entity -> mapper.map(entity, ExamCourseDTO.class))
                .orElse(null);
    }

    @Override
    public List<ExamCourseDTO> getAll() {
        List<ExamCourseDTO> list = new ArrayList<>();
        examCourseRepository.findAll().forEach(entity -> list.add(mapper.map(entity, ExamCourseDTO.class)));
        return list;
    }

    @Override
    public ExamCourseDTO update(String id, ExamCourseDTO examCourseDTO) {
        if (!examCourseRepository.existsById(id)) {
            return null;
        }
        examCourseDTO.setCourseId(id);
        ExamCourseEntity entity = mapper.map(examCourseDTO, ExamCourseEntity.class);
        ExamCourseEntity updated = examCourseRepository.save(entity);
        return mapper.map(updated, ExamCourseDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (examCourseRepository.existsById(id)) {
            examCourseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
