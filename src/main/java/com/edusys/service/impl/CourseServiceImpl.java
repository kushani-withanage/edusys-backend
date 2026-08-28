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

    @Autowired
    private com.edusys.repository.StudentRepository studentRepository;

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
    @Transactional
    public CourseDTO getById(String id) {
        return courseRepository.findById(id)
                .map(entity -> {
                    CourseDTO dto = mapper.map(entity, CourseDTO.class);
                    if (dto.getBatchCode() == null || dto.getBatchCode().trim().isEmpty()) {
                        List<String> batches = new ArrayList<>();
                        try {
                            batchRepository.findAll().forEach(batch -> {
                                if (batch.getCourses() != null) {
                                    boolean hasCourse = batch.getCourses().stream()
                                            .anyMatch(c -> c.getCourseId().equalsIgnoreCase(id));
                                    if (hasCourse) {
                                        batches.add(batch.getBatchName());
                                    }
                                }
                            });
                        } catch (Exception e) {}
                        if (!batches.isEmpty()) {
                            dto.setBatchCode(String.join(", ", batches));
                        }
                    }
                    return dto;
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public List<CourseDTO> getAll() {
        List<CourseDTO> list = new ArrayList<>();
        Map<String, List<String>> courseBatchMap = new HashMap<>();
        try {
            batchRepository.findAll().forEach(batch -> {
                if (batch.getCourses() != null) {
                    batch.getCourses().forEach(course -> {
                        courseBatchMap.computeIfAbsent(course.getCourseId().toLowerCase(), k -> new ArrayList<>())
                                      .add(batch.getBatchName());
                    });
                }
            });
        } catch (Exception e) {}

        courseRepository.findAll().forEach(entity -> {
            CourseDTO dto = mapper.map(entity, CourseDTO.class);
            if (dto.getBatchCode() == null || dto.getBatchCode().trim().isEmpty()) {
                List<String> batches = courseBatchMap.get(entity.getCourseId().toLowerCase());
                if (batches != null && !batches.isEmpty()) {
                    dto.setBatchCode(String.join(", ", batches));
                }
            }
            list.add(dto);
        });
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
    @Transactional
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

        // 1. Add standard enrollments / live batch courses
        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            studentRepository.findById(userId).ifPresent(student -> {
                if (student.getCurrentBatchId() != null) {
                    batchRepository.findById(student.getCurrentBatchId()).ifPresent(batch -> {
                        if (batch.getCourses() != null) {
                            batch.getCourses().forEach(course -> {
                                accessibleCourseIds.add(course.getCourseId());
                                statusMap.put(course.getCourseId().toLowerCase(), "ongoing");
                                batchCodeMap.put(course.getCourseId().toLowerCase(), batch.getBatchId());
                            });
                        }
                    });
                }
            });

            enrollmentRepository.findByStudentId(userId).forEach(e -> {
                accessibleCourseIds.add(e.getCourseId());
                statusMap.put(e.getCourseId().toLowerCase(), e.getStatus() != null ? e.getStatus() : "ongoing");
                Optional<com.edusys.entity.BatchEntity> batchOpt = batchRepository.findById(e.getBatchId());
                if (batchOpt.isPresent()) {
                    batchCodeMap.put(e.getCourseId().toLowerCase(), batchOpt.get().getBatchId());
                }
            });
        }

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
            // Build standard batch course map
            Map<String, List<String>> courseBatchMap = new HashMap<>();
            try {
                batchRepository.findAll().forEach(batch -> {
                    if (batch.getCourses() != null) {
                        batch.getCourses().forEach(course -> {
                            courseBatchMap.computeIfAbsent(course.getCourseId().toLowerCase(), k -> new ArrayList<>())
                                          .add(batch.getBatchName());
                        });
                    }
                });
            } catch (Exception e) {}

            courseRepository.findAllById(accessibleCourseIds).forEach(entity -> {
                CourseDTO dto = mapper.map(entity, CourseDTO.class);
                dto.setStatus(statusMap.getOrDefault(entity.getCourseId().toLowerCase(), "ongoing"));
                
                // Determine batch code from the course module itself
                String batchCodeVal = entity.getBatchCode();
                if (batchCodeVal == null || batchCodeVal.trim().isEmpty()) {
                    List<String> stdBatches = courseBatchMap.get(entity.getCourseId().toLowerCase());
                    if (stdBatches != null && !stdBatches.isEmpty()) {
                        batchCodeVal = String.join(", ", stdBatches);
                    }
                }
                
                // Fallback to enrollment/grant batch if course itself is not mapped to any batch
                if (batchCodeVal == null || batchCodeVal.trim().isEmpty()) {
                    String rawBatchCode = batchCodeMap.get(entity.getCourseId().toLowerCase());
                    if (rawBatchCode != null) {
                        Optional<com.edusys.entity.BatchEntity> batchOpt = batchRepository.findById(rawBatchCode);
                        if (batchOpt.isPresent()) {
                            batchCodeVal = batchOpt.get().getBatchName();
                        } else {
                            batchCodeVal = rawBatchCode;
                        }
                    }
                }
                
                dto.setBatchCode(batchCodeVal);
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

    @Override
    public List<com.edusys.model.dto.BatchDTO> getBatchesForCourse(String courseId) {
        List<com.edusys.model.dto.BatchDTO> result = new ArrayList<>();
        batchRepository.findAll().forEach(batch -> {
            if (batch.getCourses() != null) {
                boolean hasCourse = batch.getCourses().stream()
                        .anyMatch(c -> c.getCourseId().equalsIgnoreCase(courseId));
                if (hasCourse) {
                    com.edusys.model.dto.BatchDTO dto = mapper.map(batch, com.edusys.model.dto.BatchDTO.class);
                    long count = enrollmentRepository.countByBatchId(batch.getBatchId());
                    dto.setStudentCount((int) count);
                    result.add(dto);
                }
            }
        });
        return result;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void grantAccess(String courseId, String batchId, List<String> userIds) {
        com.edusys.entity.CourseEntity course = courseRepository.findById(courseId).orElse(null);
        com.edusys.entity.BatchEntity batch = batchRepository.findById(batchId).orElse(null);
        if (course == null || batch == null) {
            return;
        }

        final boolean isStandard = batch.getCourses() != null && batch.getCourses().stream()
                .anyMatch(c -> c.getCourseId().equalsIgnoreCase(courseId));

        for (String userId : userIds) {
            userRepository.findById(userId).ifPresent(user -> {
                if (isStandard) {
                    return;
                }

                List<com.edusys.entity.CourseAccessGrantEntity> existing =
                        courseAccessGrantRepository.findByUserIdentifierIgnoreCase(user.getEmail());
                boolean alreadyGranted = existing.stream()
                        .anyMatch(g -> g.getCourseId().equalsIgnoreCase(courseId));
                if (alreadyGranted) {
                    return;
                }

                com.edusys.entity.CourseAccessGrantEntity grant = com.edusys.entity.CourseAccessGrantEntity.builder()
                        .id(java.util.UUID.randomUUID().toString())
                        .courseId(courseId)
                        .courseName(course.getCourseName())
                        .batchCode(batchId)
                        .userIdentifier(user.getEmail())
                        .grantedAt(java.time.LocalDate.now())
                        .status("ongoing")
                        .build();
                courseAccessGrantRepository.save(grant);
            });
        }
    }

    @Override
    public List<com.edusys.model.dto.CourseAccessUserDTO> getCourseAccessList(String courseId, String batchId) {
        List<com.edusys.model.dto.CourseAccessUserDTO> result = new ArrayList<>();
        java.util.Set<String> processedUserIds = new java.util.HashSet<>();

        List<com.edusys.entity.BatchEntity> standardBatches = new ArrayList<>();
        if (batchId != null && !batchId.trim().isEmpty()) {
            batchRepository.findById(batchId).ifPresent(batch -> {
                if (batch.getCourses() != null && batch.getCourses().stream()
                        .anyMatch(c -> c.getCourseId().equalsIgnoreCase(courseId))) {
                    standardBatches.add(batch);
                }
            });
        } else {
            batchRepository.findAll().forEach(batch -> {
                if (batch.getCourses() != null && batch.getCourses().stream()
                        .anyMatch(c -> c.getCourseId().equalsIgnoreCase(courseId))) {
                    standardBatches.add(batch);
                }
            });
        }

        for (com.edusys.entity.BatchEntity batch : standardBatches) {
            List<com.edusys.entity.EnrollmentEntity> enrollments = enrollmentRepository.findByBatchId(batch.getBatchId());
            for (com.edusys.entity.EnrollmentEntity enrollment : enrollments) {
                userRepository.findById(enrollment.getStudentId()).ifPresent(user -> {
                    if (processedUserIds.add(user.getUserId())) {
                        result.add(com.edusys.model.dto.CourseAccessUserDTO.builder()
                                .userId(user.getUserId())
                                .fullName(user.getFullName())
                                .email(user.getEmail())
                                .batchId(batch.getBatchId())
                                .batchName(batch.getBatchName())
                                .accessType("Standard")
                                .grantId(null)
                                .grantedAt(enrollment.getEnrollDate())
                                .build());
                    }
                });
            }
        }

        List<com.edusys.entity.CourseAccessGrantEntity> grants = courseAccessGrantRepository.findByCourseId(courseId);
        for (com.edusys.entity.CourseAccessGrantEntity grant : grants) {
            if (batchId != null && !batchId.trim().isEmpty() && !grant.getBatchCode().equalsIgnoreCase(batchId)) {
                continue;
            }

            userRepository.findByEmail(grant.getUserIdentifier()).ifPresent(user -> {
                if (processedUserIds.add(user.getUserId())) {
                    String bId = grant.getBatchCode();
                    String bName = "Unknown Batch";
                    com.edusys.entity.BatchEntity bEntity = batchRepository.findById(bId).orElse(null);
                    if (bEntity != null) {
                        bName = bEntity.getBatchName();
                    }
                    result.add(com.edusys.model.dto.CourseAccessUserDTO.builder()
                            .userId(user.getUserId())
                            .fullName(user.getFullName())
                            .email(user.getEmail())
                            .batchId(bId)
                            .batchName(bName)
                            .accessType("Custom")
                            .grantId(grant.getId())
                            .grantedAt(grant.getGrantedAt())
                            .build());
                }
            });
        }

        return result;
    }
}
