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

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    @Transactional
    public boolean delete(String id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        UserEntity user = userRepository.findById(id).orElse(null);
        String email = user != null ? user.getEmail() : null;

        // 1. Clean up student references
        jdbcTemplate.update("DELETE FROM receipts WHERE fee_id IN (SELECT fee_id FROM fee_records WHERE student_id = ?)", id);
        jdbcTemplate.update("DELETE FROM fee_records WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM enrollments WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM exam_attempts WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM grades WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM assignment_submissions WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM parent_student_links WHERE student_id = ?", id);
        
        // Career system cascading triggers (level progress, ledger, overrides, submissions)
        jdbcTemplate.update("DELETE FROM career_level_overrides WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM student_career_progress WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM career_submissions WHERE student_id = ?", id);
        jdbcTemplate.update("DELETE FROM career_points_ledger WHERE student_id = ?", id);
        
        jdbcTemplate.update("DELETE FROM students WHERE student_id = ?", id);

        // 2. Clean up teacher references
        jdbcTemplate.update("UPDATE assignment_submissions SET graded_by = NULL WHERE graded_by = ?", id);
        jdbcTemplate.update("DELETE FROM exam_attempts WHERE exam_id IN (SELECT id FROM exams WHERE created_by = ?)", id);
        jdbcTemplate.update("DELETE FROM exams WHERE created_by = ?", id);
        jdbcTemplate.update("DELETE FROM questions WHERE created_by = ?", id);
        jdbcTemplate.update("DELETE FROM assignment_submissions WHERE assignment_id IN (SELECT assignment_id FROM assignments WHERE created_by = ?)", id);
        jdbcTemplate.update("DELETE FROM assignments WHERE created_by = ?", id);
        jdbcTemplate.update("DELETE FROM teachers WHERE teacher_id = ?", id);

        // 3. Clean up other user roles
        jdbcTemplate.update("DELETE FROM parent_student_links WHERE parent_id = ?", id);
        jdbcTemplate.update("DELETE FROM parents WHERE parent_id = ?", id);
        jdbcTemplate.update("DELETE FROM evaluations WHERE reviewer_id = ?", id);
        jdbcTemplate.update("DELETE FROM reviewers WHERE reviewer_id = ?", id);
        jdbcTemplate.update("DELETE FROM admins WHERE admin_id = ?", id);

        // 4. Clean up course access grants
        if (email != null) {
            jdbcTemplate.update("DELETE FROM course_access_grants WHERE LOWER(user_identifier) = LOWER(?)", email);
        }

        // 5. Delete the user
        userRepository.deleteById(id);
        return true;
    }
}
