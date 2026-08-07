package com.edusys.service.impl;

import com.edusys.entity.CareerLevelBatchAccessEntity;
import com.edusys.entity.CareerSubmissionEntity;
import com.edusys.entity.CareerTaskEntity;
import com.edusys.entity.StudentEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerSubmissionDTO;
import com.edusys.repository.CareerLevelBatchAccessRepository;
import com.edusys.repository.CareerSubmissionRepository;
import com.edusys.repository.CareerTaskRepository;
import com.edusys.repository.EnrollmentRepository;
import com.edusys.repository.StudentRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.CareerSubmissionService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CareerSubmissionServiceImpl implements CareerSubmissionService {

    @Autowired
    private CareerSubmissionRepository submissionRepository;

    @Autowired
    private CareerTaskRepository taskRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CareerLevelBatchAccessRepository accessRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CareerSubmissionDTO getById(String id) {
        return submissionRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<CareerSubmissionDTO> getAllSubmissions() {
        List<CareerSubmissionDTO> list = new ArrayList<>();
        submissionRepository.findAllOrderBySubmittedAtDesc()
                .forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public List<CareerSubmissionDTO> getStudentSubmissions(String studentId) {
        List<CareerSubmissionDTO> list = new ArrayList<>();
        submissionRepository.findByStudentId(studentId)
                .forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public List<CareerSubmissionDTO> getPendingSubmissions() {
        List<CareerSubmissionDTO> list = new ArrayList<>();
        submissionRepository.findByStatus("PENDING")
                .forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public List<CareerSubmissionDTO> getSubmissionsByStatus(String status) {
        List<CareerSubmissionDTO> list = new ArrayList<>();
        submissionRepository.findByStatus(status)
                .forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public CareerSubmissionDTO createSubmission(String studentId, String taskId, CareerSubmissionDTO dto) {
        CareerTaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found: " + taskId));

        if (!task.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This career task is currently inactive.");
        }

        // 1. Get student batch ID from enrollment
        String batchId = enrollmentRepository.findByStudentId(studentId).stream()
                .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()) || e.getStatus() == null)
                .map(com.edusys.entity.EnrollmentEntity::getBatchId)
                .findFirst()
                .orElse(null);

        if (batchId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Student is not currently enrolled in any active batch.");
        }

        // 2. Check if level batch access is open
        boolean hasAccess = accessRepository.findActiveAccess(batchId, task.getLevel().getId()).isPresent();
        if (!hasAccess) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This level is not open for your batch yet.");
        }

        // 3. Enforce maximum of one active (PENDING or REVISION_REQUESTED) submission per task
        if (submissionRepository.hasActiveSubmission(studentId, taskId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already have an active submission (PENDING or REVISION_REQUESTED) open for this task.");
        }

        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId));

        String id = idGenerator.generateId(EntityPrefix.CAREER_SUBMISSION, submissionRepository.count());

        CareerSubmissionEntity entity = CareerSubmissionEntity.builder()
                .id(id)
                .task(task)
                .student(student)
                .submissionType(dto.getSubmissionType())
                .submissionUrl(dto.getSubmissionUrl())
                .filePath(dto.getFilePath())
                .status("PENDING")
                .submittedAt(LocalDateTime.now())
                .build();

        CareerSubmissionEntity saved = submissionRepository.save(entity);
        return convertToDTO(saved);
    }

    private CareerSubmissionDTO convertToDTO(CareerSubmissionEntity entity) {
        String studentName = userRepository.findById(entity.getStudent().getStudentId())
                .map(UserEntity::getFullName)
                .orElse("Unknown Student");

        CareerSubmissionDTO dto = CareerSubmissionDTO.builder()
                .id(entity.getId())
                .taskId(entity.getTask().getId())
                .taskTitle(entity.getTask().getTitle())
                .taskPointsValue(entity.getTask().getPointsValue())
                .studentId(entity.getStudent().getStudentId())
                .studentName(studentName)
                .submissionType(entity.getSubmissionType())
                .submissionUrl(entity.getSubmissionUrl())
                .filePath(entity.getFilePath())
                .status(entity.getStatus())
                .pointsAwarded(entity.getPointsAwarded())
                .reviewerId(entity.getReviewer() != null ? entity.getReviewer().getUserId() : null)
                .reviewerComment(entity.getReviewerComment())
                .submittedAt(entity.getSubmittedAt())
                .reviewedAt(entity.getReviewedAt())
                .build();

        return dto;
    }
}
