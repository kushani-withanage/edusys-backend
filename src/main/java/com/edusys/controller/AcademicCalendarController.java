package com.edusys.controller;

import com.edusys.model.dto.AcademicCalendarDTO;
import com.edusys.service.AcademicCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic-calendars")
@CrossOrigin
public class AcademicCalendarController {

    @Autowired
    private AcademicCalendarService academicCalendarService;

    @PostMapping
    public ResponseEntity<AcademicCalendarDTO> create(@RequestBody AcademicCalendarDTO dto) {
        AcademicCalendarDTO created = academicCalendarService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicCalendarDTO> getById(@PathVariable String id) {
        AcademicCalendarDTO dto = academicCalendarService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AcademicCalendarDTO>> getAll() {
        return ResponseEntity.ok(academicCalendarService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicCalendarDTO> update(@PathVariable String id, @RequestBody AcademicCalendarDTO dto) {
        AcademicCalendarDTO updated = academicCalendarService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = academicCalendarService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
