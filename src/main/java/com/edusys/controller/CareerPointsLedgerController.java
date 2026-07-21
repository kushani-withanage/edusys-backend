package com.edusys.controller;

import com.edusys.model.dto.CareerPointsLedgerDTO;
import com.edusys.service.CareerPointsLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/career-points-ledger")
@CrossOrigin
public class CareerPointsLedgerController {

    @Autowired
    private CareerPointsLedgerService careerPointsLedgerService;

    @PostMapping
    public ResponseEntity<CareerPointsLedgerDTO> create(@RequestBody CareerPointsLedgerDTO dto) {
        CareerPointsLedgerDTO created = careerPointsLedgerService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareerPointsLedgerDTO> getById(@PathVariable String id) {
        CareerPointsLedgerDTO dto = careerPointsLedgerService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CareerPointsLedgerDTO>> getAll() {
        return ResponseEntity.ok(careerPointsLedgerService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareerPointsLedgerDTO> update(@PathVariable String id, @RequestBody CareerPointsLedgerDTO dto) {
        CareerPointsLedgerDTO updated = careerPointsLedgerService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = careerPointsLedgerService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
