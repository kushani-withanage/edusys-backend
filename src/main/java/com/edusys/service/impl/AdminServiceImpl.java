package com.edusys.service.impl;

import com.edusys.entity.AdminEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.AdminDTO;
import com.edusys.repository.AdminRepository;
import com.edusys.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    private AdminDTO convertToDTO(AdminEntity entity) {
        if (entity == null) return null;
        AdminDTO dto = mapper.map(entity, AdminDTO.class);
        userRepository.findById(entity.getAdminId()).ifPresent(user -> {
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setCreatedAt(user.getCreatedAt());
        });
        return dto;
    }

    @Override
    public AdminDTO create(AdminDTO adminDTO) {
        if (adminDTO.getAdminId() == null || adminDTO.getAdminId().trim().isEmpty()) {
            adminDTO.setAdminId(idGenerator.generateId(EntityPrefix.ADMIN, adminRepository.count()));
        }
        AdminEntity entity = mapper.map(adminDTO, AdminEntity.class);
        AdminEntity saved = adminRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public AdminDTO getById(String id) {
        return adminRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<AdminDTO> getAll() {
        List<AdminDTO> list = new ArrayList<>();
        adminRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public AdminDTO update(String id, AdminDTO adminDTO) {
        if (!adminRepository.existsById(id)) {
            if (!userRepository.existsById(id)) {
                return null;
            }
            AdminEntity newAdmin = new AdminEntity();
            newAdmin.setAdminId(id);
            newAdmin.setDepartment(adminDTO.getDepartment() != null ? adminDTO.getDepartment() : "Management");
            adminRepository.save(newAdmin);
        }
        adminDTO.setAdminId(id);
        AdminEntity entity = mapper.map(adminDTO, AdminEntity.class);
        AdminEntity updated = adminRepository.save(entity);
        
        userRepository.findById(id).ifPresent(user -> {
            if (adminDTO.getFullName() != null) user.setFullName(adminDTO.getFullName());
            if (adminDTO.getEmail() != null) user.setEmail(adminDTO.getEmail());
            if (adminDTO.getPhone() != null) user.setPhone(adminDTO.getPhone());
            if (adminDTO.getStatus() != null) user.setStatus(adminDTO.getStatus());
            userRepository.save(user);
        });
        
        return convertToDTO(updated);
    }

    @Override
    public boolean delete(String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
