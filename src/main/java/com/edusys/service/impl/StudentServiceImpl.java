package com.edusys.service.impl;

import com.edusys.entity.StudentEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.StudentDTO;
import com.edusys.repository.StudentRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.StudentService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edusys.repository.StudentBatchHistoryRepository;
import com.edusys.entity.StudentBatchHistoryEntity;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentBatchHistoryRepository studentBatchHistoryRepository;

    @Autowired
    private com.edusys.repository.ParentRepository parentRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private com.edusys.repository.BatchRepository batchRepository;

    private StudentDTO convertToDTO(StudentEntity entity) {
        if (entity == null) return null;
        StudentDTO dto = mapper.map(entity, StudentDTO.class);
        userRepository.findById(entity.getStudentId()).ifPresent(user -> {
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setCreatedAt(user.getCreatedAt());
        });
        try {
            String sql = "SELECT u.full_name AS guardian_name, u.email AS guardian_email FROM users u JOIN (SELECT student_id, parent_id FROM student_parent UNION SELECT student_id, parent_id FROM parent_student_links) sp ON u.user_id = sp.parent_id WHERE sp.student_id = ?";
            List<java.util.Map<String, Object>> parentRows = jdbcTemplate.queryForList(sql, entity.getStudentId());
            if (!parentRows.isEmpty()) {
                java.util.Map<String, Object> parentRow = parentRows.get(0);
                String name = (String) parentRow.get("guardian_name");
                if (name == null) name = (String) parentRow.get("GUARDIAN_NAME");
                String email = (String) parentRow.get("guardian_email");
                if (email == null) email = (String) parentRow.get("GUARDIAN_EMAIL");
                
                dto.setGuardianName(name);
                dto.setGuardianEmail(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    @Transactional
    public StudentDTO create(StudentDTO studentDTO) {
        if (studentDTO.getCurrentBatchId() != null && !studentDTO.getCurrentBatchId().trim().isEmpty()) {
            batchRepository.findById(studentDTO.getCurrentBatchId()).ifPresent(batch -> {
                if ("Finished".equalsIgnoreCase(batch.getStatus())) {
                    throw new IllegalArgumentException("Cannot register student into a finished batch: " + batch.getBatchName());
                }
            });
        }
        String studentId = studentDTO.getStudentId();
        if (studentId == null || studentId.trim().isEmpty()) {
            studentId = idGenerator.generateId(EntityPrefix.USER, userRepository.count());
            studentDTO.setStudentId(studentId);
        }

        // Ensure student UserEntity exists in the same transaction
        if (!userRepository.existsById(studentId)) {
            if (userRepository.existsByEmail(studentDTO.getEmail())) {
                throw new IllegalArgumentException("Email is already registered: " + studentDTO.getEmail());
            }
            UserEntity user = new UserEntity();
            user.setUserId(studentId);
            user.setFullName(studentDTO.getFullName());
            user.setEmail(studentDTO.getEmail());
            user.setPhone(studentDTO.getPhone() != null && !studentDTO.getPhone().trim().isEmpty() 
                    ? studentDTO.getPhone() : "+94770000000");
            user.setRole("STUDENT");
            user.setStatus("ACTIVE");
            user.setCreatedAt(java.time.LocalDateTime.now());
            user.setPassword(passwordEncoder.encode("password123")); // Default temporary password
            user.setMustSetPassword(true);
            userRepository.save(user);
        }

        StudentEntity entity = mapper.map(studentDTO, StudentEntity.class);
        if (entity.getRegNo() == null || entity.getRegNo().trim().isEmpty()) {
            entity.setRegNo(generateRegistrationNumber(entity.getCurrentBatchId()));
        }
        StudentEntity saved = studentRepository.save(entity);

        // Open first history row
        if (saved.getCurrentBatchId() != null && !saved.getCurrentBatchId().trim().isEmpty()) {
            StudentBatchHistoryEntity history = StudentBatchHistoryEntity.builder()
                    .id(java.util.UUID.randomUUID().toString())
                    .studentId(saved.getStudentId())
                    .batchId(saved.getCurrentBatchId())
                    .startDate(LocalDate.now())
                    .endDate(null)
                    .build();
            studentBatchHistoryRepository.save(history);
        }

        // Process Guardian Parent link/creation
        if (studentDTO.getGuardianEmail() != null && !studentDTO.getGuardianEmail().trim().isEmpty()) {
            String parentEmail = studentDTO.getGuardianEmail().trim();
            Optional<UserEntity> parentUserOpt = userRepository.findByEmail(parentEmail);
            String parentId;
            if (parentUserOpt.isPresent()) {
                parentId = parentUserOpt.get().getUserId();
            } else {
                parentId = idGenerator.generateId(EntityPrefix.PARENT, userRepository.count());
                UserEntity parentUser = new UserEntity();
                parentUser.setUserId(parentId);
                parentUser.setFullName(studentDTO.getGuardianName());
                parentUser.setEmail(parentEmail);
                parentUser.setRole("PARENT");
                parentUser.setStatus("ACTIVE");
                parentUser.setPhone("+94770000000");
                parentUser.setCreatedAt(java.time.LocalDateTime.now());
                parentUser.setPassword(null);
                parentUser.setMustSetPassword(true);
                userRepository.save(parentUser);

                com.edusys.entity.ParentEntity parentEntity = new com.edusys.entity.ParentEntity();
                parentEntity.setParentId(parentId);
                parentEntity.setOccupation("Guardian");
                parentRepository.save(parentEntity);
            }

            entityManager.flush();
            
            Integer spExistsCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM student_parent WHERE parent_id = ? AND student_id = ?",
                    Integer.class, parentId, saved.getStudentId());
            if (spExistsCount == null || spExistsCount == 0) {
                jdbcTemplate.update("INSERT INTO student_parent (student_id, parent_id) VALUES (?, ?)", 
                        saved.getStudentId(), parentId);
            }

            Integer existsCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM parent_student_links WHERE parent_id = ? AND student_id = ?",
                    Integer.class, parentId, saved.getStudentId());
            if (existsCount == null || existsCount == 0) {
                String linkId = idGenerator.generateId(EntityPrefix.PARENT_STUDENT_LINK, 
                        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM parent_student_links", Long.class));
                jdbcTemplate.update("INSERT INTO parent_student_links (link_id, parent_id, student_id, relationship_type, linked_date) VALUES (?, ?, ?, ?, ?)",
                        linkId, parentId, saved.getStudentId(), "Guardian", java.time.LocalDate.now());
            }
        }

        return convertToDTO(saved);
    }

    @Override
    public StudentDTO getById(String id) {
        return studentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<StudentDTO> getAll() {
        List<StudentDTO> list = new ArrayList<>();
        studentRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public StudentDTO update(String id, StudentDTO studentDTO) {
        if (!studentRepository.existsById(id)) {
            return null;
        }
        if (studentDTO.getCurrentBatchId() != null && !studentDTO.getCurrentBatchId().trim().isEmpty()) {
            batchRepository.findById(studentDTO.getCurrentBatchId()).ifPresent(batch -> {
                studentRepository.findById(id).ifPresent(oldStudent -> {
                    if (!studentDTO.getCurrentBatchId().equalsIgnoreCase(oldStudent.getCurrentBatchId()) && "Finished".equalsIgnoreCase(batch.getStatus())) {
                        throw new IllegalArgumentException("Cannot update student to a finished batch: " + batch.getBatchName());
                    }
                });
            });
        }
        studentDTO.setStudentId(id);
        StudentEntity entity = mapper.map(studentDTO, StudentEntity.class);
        studentRepository.findById(id).ifPresent(oldStudent -> {
            if (entity.getRegNo() == null || entity.getRegNo().trim().isEmpty()) {
                entity.setRegNo(oldStudent.getRegNo());
            }
        });
        if (entity.getRegNo() == null || entity.getRegNo().trim().isEmpty()) {
            entity.setRegNo(generateRegistrationNumber(entity.getCurrentBatchId()));
        }
        StudentEntity updated = studentRepository.save(entity);
        
        userRepository.findById(id).ifPresent(user -> {
            if (studentDTO.getFullName() != null) user.setFullName(studentDTO.getFullName());
            if (studentDTO.getEmail() != null) user.setEmail(studentDTO.getEmail());
            if (studentDTO.getPhone() != null) user.setPhone(studentDTO.getPhone());
            if (studentDTO.getStatus() != null) user.setStatus(studentDTO.getStatus());
            userRepository.save(user);
        });

        // Sync Guardian Parent Link
        if (studentDTO.getGuardianEmail() != null && !studentDTO.getGuardianEmail().trim().isEmpty()) {
            String parentEmail = studentDTO.getGuardianEmail().trim();
            Optional<UserEntity> parentUserOpt = userRepository.findByEmail(parentEmail);
            String parentId;
            if (parentUserOpt.isPresent()) {
                parentId = parentUserOpt.get().getUserId();
            } else {
                parentId = idGenerator.generateId(EntityPrefix.PARENT, userRepository.count());
                UserEntity parentUser = new UserEntity();
                parentUser.setUserId(parentId);
                parentUser.setFullName(studentDTO.getGuardianName());
                parentUser.setEmail(parentEmail);
                parentUser.setRole("PARENT");
                parentUser.setStatus("ACTIVE");
                parentUser.setPhone("+94770000000");
                parentUser.setCreatedAt(java.time.LocalDateTime.now());
                parentUser.setPassword(null);
                parentUser.setMustSetPassword(true);
                userRepository.save(parentUser);

                com.edusys.entity.ParentEntity parentEntity = new com.edusys.entity.ParentEntity();
                parentEntity.setParentId(parentId);
                parentEntity.setOccupation("Guardian");
                parentRepository.save(parentEntity);
            }

            Integer spExistsCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM student_parent WHERE parent_id = ? AND student_id = ?",
                    Integer.class, parentId, id);
            if (spExistsCount == null || spExistsCount == 0) {
                jdbcTemplate.update("INSERT INTO student_parent (student_id, parent_id) VALUES (?, ?)", 
                        id, parentId);
            }

            Integer existsCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM parent_student_links WHERE parent_id = ? AND student_id = ?",
                    Integer.class, parentId, id);
            if (existsCount == null || existsCount == 0) {
                String linkId = idGenerator.generateId(EntityPrefix.PARENT_STUDENT_LINK, 
                        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM parent_student_links", Long.class));
                jdbcTemplate.update("INSERT INTO parent_student_links (link_id, parent_id, student_id, relationship_type, linked_date) VALUES (?, ?, ?, ?, ?)",
                        linkId, parentId, id, "Guardian", java.time.LocalDate.now());
            }
        }
        
        return convertToDTO(updated);
    }

    @Override
    public boolean delete(String id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean transferBatch(String id, String batchId) {
        Optional<StudentEntity> studentOpt = studentRepository.findById(id);
        if (!studentOpt.isPresent()) {
            return false;
        }
        batchRepository.findById(batchId).ifPresent(batch -> {
            if ("Finished".equalsIgnoreCase(batch.getStatus())) {
                throw new IllegalArgumentException("Cannot transfer student to a finished batch: " + batch.getBatchName());
            }
        });
        StudentEntity student = studentOpt.get();
        String oldBatchId = student.getCurrentBatchId();

        if (batchId.equals(oldBatchId)) {
            return true;
        }

        // Close old batch history row if exists
        studentBatchHistoryRepository.findByStudentIdAndEndDateIsNull(id).ifPresent(history -> {
            history.setEndDate(LocalDate.now());
            studentBatchHistoryRepository.save(history);
        });

        // Open new history row
        StudentBatchHistoryEntity newHistory = StudentBatchHistoryEntity.builder()
                .id(java.util.UUID.randomUUID().toString())
                .studentId(id)
                .batchId(batchId)
                .startDate(LocalDate.now())
                .endDate(null)
                .build();
        studentBatchHistoryRepository.save(newHistory);

        // Update current batch ID
        student.setCurrentBatchId(batchId);
        studentRepository.save(student);

        return true;
    }

    private String generateRegistrationNumber(String batchId) {
        String year2 = String.valueOf(java.time.LocalDate.now().getYear() % 100);
        String batchNum = "000";
        int maxSeq = 0;
        String existingYear2 = null;
        String existingBatchNum = null;

        if (batchId != null && !batchId.trim().isEmpty()) {
            Optional<com.edusys.entity.BatchEntity> batchOpt = batchRepository.findById(batchId);
            if (batchOpt.isPresent()) {
                com.edusys.entity.BatchEntity batch = batchOpt.get();
                String batchName = batch.getBatchName();
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(batchName);
                if (m.find()) {
                    batchNum = m.group();
                    if (batchNum.length() > 3) {
                        batchNum = batchNum.substring(batchNum.length() - 3);
                    } else if (batchNum.length() < 3) {
                        batchNum = String.format("%03d", Integer.parseInt(batchNum));
                    }
                }
                if (batch.getStartDate() != null) {
                    year2 = String.format("%02d", batch.getStartDate().getYear() % 100);
                }
            }

            // Find last student number in that batch
            List<StudentEntity> batchStudents = studentRepository.findByCurrentBatchId(batchId);
            for (StudentEntity s : batchStudents) {
                String reg = s.getRegNo();
                if (reg != null && reg.length() == 11 && reg.startsWith("pr")) {
                    try {
                        String yr = reg.substring(2, 4);
                        String btn = reg.substring(4, 7);
                        String last4 = reg.substring(7);
                        int val = Integer.parseInt(last4);
                        if (val > maxSeq) {
                            maxSeq = val;
                            existingYear2 = yr;
                            existingBatchNum = btn;
                        }
                    } catch (Exception e) {
                        // ignore non-numeric sequence
                    }
                }
            }
        }

        if (existingYear2 != null) {
            year2 = existingYear2;
        }
        if (existingBatchNum != null) {
            batchNum = existingBatchNum;
        }

        int nextSeq = maxSeq + 1;
        String serial = String.format("%04d", nextSeq);
        return "pr" + year2 + batchNum + serial;
    }
}
