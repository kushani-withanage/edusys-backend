package com.edusys.service.impl;

import com.edusys.entity.BatchEntity;
import com.edusys.entity.CareerLevelEntity;
import com.edusys.entity.CareerTaskEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerTaskDTO;
import com.edusys.repository.BatchRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
    private BatchRepository batchRepository;

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
        
        String creatorId = dto.getCreatedBy();
        if (creatorId == null || creatorId.trim().isEmpty()) {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && !auth.getName().equalsIgnoreCase("anonymous")) {
                creatorId = auth.getName();
            } else {
                creatorId = "usr0001";
            }
        }
        
        UserEntity creator = userRepository.findById(creatorId)
                .or(() -> userRepository.findById("usr0001"))
                .or(() -> userRepository.findAll().iterator().hasNext() 
                    ? Optional.of(userRepository.findAll().iterator().next()) 
                    : Optional.empty())
                .orElseThrow(() -> new IllegalArgumentException("Creator user not found"));

        if (dto.getIsActive() == null) {
            dto.setIsActive(true);
        }
        
        List<BatchEntity> batches = new ArrayList<>();
        if (dto.getBatchIds() != null) {
            for (String batchId : dto.getBatchIds()) {
                batchRepository.findById(batchId).ifPresent(batches::add);
            }
        }
        
        CareerTaskEntity entity = new CareerTaskEntity();
        entity.setId(dto.getId());
        entity.setLevel(level);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPointsValue(dto.getPointsValue());
        entity.setIsActive(dto.getIsActive());
        entity.setCreator(creator);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setBatches(batches);
        
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
        if (dto.getIsActive() != null) {
            existing.setIsActive(dto.getIsActive());
        }

        List<BatchEntity> batches = new ArrayList<>();
        if (dto.getBatchIds() != null) {
            for (String batchId : dto.getBatchIds()) {
                batchRepository.findById(batchId).ifPresent(batches::add);
            }
        }
        existing.setBatches(batches);

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
        if (entity.getBatches() != null) {
            dto.setBatchIds(entity.getBatches().stream().map(BatchEntity::getBatchId).collect(Collectors.toList()));
        }
        return dto;
    }
}
