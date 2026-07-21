package com.edusys.service.impl;

import com.edusys.entity.StudentEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.StudentDTO;
import com.edusys.repository.StudentRepository;
import com.edusys.service.StudentService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public StudentDTO create(StudentDTO studentDTO) {
        if (studentDTO.getStudentId() == null || studentDTO.getStudentId().trim().isEmpty()) {
            studentDTO.setStudentId(idGenerator.generateId(EntityPrefix.STUDENT, studentRepository.count()));
        }
        StudentEntity entity = mapper.map(studentDTO, StudentEntity.class);
        StudentEntity saved = studentRepository.save(entity);
        return mapper.map(saved, StudentDTO.class);
    }

    @Override
    public StudentDTO getById(String id) {
        return studentRepository.findById(id)
                .map(entity -> mapper.map(entity, StudentDTO.class))
                .orElse(null);
    }

    @Override
    public List<StudentDTO> getAll() {
        List<StudentDTO> list = new ArrayList<>();
        studentRepository.findAll().forEach(entity -> list.add(mapper.map(entity, StudentDTO.class)));
        return list;
    }

    @Override
    public StudentDTO update(String id, StudentDTO studentDTO) {
        if (!studentRepository.existsById(id)) {
            return null;
        }
        studentDTO.setStudentId(id);
        StudentEntity entity = mapper.map(studentDTO, StudentEntity.class);
        StudentEntity updated = studentRepository.save(entity);
        return mapper.map(updated, StudentDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
