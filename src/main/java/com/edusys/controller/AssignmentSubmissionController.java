package com.edusys.controller;

import com.edusys.model.dto.AssignmentSubmissionDTO;
import com.edusys.service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignment-submissions")
@CrossOrigin
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;

    @PostMapping
    public ResponseEntity<AssignmentSubmissionDTO> create(@RequestBody AssignmentSubmissionDTO dto) {
        AssignmentSubmissionDTO created = assignmentSubmissionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentSubmissionDTO> getById(@PathVariable String id) {
        AssignmentSubmissionDTO dto = assignmentSubmissionService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AssignmentSubmissionDTO>> getAll() {
        return ResponseEntity.ok(assignmentSubmissionService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentSubmissionDTO> update(@PathVariable String id, @RequestBody AssignmentSubmissionDTO dto) {
        AssignmentSubmissionDTO updated = assignmentSubmissionService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = assignmentSubmissionService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
