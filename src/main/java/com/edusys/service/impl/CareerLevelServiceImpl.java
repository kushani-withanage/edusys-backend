package com.edusys.service.impl;

import com.edusys.entity.CareerLevelEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerLevelDTO;
import com.edusys.repository.CareerLevelRepository;
import com.edusys.service.CareerLevelService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CareerLevelServiceImpl implements CareerLevelService {

    @Autowired
    private CareerLevelRepository careerLevelRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CareerLevelDTO create(CareerLevelDTO dto) {
        if (dto.getLevelId() == null || dto.getLevelId().trim().isEmpty()) {
            dto.setLevelId(idGenerator.generateId(EntityPrefix.CAREER_LEVEL, careerLevelRepository.count()));
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
        careerLevelRepository.findAll().forEach(entity -> list.add(mapper.map(entity, CareerLevelDTO.class)));
        return list;
    }

    @Override
    public CareerLevelDTO update(String id, CareerLevelDTO dto) {
        if (!careerLevelRepository.existsById(id)) {
            return null;
        }
        dto.setLevelId(id);
        CareerLevelEntity entity = mapper.map(dto, CareerLevelEntity.class);
        CareerLevelEntity updated = careerLevelRepository.save(entity);
        return mapper.map(updated, CareerLevelDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (careerLevelRepository.existsById(id)) {
            careerLevelRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
