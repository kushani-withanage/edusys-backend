package com.edusys.service.impl;

import com.edusys.entity.CareerPointsLedgerEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.CareerPointsLedgerDTO;
import com.edusys.repository.CareerPointsLedgerRepository;
import com.edusys.service.CareerPointsLedgerService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CareerPointsLedgerServiceImpl implements CareerPointsLedgerService {

    @Autowired
    private CareerPointsLedgerRepository careerPointsLedgerRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public CareerPointsLedgerDTO create(CareerPointsLedgerDTO dto) {
        if (dto.getLedgerId() == null || dto.getLedgerId().trim().isEmpty()) {
            dto.setLedgerId(idGenerator.generateId(EntityPrefix.CAREER_POINTS_LEDGER, careerPointsLedgerRepository.count()));
        }
        CareerPointsLedgerEntity entity = mapper.map(dto, CareerPointsLedgerEntity.class);
        CareerPointsLedgerEntity saved = careerPointsLedgerRepository.save(entity);
        return mapper.map(saved, CareerPointsLedgerDTO.class);
    }

    @Override
    public CareerPointsLedgerDTO getById(String id) {
        return careerPointsLedgerRepository.findById(id)
                .map(entity -> mapper.map(entity, CareerPointsLedgerDTO.class))
                .orElse(null);
    }

    @Override
    public List<CareerPointsLedgerDTO> getAll() {
        List<CareerPointsLedgerDTO> list = new ArrayList<>();
        careerPointsLedgerRepository.findAll().forEach(entity -> list.add(mapper.map(entity, CareerPointsLedgerDTO.class)));
        return list;
    }

    @Override
    public CareerPointsLedgerDTO update(String id, CareerPointsLedgerDTO dto) {
        if (!careerPointsLedgerRepository.existsById(id)) {
            return null;
        }
        dto.setLedgerId(id);
        CareerPointsLedgerEntity entity = mapper.map(dto, CareerPointsLedgerEntity.class);
        CareerPointsLedgerEntity updated = careerPointsLedgerRepository.save(entity);
        return mapper.map(updated, CareerPointsLedgerDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (careerPointsLedgerRepository.existsById(id)) {
            careerPointsLedgerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
