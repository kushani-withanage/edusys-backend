package com.edusys.controller;

import com.edusys.model.dto.CareerLevelDTO;
import com.edusys.service.CareerLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/career-levels")
@CrossOrigin
public class CareerLevelController {

    @Autowired
    private CareerLevelService careerLevelService;

    @PostMapping
    public ResponseEntity<CareerLevelDTO> create(@RequestBody CareerLevelDTO dto) {
        CareerLevelDTO created = careerLevelService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareerLevelDTO> getById(@PathVariable String id) {
        CareerLevelDTO dto = careerLevelService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CareerLevelDTO>> getAll() {
        return ResponseEntity.ok(careerLevelService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareerLevelDTO> update(@PathVariable String id, @RequestBody CareerLevelDTO dto) {
        CareerLevelDTO updated = careerLevelService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            boolean deleted = careerLevelService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
