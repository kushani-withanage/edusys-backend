package com.edusys.controller;

import com.edusys.model.dto.QuestionDTO;
import com.edusys.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<QuestionDTO>> getQuestions(
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String status
    ) {
        if (courseId != null && !courseId.trim().isEmpty()) {
            return ResponseEntity.ok(questionService.getQuestions(courseId, difficulty, status));
        }
        return ResponseEntity.ok(questionService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuestionDTO> getById(@PathVariable String id) {
        QuestionDTO dto = questionService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuestionDTO> create(@RequestBody QuestionDTO dto) {
        QuestionDTO created = questionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuestionDTO> update(@PathVariable String id, @RequestBody QuestionDTO dto) {
        try {
            QuestionDTO updated = questionService.update(id, dto);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            boolean deleted = questionService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<QuestionDTO>> importQuestions(
            @RequestParam String courseId,
            @RequestParam String createdBy,
            @RequestBody String csvContent
    ) {
        try {
            List<QuestionDTO> imported = questionService.importQuestionsFromCsv(courseId, createdBy, csvContent);
            return ResponseEntity.ok(imported);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
