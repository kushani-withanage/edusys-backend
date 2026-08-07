package com.edusys.service.impl;

import com.edusys.entity.CareerLevelEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerLevelDTO;
import com.edusys.repository.CareerLevelRepository;
import com.edusys.repository.CareerTaskRepository;
import com.edusys.repository.StudentCareerProgressRepository;
import com.edusys.service.CareerLevelService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CareerLevelServiceImpl implements CareerLevelService {

    @Autowired
    private CareerLevelRepository careerLevelRepository;

    @Autowired
    private StudentCareerProgressRepository progressRepository;

    @Autowired
    private CareerTaskRepository careerTaskRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CareerLevelDTO create(CareerLevelDTO dto) {
        if (dto.getLevelNumber() == null || dto.getLevelNumber() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level number must be a positive integer starting from 1.");
        }

        // Check uniqueness
        if (careerLevelRepository.findByLevelNumber(dto.getLevelNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Level L" + dto.getLevelNumber() + " already exists.");
        }

        // Check sequential order (no gaps)
        if (dto.getLevelNumber() > 1) {
            int previousLevelNum = dto.getLevelNumber() - 1;
            if (careerLevelRepository.findByLevelNumber(previousLevelNum).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create Level L" + dto.getLevelNumber() + " before Level L" + previousLevelNum + " exists.");
            }
        }

        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            dto.setId(idGenerator.generateId(EntityPrefix.CAREER_LEVEL, careerLevelRepository.count()));
        }
        if (dto.getIsActive() == null) {
            dto.setIsActive(true);
        }

        CareerLevelEntity entity = mapper.map(dto, CareerLevelEntity.class);
        CareerLevelEntity saved = careerLevelRepository.save(entity);
        return mapper.map(saved, CareerLevelDTO.class);
    }

    @Override
    public CareerLevelDTO getById(String id) {
        return careerLevelRepository.findById(id)
                .map(entity -> mapper.map(entity, CareerLevelDTO.class))
                .orElse(null);
    }

    @Override
    public List<CareerLevelDTO> getAll() {
        List<CareerLevelDTO> list = new ArrayList<>();
        careerLevelRepository.findAllOrderByLevelNumberAsc()
                .forEach(entity -> list.add(mapper.map(entity, CareerLevelDTO.class)));
        return list;
    }

    @Override
    public CareerLevelDTO update(String id, CareerLevelDTO dto) {
        CareerLevelEntity existing = careerLevelRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        existing.setLevelNumber(dto.getLevelNumber());
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setPointsRequired(dto.getPointsRequired());
        if (dto.getIsActive() != null) {
            existing.setIsActive(dto.getIsActive());
        }

        CareerLevelEntity updated = careerLevelRepository.save(existing);
        return mapper.map(updated, CareerLevelDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (!careerLevelRepository.existsById(id)) {
            return false;
        }
        // Prevent deleting a level that already has student progress or tasks attached
        if (progressRepository.existsByLevelId(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete a career level that has active student progress. Deactivate it instead.");
        }
        if (!careerTaskRepository.findByLevelId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete a career level that has tasks attached. Deactivate it instead.");
        }

        careerLevelRepository.deleteById(id);
        return true;
    }
}
