package com.edusys.controller;

import com.edusys.model.dto.ExamCourseDTO;
import com.edusys.service.ExamCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam-courses")
@CrossOrigin
public class ExamCourseController {

    @Autowired
    private ExamCourseService examCourseService;

    @PostMapping
    public ResponseEntity<ExamCourseDTO> create(@RequestBody ExamCourseDTO dto) {
        ExamCourseDTO created = examCourseService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamCourseDTO> getById(@PathVariable String id) {
        ExamCourseDTO dto = examCourseService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ExamCourseDTO>> getAll() {
        return ResponseEntity.ok(examCourseService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamCourseDTO> update(@PathVariable String id, @RequestBody ExamCourseDTO dto) {
        ExamCourseDTO updated = examCourseService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = examCourseService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
