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

    private final java.util.concurrent.ConcurrentHashMap<String, String> resetOtpMap = new java.util.concurrent.ConcurrentHashMap<>();

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

        boolean isNoPasswordParent = "PARENT".equalsIgnoreCase(user.getRole()) && user.getPassword() == null;

        if (!isNoPasswordParent) {
            if (user.getPassword() == null || !passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid email or password.");
            }
        }

        String token;
        boolean mustSet = Boolean.TRUE.equals(user.getMustSetPassword()) || isNoPasswordParent;
        if (mustSet) {
            token = jwtTokenProvider.generateLimitedToken(user.getUserId(), user.getEmail(), user.getRole());
        } else {
            token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmail(), user.getRole());
        }

        if (user.getFirstLogin() == null) {
            user.setFirstLogin(LocalDateTime.now());
        }
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .mustSetPassword(mustSet)
                .build();
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email address not found."));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public String generateResetOtp(String email) {
        if (!existsByEmail(email)) {
            throw new IllegalArgumentException("Email address not found.");
        }
        String otp = String.format("%06d", new java.util.Random().nextInt(999999));
        resetOtpMap.put(email, otp);
        System.out.println("====== RESET PASSWORD OTP FOR " + email + " IS: " + otp + " ======");
        return otp;
    }

    @Override
    public void resetPasswordWithOtp(String email, String otp, String newPassword) {
        String savedOtp = resetOtpMap.get(email);
        if (savedOtp == null || !savedOtp.equals(otp)) {
            throw new IllegalArgumentException("Invalid or expired verification code.");
        }
        resetPassword(email, newPassword);
        resetOtpMap.remove(email);
    }

    @Override
    public AuthResponseDTO setPassword(String userId, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustSetPassword(false);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmail(), user.getRole());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .mustSetPassword(false)
                .build();
    }
}
