package com.edusys.controller;

import com.edusys.model.dto.AssignmentSubmissionDTO;
import com.edusys.service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignment-submissions")
@CrossOrigin
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;

    @PostMapping
    public ResponseEntity<AssignmentSubmissionDTO> create(@RequestBody AssignmentSubmissionDTO dto) {
        AssignmentSubmissionDTO created = assignmentSubmissionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentSubmissionDTO> getById(@PathVariable String id) {
        AssignmentSubmissionDTO dto = assignmentSubmissionService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AssignmentSubmissionDTO>> getAll() {
        return ResponseEntity.ok(assignmentSubmissionService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentSubmissionDTO> update(@PathVariable String id, @RequestBody AssignmentSubmissionDTO dto) {
        AssignmentSubmissionDTO updated = assignmentSubmissionService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = assignmentSubmissionService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/assignment/{assignmentId}/student/{studentId}")
    public ResponseEntity<AssignmentSubmissionDTO> getByAssignmentAndStudent(
            @PathVariable String assignmentId, @PathVariable String studentId) {
        AssignmentSubmissionDTO dto = assignmentSubmissionService.getByAssignmentAndStudent(assignmentId, studentId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<java.util.Map<String, String>> uploadFile(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            java.io.File uploadDir = new java.io.File("uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String originalFileName = file.getOriginalFilename();
            String savedFileName = System.currentTimeMillis() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            java.io.File dest = new java.io.File(uploadDir, savedFileName);
            file.transferTo(dest.getAbsoluteFile());

            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("fileName", savedFileName);
            response.put("fileUrl", "/uploads/" + savedFileName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<AssignmentSubmissionDTO>> getByAssignment(@PathVariable String assignmentId) {
        return ResponseEntity.ok(assignmentSubmissionService.getByAssignment(assignmentId));
    }
}
