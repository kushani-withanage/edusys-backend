package com.edusys.service.impl;

import com.edusys.entity.CourseEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CourseDTO;
import com.edusys.repository.CourseRepository;
import com.edusys.service.CourseService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CourseDTO create(CourseDTO courseDTO) {
        if (courseDTO.getCourseId() == null || courseDTO.getCourseId().trim().isEmpty()) {
            courseDTO.setCourseId(idGenerator.generateId(EntityPrefix.COURSE, courseRepository.count()));
        }
        CourseEntity entity = mapper.map(courseDTO, CourseEntity.class);
        CourseEntity saved = courseRepository.save(entity);
        return mapper.map(saved, CourseDTO.class);
    }

    @Override
    public CourseDTO getById(String id) {
        return courseRepository.findById(id)
                .map(entity -> mapper.map(entity, CourseDTO.class))
                .orElse(null);
    }

    @Override
    public List<CourseDTO> getAll() {
        List<CourseDTO> list = new ArrayList<>();
        courseRepository.findAll().forEach(entity -> list.add(mapper.map(entity, CourseDTO.class)));
        return list;
    }

    @Override
    public CourseDTO update(String id, CourseDTO courseDTO) {
        if (!courseRepository.existsById(id)) {
            return null;
        }
        courseDTO.setCourseId(id);
        CourseEntity entity = mapper.map(courseDTO, CourseEntity.class);
        CourseEntity updated = courseRepository.save(entity);
        return mapper.map(updated, CourseDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
