package com.edusys.service.impl;

import com.edusys.entity.ReviewerEntity;
import com.edusys.entity.UserEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ReviewerDTO;
import com.edusys.repository.ReviewerRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.ReviewerService;
import com.edusys.util.IdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewerServiceImpl implements ReviewerService {

    @Autowired
    private ReviewerRepository reviewerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    private ReviewerDTO convertToDTO(ReviewerEntity entity) {
        if (entity == null) return null;
        ReviewerDTO dto = mapper.map(entity, ReviewerDTO.class);
        userRepository.findById(entity.getReviewerId()).ifPresent(user -> {
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setCreatedAt(user.getCreatedAt());
        });
        return dto;
    }

    @Override
    public ReviewerDTO create(ReviewerDTO reviewerDTO) {
        if (reviewerDTO.getReviewerId() == null || reviewerDTO.getReviewerId().trim().isEmpty()) {
            reviewerDTO.setReviewerId(idGenerator.generateId(EntityPrefix.REVIEWER, reviewerRepository.count()));
        }
        ReviewerEntity entity = mapper.map(reviewerDTO, ReviewerEntity.class);
        ReviewerEntity saved = reviewerRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public ReviewerDTO getById(String id) {
        return reviewerRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<ReviewerDTO> getAll() {
        List<ReviewerDTO> list = new ArrayList<>();
        reviewerRepository.findAll().forEach(entity -> list.add(convertToDTO(entity)));
        return list;
    }

    @Override
    public ReviewerDTO update(String id, ReviewerDTO reviewerDTO) {
        if (!reviewerRepository.existsById(id)) {
            return null;
        }
        reviewerDTO.setReviewerId(id);
        ReviewerEntity entity = mapper.map(reviewerDTO, ReviewerEntity.class);
        ReviewerEntity updated = reviewerRepository.save(entity);
        
        userRepository.findById(id).ifPresent(user -> {
            if (reviewerDTO.getFullName() != null) user.setFullName(reviewerDTO.getFullName());
            if (reviewerDTO.getEmail() != null) user.setEmail(reviewerDTO.getEmail());
            if (reviewerDTO.getPhone() != null) user.setPhone(reviewerDTO.getPhone());
            if (reviewerDTO.getStatus() != null) user.setStatus(reviewerDTO.getStatus());
            userRepository.save(user);
        });
        
        return convertToDTO(updated);
    }

    @Override
    public boolean delete(String id) {
        if (reviewerRepository.existsById(id)) {
            reviewerRepository.deleteById(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
