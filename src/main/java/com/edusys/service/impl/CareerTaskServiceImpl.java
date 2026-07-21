package com.edusys.service.impl;

import com.edusys.entity.CareerTaskEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerTaskDTO;
import com.edusys.repository.CareerTaskRepository;
import com.edusys.service.CareerTaskService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CareerTaskServiceImpl implements CareerTaskService {

    @Autowired
    private CareerTaskRepository careerTaskRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CareerTaskDTO create(CareerTaskDTO dto) {
        if (dto.getTaskId() == null || dto.getTaskId().trim().isEmpty()) {
            dto.setTaskId(idGenerator.generateId(EntityPrefix.CAREER_TASK, careerTaskRepository.count()));
        }
        CareerTaskEntity entity = mapper.map(dto, CareerTaskEntity.class);
        CareerTaskEntity saved = careerTaskRepository.save(entity);
        return mapper.map(saved, CareerTaskDTO.class);
    }

    @Override
    public CareerTaskDTO getById(String id) {
        return careerTaskRepository.findById(id)
                .map(entity -> mapper.map(entity, CareerTaskDTO.class))
                .orElse(null);
    }

    @Override
    public List<CareerTaskDTO> getAll() {
        List<CareerTaskDTO> list = new ArrayList<>();
        careerTaskRepository.findAll().forEach(entity -> list.add(mapper.map(entity, CareerTaskDTO.class)));
        return list;
    }

    @Override
    public CareerTaskDTO update(String id, CareerTaskDTO dto) {
        if (!careerTaskRepository.existsById(id)) {
            return null;
        }
        dto.setTaskId(id);
        CareerTaskEntity entity = mapper.map(dto, CareerTaskEntity.class);
        CareerTaskEntity updated = careerTaskRepository.save(entity);
        return mapper.map(updated, CareerTaskDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (careerTaskRepository.existsById(id)) {
            careerTaskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
