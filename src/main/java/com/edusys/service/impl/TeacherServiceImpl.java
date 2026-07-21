package com.edusys.service.impl;

import com.edusys.entity.TeacherEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.TeacherDTO;
import com.edusys.repository.TeacherRepository;
import com.edusys.service.TeacherService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public TeacherDTO create(TeacherDTO teacherDTO) {
        if (teacherDTO.getTeacherId() == null || teacherDTO.getTeacherId().trim().isEmpty()) {
            teacherDTO.setTeacherId(idGenerator.generateId(EntityPrefix.TEACHER, teacherRepository.count()));
        }
        TeacherEntity entity = mapper.map(teacherDTO, TeacherEntity.class);
        TeacherEntity saved = teacherRepository.save(entity);
        return mapper.map(saved, TeacherDTO.class);
    }

    @Override
    public TeacherDTO getById(String id) {
        return teacherRepository.findById(id)
                .map(entity -> mapper.map(entity, TeacherDTO.class))
                .orElse(null);
    }

    @Override
    public List<TeacherDTO> getAll() {
        List<TeacherDTO> list = new ArrayList<>();
        teacherRepository.findAll().forEach(entity -> list.add(mapper.map(entity, TeacherDTO.class)));
        return list;
    }

    @Override
    public TeacherDTO update(String id, TeacherDTO teacherDTO) {
        if (!teacherRepository.existsById(id)) {
            return null;
        }
        teacherDTO.setTeacherId(id);
        TeacherEntity entity = mapper.map(teacherDTO, TeacherEntity.class);
        TeacherEntity updated = teacherRepository.save(entity);
        return mapper.map(updated, TeacherDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
