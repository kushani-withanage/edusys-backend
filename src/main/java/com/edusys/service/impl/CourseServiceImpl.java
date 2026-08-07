package com.edusys.service.impl;

import com.edusys.entity.CourseEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CourseDTO;
import com.edusys.repository.CourseRepository;
import com.edusys.repository.UserRepository;
import com.edusys.repository.EnrollmentRepository;
import com.edusys.repository.CourseAccessGrantRepository;
import com.edusys.service.CourseService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseAccessGrantRepository courseAccessGrantRepository;

    @Autowired
    private com.edusys.repository.BatchRepository batchRepository;

    @Override
    public CourseDTO create(CourseDTO courseDTO) {
        if (courseDTO.getCourseId() == null || courseDTO.getCourseId().trim().isEmpty()) {
            courseDTO.setCourseId(idGenerator.generateId(EntityPrefix.COURSE, courseRepository.count()));
        }
        CourseEntity entity = mapper.map(courseDTO, CourseEntity.class);
        CourseEntity saved = courseRepository.save(entity);
        return mapper.map(saved, CourseDTO.class);
    }

    @Override
    public CourseDTO getById(String id) {
        return courseRepository.findById(id)
                .map(entity -> mapper.map(entity, CourseDTO.class))
                .orElse(null);
    }

    @Override
    public List<CourseDTO> getAll() {
        List<CourseDTO> list = new ArrayList<>();
        courseRepository.findAll().forEach(entity -> list.add(mapper.map(entity, CourseDTO.class)));
        return list;
    }

    @Override
    public CourseDTO update(String id, CourseDTO courseDTO) {
        if (!courseRepository.existsById(id)) {
            return null;
        }
        courseDTO.setCourseId(id);
        CourseEntity entity = mapper.map(courseDTO, CourseEntity.class);
        CourseEntity updated = courseRepository.save(entity);
        return mapper.map(updated, CourseDTO.class);
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        if (courseRepository.existsById(id)) {
            jdbcTemplate.update("DELETE FROM course_access_grants WHERE course_id = ?", id);
            jdbcTemplate.update("DELETE FROM batch_course WHERE course_id = ?", id);
            jdbcTemplate.update("DELETE FROM enrollments WHERE course_id = ?", id);
            jdbcTemplate.update("DELETE FROM grades WHERE course_id = ?", id);
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<CourseDTO> getCoursesForUser(String userId) {
        Optional<com.edusys.entity.UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return new ArrayList<>();
        }
        com.edusys.entity.UserEntity user = userOpt.get();
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return getAll();
        }

        Set<String> accessibleCourseIds = new HashSet<>();
        Map<String, String> statusMap = new HashMap<>();
        Map<String, String> batchCodeMap = new HashMap<>();
        Map<String, String> instructorMap = new HashMap<>();

        // 1. Add standard enrollments
        enrollmentRepository.findByStudentId(userId).forEach(e -> {
            accessibleCourseIds.add(e.getCourseId());
            statusMap.put(e.getCourseId().toLowerCase(), e.getStatus() != null ? e.getStatus() : "ongoing");
            Optional<com.edusys.entity.BatchEntity> batchOpt = batchRepository.findById(e.getBatchId());
            if (batchOpt.isPresent()) {
                com.edusys.entity.BatchEntity batch = batchOpt.get();
                batchCodeMap.put(e.getCourseId().toLowerCase(), batch.getBatchName());
            }
        });

        // 2. Add custom grants
        courseAccessGrantRepository.findByUserIdentifierIgnoreCase(user.getEmail()).forEach(g -> {
            accessibleCourseIds.add(g.getCourseId());
            if (!statusMap.containsKey(g.getCourseId().toLowerCase())) {
                statusMap.put(g.getCourseId().toLowerCase(), g.getStatus() != null ? g.getStatus() : "ongoing");
            }
            if (g.getBatchCode() != null) {
                batchCodeMap.put(g.getCourseId().toLowerCase(), g.getBatchCode());
            }
        });

        // 3. Fetch courses
        List<CourseDTO> list = new ArrayList<>();
        if (!accessibleCourseIds.isEmpty()) {
            courseRepository.findAllById(accessibleCourseIds).forEach(entity -> {
                CourseDTO dto = mapper.map(entity, CourseDTO.class);
                dto.setStatus(statusMap.getOrDefault(entity.getCourseId().toLowerCase(), "ongoing"));
                dto.setBatchCode(batchCodeMap.get(entity.getCourseId().toLowerCase()));
                dto.setInstructor(instructorMap.getOrDefault(entity.getCourseId().toLowerCase(), "Academic Faculty"));
                list.add(dto);
            });
        }
        return list;
    }

    @Override
    @Transactional
    public boolean updateCourseStatusForUser(String userId, String courseId, String status) {
        Optional<com.edusys.entity.UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return false;
        }
        com.edusys.entity.UserEntity user = userOpt.get();

        boolean updated = false;

        // 1. Update in enrollments if exists
        List<com.edusys.entity.EnrollmentEntity> enrollments = enrollmentRepository.findByStudentId(userId);
        for (com.edusys.entity.EnrollmentEntity enrollment : enrollments) {
            if (enrollment.getCourseId().equalsIgnoreCase(courseId)) {
                enrollment.setStatus(status);
                enrollmentRepository.save(enrollment);
                updated = true;
            }
        }

        // 2. Update in course_access_grants if exists
        List<com.edusys.entity.CourseAccessGrantEntity> grants = courseAccessGrantRepository.findByUserIdentifierIgnoreCase(user.getEmail());
        for (com.edusys.entity.CourseAccessGrantEntity grant : grants) {
            if (grant.getCourseId().equalsIgnoreCase(courseId)) {
                grant.setStatus(status);
                courseAccessGrantRepository.save(grant);
                updated = true;
            }
        }

        return updated;
    }
}
