package com.edusys.service;

import com.edusys.model.dto.AuthRequestDTO;
import com.edusys.model.dto.AuthResponseDTO;
import com.edusys.model.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerDTO);
    AuthResponseDTO login(AuthRequestDTO authDTO);
    void resetPassword(String email, String newPassword);
    boolean existsByEmail(String email);
    String generateResetOtp(String email);
    void resetPasswordWithOtp(String email, String otp, String newPassword);
    AuthResponseDTO setPassword(String userId, String newPassword);
}
