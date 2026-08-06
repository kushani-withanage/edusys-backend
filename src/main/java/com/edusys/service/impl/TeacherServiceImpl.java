package com.edusys.service.impl;

import com.edusys.entity.TeacherEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.TeacherDTO;
import com.edusys.repository.TeacherRepository;
import com.edusys.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    private TeacherDTO convertToDTO(TeacherEntity entity) {
        if (entity == null) return null;
        TeacherDTO dto = mapper.map(entity, TeacherDTO.class);
        userRepository.findById(entity.getTeacherId()).ifPresent(user -> {
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setCreatedAt(user.getCreatedAt());
        });
        return dto;
    }

    @Override
    public TeacherDTO create(TeacherDTO teacherDTO) {
        if (teacherDTO.getTeacherId() == null || teacherDTO.getTeacherId().trim().isEmpty()) {
            teacherDTO.setTeacherId(idGenerator.generateId(EntityPrefix.TEACHER, teacherRepository.count()));
        }
        TeacherEntity entity = mapper.map(teacherDTO, TeacherEntity.class);
        TeacherEntity saved = teacherRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public TeacherDTO getById(String id) {
        return teacherRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<TeacherDTO> getAll() {
        List<TeacherDTO> list = new ArrayList<>();
        teacherRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
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
        
        userRepository.findById(id).ifPresent(user -> {
            if (teacherDTO.getFullName() != null) user.setFullName(teacherDTO.getFullName());
            if (teacherDTO.getEmail() != null) user.setEmail(teacherDTO.getEmail());
            if (teacherDTO.getPhone() != null) user.setPhone(teacherDTO.getPhone());
            if (teacherDTO.getStatus() != null) user.setStatus(teacherDTO.getStatus());
            userRepository.save(user);
        });
        
        return convertToDTO(updated);
    }

    @Override
    public boolean delete(String id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
