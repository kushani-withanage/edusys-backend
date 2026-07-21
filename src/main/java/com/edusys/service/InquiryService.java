package com.edusys.service;

import com.edusys.model.dto.InquiryDTO;

import java.util.List;

public interface InquiryService {
    InquiryDTO create(InquiryDTO inquiryDTO);
    InquiryDTO getById(String id);
    List<InquiryDTO> getAll();
    InquiryDTO update(String id, InquiryDTO inquiryDTO);
    boolean delete(String id);
}
