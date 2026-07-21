package com.edusys.controller;

import com.edusys.model.dto.ReceiptDTO;
import com.edusys.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receipts")
@CrossOrigin
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<ReceiptDTO> create(@RequestBody ReceiptDTO dto) {
        ReceiptDTO created = receiptService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDTO> getById(@PathVariable String id) {
        ReceiptDTO dto = receiptService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ReceiptDTO>> getAll() {
        return ResponseEntity.ok(receiptService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceiptDTO> update(@PathVariable String id, @RequestBody ReceiptDTO dto) {
        ReceiptDTO updated = receiptService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = receiptService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
