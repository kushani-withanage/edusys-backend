package com.edusys.controller;

import com.edusys.model.dto.CareerTaskDTO;
import com.edusys.service.CareerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/career-tasks")
@CrossOrigin
public class CareerTaskController {

    @Autowired
    private CareerTaskService careerTaskService;

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
    public ResponseEntity<List<CareerTaskDTO>> getAll() {
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
}
