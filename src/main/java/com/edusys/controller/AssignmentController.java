package com.edusys.controller;

import com.edusys.model.dto.AssignmentDTO;
import com.edusys.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@CrossOrigin
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentDTO> create(@RequestBody AssignmentDTO dto) {
        AssignmentDTO created = assignmentService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload-file", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getById(@PathVariable String id) {
        AssignmentDTO dto = assignmentService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        if (id != null && id.startsWith("item-")) {
            AssignmentDTO defaultDto = AssignmentDTO.builder()
                    .assignmentId(id)
                    .title("New Assignment")
                    .description("No description provided.")
                    .submissionTypeFile(true)
                    .submissionTypeOnlineText(false)
                    .maxSize("50MB")
                    .maxFiles(1)
                    .additionalFileUrl("[]")
                    .build();
            return ResponseEntity.ok(defaultDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAll() {
        return ResponseEntity.ok(assignmentService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> update(@PathVariable String id, @RequestBody AssignmentDTO dto) {
        AssignmentDTO updated = assignmentService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = assignmentService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
