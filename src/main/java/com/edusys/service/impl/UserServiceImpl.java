package com.edusys.service.impl;

import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.UserDTO;
import com.edusys.repository.UserRepository;
import com.edusys.service.UserService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public UserDTO create(UserDTO userDTO) {
        if (userDTO.getUserId() == null || userDTO.getUserId().trim().isEmpty()) {
            userDTO.setUserId(idGenerator.generateId(EntityPrefix.USER, userRepository.count()));
        }
        UserEntity entity = mapper.map(userDTO, UserEntity.class);
        UserEntity savedEntity = userRepository.save(entity);
        return mapper.map(savedEntity, UserDTO.class);
    }

    @Override
    public UserDTO getById(String id) {
        return userRepository.findById(id)
                .map(entity -> mapper.map(entity, UserDTO.class))
                .orElse(null);
    }

    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> list = new ArrayList<>();
        userRepository.findAll().forEach(entity -> list.add(mapper.map(entity, UserDTO.class)));
        return list;
    }

    @Override
    public UserDTO update(String id, UserDTO userDTO) {
        if (!userRepository.existsById(id)) {
            return null;
        }
        userDTO.setUserId(id);
        UserEntity entity = mapper.map(userDTO, UserEntity.class);
        UserEntity updatedEntity = userRepository.save(entity);
        return mapper.map(updatedEntity, UserDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
