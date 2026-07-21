package com.edusys.controller;

import com.edusys.model.dto.GradeDTO;
import com.edusys.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grades")
@CrossOrigin
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping
    public ResponseEntity<GradeDTO> create(@RequestBody GradeDTO dto) {
        GradeDTO created = gradeService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeDTO> getById(@PathVariable String id) {
        GradeDTO dto = gradeService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<GradeDTO>> getAll() {
        return ResponseEntity.ok(gradeService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeDTO> update(@PathVariable String id, @RequestBody GradeDTO dto) {
        GradeDTO updated = gradeService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = gradeService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
