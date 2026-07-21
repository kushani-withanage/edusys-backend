package com.edusys.service.impl;

import com.edusys.entity.AdminEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.AdminDTO;
import com.edusys.repository.AdminRepository;
import com.edusys.service.AdminService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public AdminDTO create(AdminDTO adminDTO) {
        if (adminDTO.getAdminId() == null || adminDTO.getAdminId().trim().isEmpty()) {
            adminDTO.setAdminId(idGenerator.generateId(EntityPrefix.ADMIN, adminRepository.count()));
        }
        AdminEntity entity = mapper.map(adminDTO, AdminEntity.class);
        AdminEntity saved = adminRepository.save(entity);
        return mapper.map(saved, AdminDTO.class);
    }

    @Override
    public AdminDTO getById(String id) {
        return adminRepository.findById(id)
                .map(entity -> mapper.map(entity, AdminDTO.class))
                .orElse(null);
    }

    @Override
    public List<AdminDTO> getAll() {
        List<AdminDTO> list = new ArrayList<>();
        adminRepository.findAll().forEach(entity -> list.add(mapper.map(entity, AdminDTO.class)));
        return list;
    }

    @Override
    public AdminDTO update(String id, AdminDTO adminDTO) {
        if (!adminRepository.existsById(id)) {
            return null;
        }
        adminDTO.setAdminId(id);
        AdminEntity entity = mapper.map(adminDTO, AdminEntity.class);
        AdminEntity updated = adminRepository.save(entity);
        return mapper.map(updated, AdminDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
