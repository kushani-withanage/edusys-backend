package com.edusys.controller;

import com.edusys.model.dto.CourseAccessGrantDTO;
import com.edusys.service.CourseAccessGrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course-access-grants")
@CrossOrigin
public class CourseAccessGrantController {

    @Autowired
    private CourseAccessGrantService service;

    @PostMapping
    public ResponseEntity<CourseAccessGrantDTO> create(@RequestBody CourseAccessGrantDTO dto) {
        CourseAccessGrantDTO created = service.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseAccessGrantDTO> getById(@PathVariable String id) {
        CourseAccessGrantDTO dto = service.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CourseAccessGrantDTO>> getAll(@RequestParam(required = false) String email) {
        if (email != null && !email.trim().isEmpty()) {
            return ResponseEntity.ok(service.getByUserIdentifier(email));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
