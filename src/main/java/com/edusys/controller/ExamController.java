package com.edusys.controller;

import com.edusys.model.dto.ExamDTO;
import com.edusys.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exams")
@CrossOrigin
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<ExamDTO> create(@RequestBody ExamDTO dto) {
        ExamDTO created = examService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> getById(@PathVariable String id) {
        ExamDTO dto = examService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ExamDTO>> getAll() {
        return ResponseEntity.ok(examService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDTO> update(@PathVariable String id, @RequestBody ExamDTO dto) {
        ExamDTO updated = examService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = examService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
