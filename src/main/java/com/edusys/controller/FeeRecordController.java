package com.edusys.controller;

import com.edusys.model.dto.FeeRecordDTO;
import com.edusys.service.FeeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fee-records")
@CrossOrigin
public class FeeRecordController {

    @Autowired
    private FeeRecordService feeRecordService;

    @PostMapping
    public ResponseEntity<FeeRecordDTO> create(@RequestBody FeeRecordDTO dto) {
        FeeRecordDTO created = feeRecordService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeRecordDTO> getById(@PathVariable String id) {
        FeeRecordDTO dto = feeRecordService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<FeeRecordDTO>> getAll() {
        return ResponseEntity.ok(feeRecordService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeeRecordDTO> update(@PathVariable String id, @RequestBody FeeRecordDTO dto) {
        FeeRecordDTO updated = feeRecordService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = feeRecordService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
