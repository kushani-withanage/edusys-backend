package com.edusys.service;

import com.edusys.model.dto.ReviewerDTO;

import java.util.List;

public interface ReviewerService {
    ReviewerDTO create(ReviewerDTO reviewerDTO);
    ReviewerDTO getById(String id);
    List<ReviewerDTO> getAll();
    ReviewerDTO update(String id, ReviewerDTO reviewerDTO);
    boolean delete(String id);
}
