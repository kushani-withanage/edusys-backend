package com.edusys.controller;

import com.edusys.entity.CareerLevelBatchAccessEntity;
import com.edusys.model.dto.CareerTaskDTO;
import com.edusys.service.CareerLevelBatchAccessService;
import com.edusys.service.CareerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/career-tasks")
@CrossOrigin
public class CareerTaskController {

    @Autowired
    private CareerTaskService careerTaskService;

    @Autowired
    private CareerLevelBatchAccessService batchAccessService;

    @PostMapping
    public ResponseEntity<CareerTaskDTO> create(@RequestBody CareerTaskDTO dto) {
        CareerTaskDTO created = careerTaskService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareerTaskDTO> getById(@PathVariable String id) {
        CareerTaskDTO dto = careerTaskService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CareerTaskDTO>> getTasks(
            @RequestParam(required = false) String levelId,
            @RequestParam(required = false) Boolean isActive) {
        if (levelId != null && isActive != null) {
            return ResponseEntity.ok(careerTaskService.getByLevelIdAndIsActive(levelId, isActive));
        } else if (levelId != null) {
            return ResponseEntity.ok(careerTaskService.getByLevelId(levelId));
        }
        return ResponseEntity.ok(careerTaskService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareerTaskDTO> update(@PathVariable String id, @RequestBody CareerTaskDTO dto) {
        CareerTaskDTO updated = careerTaskService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = careerTaskService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- Batch Access Management endpoints ---

    @GetMapping("/batch-access")
    public ResponseEntity<List<CareerLevelBatchAccessEntity>> getBatchAccessList() {
        return ResponseEntity.ok(batchAccessService.getAccessList());
    }

    @PostMapping("/batch-access/toggle")
    public ResponseEntity<?> toggleBatchAccess(
            @RequestParam String levelId,
            @RequestParam String batchId,
            @RequestParam(required = false) String openedBy) {
        try {
            CareerLevelBatchAccessEntity access = batchAccessService.toggleAccess(levelId, batchId, openedBy != null ? openedBy : "usr0007");
            return ResponseEntity.ok(access);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
