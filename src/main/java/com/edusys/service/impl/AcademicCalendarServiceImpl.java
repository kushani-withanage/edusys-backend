package com.edusys.service.impl;

import com.edusys.entity.AcademicCalendarEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.AcademicCalendarDTO;
import com.edusys.repository.AcademicCalendarRepository;
import com.edusys.service.AcademicCalendarService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcademicCalendarServiceImpl implements AcademicCalendarService {

    @Autowired
    private AcademicCalendarRepository academicCalendarRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public AcademicCalendarDTO create(AcademicCalendarDTO academicCalendarDTO) {
        if (academicCalendarDTO.getCalendarId() == null || academicCalendarDTO.getCalendarId().trim().isEmpty()) {
            academicCalendarDTO.setCalendarId(idGenerator.generateId(EntityPrefix.ACADEMIC_CALENDAR, academicCalendarRepository.count()));
        }
        AcademicCalendarEntity entity = mapper.map(academicCalendarDTO, AcademicCalendarEntity.class);
        AcademicCalendarEntity saved = academicCalendarRepository.save(entity);
        return mapper.map(saved, AcademicCalendarDTO.class);
    }

    @Override
    public AcademicCalendarDTO getById(String id) {
        return academicCalendarRepository.findById(id)
                .map(entity -> mapper.map(entity, AcademicCalendarDTO.class))
                .orElse(null);
    }

    @Override
    public List<AcademicCalendarDTO> getAll() {
        List<AcademicCalendarDTO> list = new ArrayList<>();
        academicCalendarRepository.findAll().forEach(entity -> list.add(mapper.map(entity, AcademicCalendarDTO.class)));
        return list;
    }

    @Override
    public AcademicCalendarDTO update(String id, AcademicCalendarDTO academicCalendarDTO) {
        if (!academicCalendarRepository.existsById(id)) {
            return null;
        }
        academicCalendarDTO.setCalendarId(id);
        AcademicCalendarEntity entity = mapper.map(academicCalendarDTO, AcademicCalendarEntity.class);
        AcademicCalendarEntity updated = academicCalendarRepository.save(entity);
        return mapper.map(updated, AcademicCalendarDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (academicCalendarRepository.existsById(id)) {
            academicCalendarRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
