package com.edusys.service.impl;

import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.AuthRequestDTO;
import com.edusys.model.dto.AuthResponseDTO;
import com.edusys.model.dto.RegisterRequestDTO;
import com.edusys.repository.UserRepository;
import com.edusys.security.JwtTokenProvider;
import com.edusys.service.AuthService;
import com.edusys.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already registered: " + registerDTO.getEmail());
        }

        String generatedUserId = idGenerator.generateId(EntityPrefix.USER, userRepository.count());
        String role = (registerDTO.getRole() != null && !registerDTO.getRole().trim().isEmpty())
                ? registerDTO.getRole().toUpperCase()
                : "STUDENT";

        UserEntity user = new UserEntity();
        user.setUserId(generatedUserId);
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setRole(role);
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        // Password manage - explicit hashing and saving
        String rawPassword = registerDTO.getPassword();
        String hashed = passwordEncoder.encode(rawPassword);
        user.setPassword(hashed);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmail(), user.getRole());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO authDTO) {
        UserEntity user = userRepository.findByEmail(authDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmail(), user.getRole());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
