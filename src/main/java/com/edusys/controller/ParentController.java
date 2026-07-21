package com.edusys.controller;

import com.edusys.model.dto.ParentDTO;
import com.edusys.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parents")
@CrossOrigin
public class ParentController {

    @Autowired
    private ParentService parentService;

    @PostMapping
    public ResponseEntity<ParentDTO> create(@RequestBody ParentDTO dto) {
        ParentDTO created = parentService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentDTO> getById(@PathVariable String id) {
        ParentDTO dto = parentService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ParentDTO>> getAll() {
        return ResponseEntity.ok(parentService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentDTO> update(@PathVariable String id, @RequestBody ParentDTO dto) {
        ParentDTO updated = parentService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = parentService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
