package com.edusys.controller;

import com.edusys.model.dto.CareerSubmissionDTO;
import com.edusys.service.CareerSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/career-submissions")
@CrossOrigin
public class CareerSubmissionController {

    @Autowired
    private CareerSubmissionService careerSubmissionService;

    @PostMapping
    public ResponseEntity<CareerSubmissionDTO> create(@RequestBody CareerSubmissionDTO dto) {
        CareerSubmissionDTO created = careerSubmissionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareerSubmissionDTO> getById(@PathVariable String id) {
        CareerSubmissionDTO dto = careerSubmissionService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CareerSubmissionDTO>> getAll() {
        return ResponseEntity.ok(careerSubmissionService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareerSubmissionDTO> update(@PathVariable String id, @RequestBody CareerSubmissionDTO dto) {
        CareerSubmissionDTO updated = careerSubmissionService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = careerSubmissionService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
