package com.edusys.controller;

import com.edusys.model.dto.SemesterDTO;
import com.edusys.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/semesters")
@CrossOrigin
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping
    public ResponseEntity<SemesterDTO> create(@RequestBody SemesterDTO dto) {
        SemesterDTO created = semesterService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SemesterDTO> getById(@PathVariable String id) {
        SemesterDTO dto = semesterService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<SemesterDTO>> getAll() {
        return ResponseEntity.ok(semesterService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemesterDTO> update(@PathVariable String id, @RequestBody SemesterDTO dto) {
        SemesterDTO updated = semesterService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = semesterService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
