package com.edusys.controller;

import com.edusys.model.dto.QuestionBankDTO;
import com.edusys.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question-bank")
@CrossOrigin
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    @PostMapping
    public ResponseEntity<QuestionBankDTO> create(@RequestBody QuestionBankDTO dto) {
        QuestionBankDTO created = questionBankService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionBankDTO> getById(@PathVariable String id) {
        QuestionBankDTO dto = questionBankService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<QuestionBankDTO>> getAll() {
        return ResponseEntity.ok(questionBankService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionBankDTO> update(@PathVariable String id, @RequestBody QuestionBankDTO dto) {
        QuestionBankDTO updated = questionBankService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = questionBankService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
