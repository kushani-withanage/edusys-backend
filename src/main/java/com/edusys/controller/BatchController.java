package com.edusys.controller;

import com.edusys.model.dto.BatchDTO;
import com.edusys.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/batches")
@CrossOrigin
public class BatchController {

    @Autowired
    private BatchService batchService;

    @PostMapping
    public ResponseEntity<BatchDTO> create(@RequestBody BatchDTO dto) {
        BatchDTO created = batchService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchDTO> getById(@PathVariable String id) {
        BatchDTO dto = batchService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BatchDTO>> getAll() {
        return ResponseEntity.ok(batchService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchDTO> update(@PathVariable String id, @RequestBody BatchDTO dto) {
        BatchDTO updated = batchService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = batchService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
