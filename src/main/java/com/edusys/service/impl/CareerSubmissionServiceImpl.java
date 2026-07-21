package com.edusys.service.impl;

import com.edusys.entity.CareerSubmissionEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerSubmissionDTO;
import com.edusys.repository.CareerSubmissionRepository;
import com.edusys.service.CareerSubmissionService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CareerSubmissionServiceImpl implements CareerSubmissionService {

    @Autowired
    private CareerSubmissionRepository careerSubmissionRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CareerSubmissionDTO create(CareerSubmissionDTO dto) {
        if (dto.getSubmissionId() == null || dto.getSubmissionId().trim().isEmpty()) {
            dto.setSubmissionId(idGenerator.generateId(EntityPrefix.CAREER_SUBMISSION, careerSubmissionRepository.count()));
        }
        CareerSubmissionEntity entity = mapper.map(dto, CareerSubmissionEntity.class);
        CareerSubmissionEntity saved = careerSubmissionRepository.save(entity);
        return mapper.map(saved, CareerSubmissionDTO.class);
    }

    @Override
    public CareerSubmissionDTO getById(String id) {
        return careerSubmissionRepository.findById(id)
                .map(entity -> mapper.map(entity, CareerSubmissionDTO.class))
                .orElse(null);
    }

    @Override
    public List<CareerSubmissionDTO> getAll() {
        List<CareerSubmissionDTO> list = new ArrayList<>();
        careerSubmissionRepository.findAll().forEach(entity -> list.add(mapper.map(entity, CareerSubmissionDTO.class)));
        return list;
    }

    @Override
    public CareerSubmissionDTO update(String id, CareerSubmissionDTO dto) {
        if (!careerSubmissionRepository.existsById(id)) {
            return null;
        }
        dto.setSubmissionId(id);
        CareerSubmissionEntity entity = mapper.map(dto, CareerSubmissionEntity.class);
        CareerSubmissionEntity updated = careerSubmissionRepository.save(entity);
        return mapper.map(updated, CareerSubmissionDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (careerSubmissionRepository.existsById(id)) {
            careerSubmissionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
