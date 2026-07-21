package com.edusys.controller;

import com.edusys.model.dto.AdminDTO;
import com.edusys.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminDTO> create(@RequestBody AdminDTO dto) {
        AdminDTO created = adminService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getById(@PathVariable String id) {
        AdminDTO dto = adminService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAll() {
        return ResponseEntity.ok(adminService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> update(@PathVariable String id, @RequestBody AdminDTO dto) {
        AdminDTO updated = adminService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = adminService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
