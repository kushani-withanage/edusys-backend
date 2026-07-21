package com.edusys.controller;

import com.edusys.model.dto.ParentStudentLinkDTO;
import com.edusys.service.ParentStudentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parent-student-links")
@CrossOrigin
public class ParentStudentLinkController {

    @Autowired
    private ParentStudentLinkService parentStudentLinkService;

    @PostMapping
    public ResponseEntity<ParentStudentLinkDTO> create(@RequestBody ParentStudentLinkDTO dto) {
        ParentStudentLinkDTO created = parentStudentLinkService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentStudentLinkDTO> getById(@PathVariable String id) {
        ParentStudentLinkDTO dto = parentStudentLinkService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ParentStudentLinkDTO>> getAll() {
        return ResponseEntity.ok(parentStudentLinkService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentStudentLinkDTO> update(@PathVariable String id, @RequestBody ParentStudentLinkDTO dto) {
        ParentStudentLinkDTO updated = parentStudentLinkService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = parentStudentLinkService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
