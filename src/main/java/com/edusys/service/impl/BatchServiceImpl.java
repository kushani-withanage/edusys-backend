package com.edusys.service.impl;

import com.edusys.entity.BatchEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.BatchDTO;
import com.edusys.model.dto.CourseDTO;
import com.edusys.repository.BatchRepository;
import com.edusys.repository.CourseRepository;
import com.edusys.repository.EnrollmentRepository;
import com.edusys.service.BatchService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private com.edusys.repository.UserRepository userRepository;

    private BatchDTO convertToDTO(BatchEntity entity) {
        if (entity == null) return null;
        BatchDTO dto = mapper.map(entity, BatchDTO.class);
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students WHERE current_batch_id = ?", Long.class, entity.getBatchId());
        if (count == null) count = 0L;
        dto.setStudentCount(count.intValue());
        if (entity.getCourses() != null) {
            List<CourseDTO> courseDTOs = new ArrayList<>();
            entity.getCourses().forEach(c -> courseDTOs.add(mapper.map(c, CourseDTO.class)));
            dto.setCourses(courseDTOs);
        }
        return dto;
    }

    @Override
    public BatchDTO create(BatchDTO batchDTO) {
        if (batchDTO.getBatchName() != null && !batchDTO.getBatchName().trim().isEmpty()) {
            String trimmedCode = batchDTO.getBatchName().trim();
            if (batchRepository.findByBatchNameIgnoreCase(trimmedCode).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Batch code '" + trimmedCode + "' already exists");
            }
        }

        if (batchDTO.getBatchId() == null || batchDTO.getBatchId().trim().isEmpty()) {
            batchDTO.setBatchId(idGenerator.generateId(EntityPrefix.BATCH, batchRepository.count()));
        }
        if (batchDTO.getStatus() == null || batchDTO.getStatus().trim().isEmpty()) {
            batchDTO.setStatus("Active");
        }

        BatchEntity entity = mapper.map(batchDTO, BatchEntity.class);
        if (batchDTO.getCourses() != null) {
            Set<com.edusys.entity.CourseEntity> courseEntities = new HashSet<>();
            batchDTO.getCourses().forEach(c -> {
                if (c.getCourseId() != null) {
                    courseRepository.findById(c.getCourseId()).ifPresent(courseEntities::add);
                }
            });
            entity.setCourses(courseEntities);
        }
        BatchEntity saved = batchRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public BatchDTO getById(String id) {
        return batchRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public List<BatchDTO> getAll() {
        List<BatchDTO> list = new ArrayList<>();
        batchRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public BatchDTO update(String id, BatchDTO batchDTO) {
        if (!batchRepository.existsById(id)) {
            return null;
        }

        if (batchDTO.getBatchName() != null && !batchDTO.getBatchName().trim().isEmpty()) {
            String trimmedCode = batchDTO.getBatchName().trim();
            if (batchRepository.findByBatchNameIgnoreCaseAndBatchIdNot(trimmedCode, id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Batch code '" + trimmedCode + "' already exists");
            }
        }

        batchDTO.setBatchId(id);
        BatchEntity entity = mapper.map(batchDTO, BatchEntity.class);
        if (batchDTO.getCourses() != null) {
            Set<com.edusys.entity.CourseEntity> courseEntities = new HashSet<>();
            batchDTO.getCourses().forEach(c -> {
                if (c.getCourseId() != null) {
                    courseRepository.findById(c.getCourseId()).ifPresent(courseEntities::add);
                }
            });
            entity.setCourses(courseEntities);
        }
        BatchEntity updated = batchRepository.save(entity);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        if (!batchRepository.existsById(id)) {
            return false;
        }

        // Clean up batch course mappings
        jdbcTemplate.update("DELETE FROM batch_course WHERE batch_id = ?", id);

        // Clean up semesters referencing this batch
        jdbcTemplate.update("DELETE FROM semesters WHERE batch_id = ?", id);

        // Clean up academic calendars referencing this batch
        jdbcTemplate.update("DELETE FROM academic_calendars WHERE batch_id = ?", id);

        // Clean up enrollments referencing this batch
        jdbcTemplate.update("DELETE FROM enrollments WHERE batch_id = ?", id);

        // Clean up inquiries referencing this batch
        jdbcTemplate.update("DELETE FROM inquiries WHERE batch_id = ?", id);

        // Finally, delete the batch
        batchRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean isCodeTaken(String code, String excludeId) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        String trimmed = code.trim();
        if (excludeId != null && !excludeId.trim().isEmpty()) {
            return batchRepository.findByBatchNameIgnoreCaseAndBatchIdNot(trimmed, excludeId.trim()).isPresent();
        }
        return batchRepository.findByBatchNameIgnoreCase(trimmed).isPresent();
    }

    @Override
    @Transactional
    public List<CourseDTO> getCoursesForBatch(String batchId) {
        BatchEntity batch = batchRepository.findById(batchId).orElse(null);
        if (batch == null) {
            return new ArrayList<>();
        }

        Set<String> courseIds = new HashSet<>();

        // 1. From ManyToMany courses relationship
        if (batch.getCourses() != null) {
            batch.getCourses().forEach(c -> courseIds.add(c.getCourseId()));
        }

        // 2. Query from course_access_grants table using batch_name or batch_id (batch_code in grants)
        List<String> grantIds = jdbcTemplate.queryForList(
                "SELECT DISTINCT course_id FROM course_access_grants WHERE LOWER(batch_code) = LOWER(?) OR LOWER(batch_code) = LOWER(?)",
                String.class,
                batch.getBatchName(),
                batch.getBatchId()
        );
        courseIds.addAll(grantIds);

        // 3. Query from enrollments table
        List<String> enrollmentCourseIds = jdbcTemplate.queryForList(
                "SELECT DISTINCT course_id FROM enrollments WHERE batch_id = ?",
                String.class,
                batchId
        );
        courseIds.addAll(enrollmentCourseIds);

        if (courseIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<CourseDTO> dtos = new ArrayList<>();
        courseRepository.findAllById(courseIds).forEach(courseEntity -> {
            dtos.add(mapper.map(courseEntity, CourseDTO.class));
        });
        return dtos;
    }

    @Override
    public List<com.edusys.model.dto.UserDTO> getStudentsInBatch(String batchId) {
        List<String> studentIds = jdbcTemplate.queryForList(
                "SELECT DISTINCT student_id FROM students WHERE current_batch_id = ? " +
                "UNION " +
                "SELECT DISTINCT student_id FROM enrollments WHERE batch_id = ?",
                String.class,
                batchId,
                batchId
        );
        
        List<com.edusys.model.dto.UserDTO> list = new ArrayList<>();
        if (studentIds.isEmpty()) {
            return list;
        }

        userRepository.findAllById(studentIds).forEach(user -> {
            list.add(mapper.map(user, com.edusys.model.dto.UserDTO.class));
        });
        return list;
    }
}
