package com.edusys.controller;

import com.edusys.model.dto.ReviewerDTO;
import com.edusys.service.ReviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviewers")
@CrossOrigin
public class ReviewerController {

    @Autowired
    private ReviewerService reviewerService;

    @PostMapping
    public ResponseEntity<ReviewerDTO> create(@RequestBody ReviewerDTO dto) {
        ReviewerDTO created = reviewerService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewerDTO> getById(@PathVariable String id) {
        ReviewerDTO dto = reviewerService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ReviewerDTO>> getAll() {
        return ResponseEntity.ok(reviewerService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewerDTO> update(@PathVariable String id, @RequestBody ReviewerDTO dto) {
        ReviewerDTO updated = reviewerService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = reviewerService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
