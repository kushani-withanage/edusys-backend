package com.edusys.controller;

import com.edusys.model.dto.AssignmentDTO;
import com.edusys.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@CrossOrigin
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentDTO> create(@RequestBody AssignmentDTO dto) {
        AssignmentDTO created = assignmentService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getById(@PathVariable String id) {
        AssignmentDTO dto = assignmentService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAll() {
        return ResponseEntity.ok(assignmentService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> update(@PathVariable String id, @RequestBody AssignmentDTO dto) {
        AssignmentDTO updated = assignmentService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = assignmentService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
