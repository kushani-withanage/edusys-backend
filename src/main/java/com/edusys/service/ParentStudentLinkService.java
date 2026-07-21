package com.edusys.service;

import com.edusys.model.dto.ParentStudentLinkDTO;

import java.util.List;

public interface ParentStudentLinkService {
    ParentStudentLinkDTO create(ParentStudentLinkDTO dto);
    ParentStudentLinkDTO getById(String id);
    List<ParentStudentLinkDTO> getAll();
    ParentStudentLinkDTO update(String id, ParentStudentLinkDTO dto);
    boolean delete(String id);
}
