package com.edusys.service;

import com.edusys.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(UserDTO userDTO);
    UserDTO getById(String id);
    List<UserDTO> getAll();
    UserDTO update(String id, UserDTO userDTO);
    boolean delete(String id);
}
