package com.edusys.service.impl;

import com.edusys.entity.ParentStudentLinkEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ParentStudentLinkDTO;
import com.edusys.repository.ParentStudentLinkRepository;
import com.edusys.service.ParentStudentLinkService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParentStudentLinkServiceImpl implements ParentStudentLinkService {

    @Autowired
    private ParentStudentLinkRepository parentStudentLinkRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ParentStudentLinkDTO create(ParentStudentLinkDTO dto) {
        if (dto.getLinkId() == null || dto.getLinkId().trim().isEmpty()) {
            dto.setLinkId(idGenerator.generateId(EntityPrefix.PARENT_STUDENT_LINK, parentStudentLinkRepository.count()));
        }
        ParentStudentLinkEntity entity = mapper.map(dto, ParentStudentLinkEntity.class);
        ParentStudentLinkEntity saved = parentStudentLinkRepository.save(entity);

        // Also insert into student_parent
        jdbcTemplate.update("INSERT IGNORE INTO student_parent (student_id, parent_id) VALUES (?, ?)",
                dto.getStudentId(), dto.getParentId());

        return mapper.map(saved, ParentStudentLinkDTO.class);
    }

    @Override
    public ParentStudentLinkDTO getById(String id) {
        return parentStudentLinkRepository.findById(id)
                .map(entity -> mapper.map(entity, ParentStudentLinkDTO.class))
                .orElse(null);
    }

    @Override
    public List<ParentStudentLinkDTO> getAll() {
        List<ParentStudentLinkDTO> list = new ArrayList<>();
        parentStudentLinkRepository.findAll().forEach(entity -> list.add(mapper.map(entity, ParentStudentLinkDTO.class)));
        return list;
    }

    @Override
    public ParentStudentLinkDTO update(String id, ParentStudentLinkDTO dto) {
        if (!parentStudentLinkRepository.existsById(id)) {
            return null;
        }
        dto.setLinkId(id);
        ParentStudentLinkEntity entity = mapper.map(dto, ParentStudentLinkEntity.class);
        ParentStudentLinkEntity updated = parentStudentLinkRepository.save(entity);
        return mapper.map(updated, ParentStudentLinkDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (parentStudentLinkRepository.existsById(id)) {
            ParentStudentLinkEntity entity = parentStudentLinkRepository.findById(id).orElse(null);
            if (entity != null) {
                jdbcTemplate.update("DELETE FROM student_parent WHERE student_id = ? AND parent_id = ?",
                        entity.getStudentId(), entity.getParentId());
            }
            parentStudentLinkRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
