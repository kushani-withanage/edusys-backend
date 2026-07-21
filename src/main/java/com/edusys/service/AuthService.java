package com.edusys.service;

import com.edusys.model.dto.AuthRequestDTO;
import com.edusys.model.dto.AuthResponseDTO;
import com.edusys.model.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerDTO);
    AuthResponseDTO login(AuthRequestDTO authDTO);
}
