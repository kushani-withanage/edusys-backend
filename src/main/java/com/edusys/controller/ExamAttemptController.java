package com.edusys.controller;

import com.edusys.model.dto.ExamAttemptDTO;
import com.edusys.service.ExamAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-attempts")
@CrossOrigin
public class ExamAttemptController {

    @Autowired
    private ExamAttemptService examAttemptService;

    @PostMapping
    public ResponseEntity<ExamAttemptDTO> create(@RequestBody ExamAttemptDTO dto) {
        ExamAttemptDTO created = examAttemptService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamAttemptDTO> getById(@PathVariable String id) {
        ExamAttemptDTO dto = examAttemptService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ExamAttemptDTO>> getAll() {
        return ResponseEntity.ok(examAttemptService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamAttemptDTO> update(@PathVariable String id, @RequestBody ExamAttemptDTO dto) {
        ExamAttemptDTO updated = examAttemptService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = examAttemptService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ExamAttemptDTO>> getByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(examAttemptService.getByStudent(studentId));
    }
}
