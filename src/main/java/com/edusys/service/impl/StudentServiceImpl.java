package com.edusys.service.impl;

import com.edusys.entity.StudentEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.StudentDTO;
import com.edusys.repository.StudentRepository;
import com.edusys.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    private StudentDTO convertToDTO(StudentEntity entity) {
        if (entity == null) return null;
        StudentDTO dto = mapper.map(entity, StudentDTO.class);
        userRepository.findById(entity.getStudentId()).ifPresent(user -> {
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setCreatedAt(user.getCreatedAt());
        });
        return dto;
    }

    @Override
    public StudentDTO create(StudentDTO studentDTO) {
        if (studentDTO.getStudentId() == null || studentDTO.getStudentId().trim().isEmpty()) {
            studentDTO.setStudentId(idGenerator.generateId(EntityPrefix.STUDENT, studentRepository.count()));
        }
        StudentEntity entity = mapper.map(studentDTO, StudentEntity.class);
        StudentEntity saved = studentRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public StudentDTO getById(String id) {
        return studentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<StudentDTO> getAll() {
        List<StudentDTO> list = new ArrayList<>();
        studentRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
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
        
        userRepository.findById(id).ifPresent(user -> {
            if (studentDTO.getFullName() != null) user.setFullName(studentDTO.getFullName());
            if (studentDTO.getEmail() != null) user.setEmail(studentDTO.getEmail());
            if (studentDTO.getPhone() != null) user.setPhone(studentDTO.getPhone());
            if (studentDTO.getStatus() != null) user.setStatus(studentDTO.getStatus());
            userRepository.save(user);
        });
        
        return convertToDTO(updated);
    }

    @Override
    public boolean delete(String id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
