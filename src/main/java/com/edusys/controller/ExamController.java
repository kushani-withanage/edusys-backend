package com.edusys.controller;

import com.edusys.model.dto.ExamDTO;
import com.edusys.model.dto.QuestionDTO;
import com.edusys.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exams")
@CrossOrigin
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ExamDTO> create(@RequestBody ExamDTO dto) {
        ExamDTO created = examService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<ExamDTO> getById(@PathVariable String id) {
        ExamDTO dto = examService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<ExamDTO>> getAll() {
        return ResponseEntity.ok(examService.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ExamDTO> update(@PathVariable String id, @RequestBody ExamDTO dto) {
        try {
            ExamDTO updated = examService.update(id, dto);
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
            boolean deleted = examService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ExamDTO> publish(@PathVariable String id) {
        try {
            ExamDTO published = examService.publish(id);
            if (published != null) {
                return ResponseEntity.ok(published);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ExamDTO> close(@PathVariable String id) {
        ExamDTO closed = examService.close(id);
        if (closed != null) {
            return ResponseEntity.ok(closed);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/questions")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<QuestionDTO>> getQuestions(@PathVariable String id) {
        List<QuestionDTO> questions = examService.getQuestionsForExam(id, false);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}/analytics")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Map<String, Object>> getAnalytics(@PathVariable String id) {
        Map<String, Object> analytics = examService.getExamAnalytics(id);
        return ResponseEntity.ok(analytics);
    }
}
