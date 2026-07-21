package com.edusys.controller;

import com.edusys.model.dto.TeacherDTO;
import com.edusys.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherDTO> create(@RequestBody TeacherDTO dto) {
        TeacherDTO created = teacherService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getById(@PathVariable String id) {
        TeacherDTO dto = teacherService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAll() {
        return ResponseEntity.ok(teacherService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> update(@PathVariable String id, @RequestBody TeacherDTO dto) {
        TeacherDTO updated = teacherService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = teacherService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
