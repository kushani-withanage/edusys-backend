package com.edusys.service.impl;

import com.edusys.entity.CareerLevelEntity;
import com.edusys.entity.CareerTaskEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerTaskDTO;
import com.edusys.repository.CareerLevelRepository;
import com.edusys.repository.CareerTaskRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.CareerTaskService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CareerTaskServiceImpl implements CareerTaskService {

    @Autowired
    private CareerTaskRepository careerTaskRepository;

    @Autowired
    private CareerLevelRepository careerLevelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CareerTaskDTO create(CareerTaskDTO dto) {
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            dto.setId(idGenerator.generateId(EntityPrefix.CAREER_TASK, careerTaskRepository.count()));
        }
        
        CareerLevelEntity level = careerLevelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Level not found: " + dto.getLevelId()));
        
        UserEntity creator = userRepository.findById(dto.getCreatedBy() != null ? dto.getCreatedBy() : "usr0007")
                .orElseThrow(() -> new IllegalArgumentException("Creator user not found"));

        if (dto.getIsActive() == null) {
            dto.setIsActive(true);
        }
        
        CareerTaskEntity entity = new CareerTaskEntity();
        entity.setId(dto.getId());
        entity.setLevel(level);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setSubmissionType(dto.getSubmissionType());
        entity.setPointsValue(dto.getPointsValue());
        entity.setIsActive(dto.getIsActive());
        entity.setCreator(creator);
        entity.setCreatedAt(LocalDateTime.now());
        
        CareerTaskEntity saved = careerTaskRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public CareerTaskDTO getById(String id) {
        return careerTaskRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<CareerTaskDTO> getAll() {
        List<CareerTaskDTO> list = new ArrayList<>();
        careerTaskRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public List<CareerTaskDTO> getByLevelId(String levelId) {
        List<CareerTaskDTO> list = new ArrayList<>();
        careerTaskRepository.findByLevelId(levelId)
                .forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public List<CareerTaskDTO> getByLevelIdAndIsActive(String levelId, Boolean isActive) {
        List<CareerTaskDTO> list = new ArrayList<>();
        careerTaskRepository.findByLevelIdAndIsActive(levelId, isActive)
                .forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public CareerTaskDTO update(String id, CareerTaskDTO dto) {
        CareerTaskEntity existing = careerTaskRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        CareerLevelEntity level = careerLevelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Level not found: " + dto.getLevelId()));

        existing.setLevel(level);
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setPointsValue(dto.getPointsValue());
        existing.setSubmissionType(dto.getSubmissionType());
        if (dto.getIsActive() != null) {
            existing.setIsActive(dto.getIsActive());
        }

        CareerTaskEntity updated = careerTaskRepository.save(existing);
        return convertToDTO(updated);
    }

    @Override
    public boolean delete(String id) {
        if (careerTaskRepository.existsById(id)) {
            careerTaskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private CareerTaskDTO convertToDTO(CareerTaskEntity entity) {
        CareerTaskDTO dto = mapper.map(entity, CareerTaskDTO.class);
        if (entity.getLevel() != null) {
            dto.setLevelId(entity.getLevel().getId());
            dto.setLevelNumber(entity.getLevel().getLevelNumber());
            dto.setLevelTitle(entity.getLevel().getTitle());
        }
        if (entity.getCreator() != null) {
            dto.setCreatedBy(entity.getCreator().getUserId());
        }
        return dto;
    }
}
