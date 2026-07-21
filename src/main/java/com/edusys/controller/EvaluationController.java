package com.edusys.controller;

import com.edusys.model.dto.EvaluationDTO;
import com.edusys.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluations")
@CrossOrigin
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<EvaluationDTO> create(@RequestBody EvaluationDTO dto) {
        EvaluationDTO created = evaluationService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationDTO> getById(@PathVariable String id) {
        EvaluationDTO dto = evaluationService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<EvaluationDTO>> getAll() {
        return ResponseEntity.ok(evaluationService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationDTO> update(@PathVariable String id, @RequestBody EvaluationDTO dto) {
        EvaluationDTO updated = evaluationService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = evaluationService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
