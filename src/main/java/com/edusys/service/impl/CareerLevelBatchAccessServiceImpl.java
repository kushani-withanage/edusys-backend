package com.edusys.service.impl;

import com.edusys.entity.BatchEntity;
import com.edusys.entity.CareerLevelBatchAccessEntity;
import com.edusys.entity.CareerLevelEntity;
import com.edusys.entity.UserEntity;
import com.edusys.repository.BatchRepository;
import com.edusys.repository.CareerLevelBatchAccessRepository;
import com.edusys.repository.CareerLevelRepository;
import com.edusys.repository.UserRepository;
import com.edusys.service.CareerLevelBatchAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CareerLevelBatchAccessServiceImpl implements CareerLevelBatchAccessService {

    @Autowired
    private CareerLevelBatchAccessRepository accessRepository;

    @Autowired
    private CareerLevelRepository levelRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CareerLevelBatchAccessEntity> getAccessList() {
        List<CareerLevelBatchAccessEntity> list = new ArrayList<>();
        accessRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public List<CareerLevelBatchAccessEntity> getByBatchId(String batchId) {
        return accessRepository.findByBatchId(batchId);
    }

    @Override
    public CareerLevelBatchAccessEntity toggleAccess(String levelId, String batchId, String openedByUserId) {
        CareerLevelEntity level = levelRepository.findById(levelId)
                .orElseThrow(() -> new IllegalArgumentException("Level not found: " + levelId));
        BatchEntity batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId));
        UserEntity user = userRepository.findById(openedByUserId != null ? openedByUserId : "usr0007")
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + openedByUserId));

        CareerLevelBatchAccessEntity entity = accessRepository.findByLevelIdAndBatchId(levelId, batchId).orElse(null);
        if (entity != null) {
            entity.setIsOpen(!entity.getIsOpen());
            entity.setOpenedBy(user);
            entity.setOpenedAt(LocalDateTime.now());
            return accessRepository.save(entity);
        } else {
            entity = CareerLevelBatchAccessEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .level(level)
                    .batch(batch)
                    .isOpen(true)
                    .openedBy(user)
                    .openedAt(LocalDateTime.now())
                    .build();
            return accessRepository.save(entity);
        }
    }
}
