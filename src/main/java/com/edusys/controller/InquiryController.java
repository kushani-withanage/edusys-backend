package com.edusys.controller;

import com.edusys.model.dto.InquiryDTO;
import com.edusys.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiries")
@CrossOrigin
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<InquiryDTO> create(@RequestBody InquiryDTO dto) {
        InquiryDTO created = inquiryService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryDTO> getById(@PathVariable String id) {
        InquiryDTO dto = inquiryService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<InquiryDTO>> getAll() {
        return ResponseEntity.ok(inquiryService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InquiryDTO> update(@PathVariable String id, @RequestBody InquiryDTO dto) {
        InquiryDTO updated = inquiryService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = inquiryService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
