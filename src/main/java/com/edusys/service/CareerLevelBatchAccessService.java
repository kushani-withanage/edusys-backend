package com.edusys.service;

import com.edusys.entity.CareerLevelBatchAccessEntity;
import java.util.List;

public interface CareerLevelBatchAccessService {
    List<CareerLevelBatchAccessEntity> getAccessList();
    List<CareerLevelBatchAccessEntity> getByBatchId(String batchId);
    CareerLevelBatchAccessEntity toggleAccess(String levelId, String batchId, String openedByUserId);
}
