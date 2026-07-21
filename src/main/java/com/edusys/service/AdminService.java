package com.edusys.service;

import com.edusys.model.dto.AdminDTO;

import java.util.List;

public interface AdminService {
    AdminDTO create(AdminDTO adminDTO);
    AdminDTO getById(String id);
    List<AdminDTO> getAll();
    AdminDTO update(String id, AdminDTO adminDTO);
    boolean delete(String id);
}
