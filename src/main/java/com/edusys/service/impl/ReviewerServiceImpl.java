package com.edusys.service.impl;

import com.edusys.entity.ReviewerEntity;
import com.edusys.enums.EntityPrefix;
import com.edusys.model.dto.ReviewerDTO;
import com.edusys.repository.ReviewerRepository;
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
    private ModelMapper mapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public ReviewerDTO create(ReviewerDTO reviewerDTO) {
        if (reviewerDTO.getReviewerId() == null || reviewerDTO.getReviewerId().trim().isEmpty()) {
            reviewerDTO.setReviewerId(idGenerator.generateId(EntityPrefix.REVIEWER, reviewerRepository.count()));
        }
        ReviewerEntity entity = mapper.map(reviewerDTO, ReviewerEntity.class);
        ReviewerEntity saved = reviewerRepository.save(entity);
        return mapper.map(saved, ReviewerDTO.class);
    }

    @Override
    public ReviewerDTO getById(String id) {
        return reviewerRepository.findById(id)
                .map(entity -> mapper.map(entity, ReviewerDTO.class))
                .orElse(null);
    }

    @Override
    public List<ReviewerDTO> getAll() {
        List<ReviewerDTO> list = new ArrayList<>();
        reviewerRepository.findAll().forEach(entity -> list.add(mapper.map(entity, ReviewerDTO.class)));
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
        return mapper.map(updated, ReviewerDTO.class);
    }

    @Override
    public boolean delete(String id) {
        if (reviewerRepository.existsById(id)) {
            reviewerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
