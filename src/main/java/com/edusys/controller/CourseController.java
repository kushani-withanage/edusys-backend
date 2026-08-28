package com.edusys.controller;

import com.edusys.model.dto.CourseDTO;
import com.edusys.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDTO> create(@RequestBody CourseDTO dto) {
        CourseDTO created = courseService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseDTO>> getMyCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = authentication.getName();
        return ResponseEntity.ok(courseService.getCoursesForUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String userId = authentication.getName();
            boolean isStudent = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
            if (isStudent) {
                List<CourseDTO> myCourses = courseService.getCoursesForUser(userId);
                boolean hasAccess = myCourses.stream()
                        .anyMatch(c -> c.getCourseId().equalsIgnoreCase(id));
                if (!hasAccess) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        }
        CourseDTO dto = courseService.getById(id);
        if (dto != null) {
            if (authentication != null) {
                String userId = authentication.getName();
                boolean isStudent = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
                if (isStudent) {
                    List<CourseDTO> myCourses = courseService.getCoursesForUser(userId);
                    java.util.Optional<CourseDTO> matched = myCourses.stream()
                            .filter(c -> c.getCourseId().equalsIgnoreCase(id))
                            .findFirst();
                    if (matched.isPresent()) {
                        dto.setStatus(matched.get().getStatus());
                        dto.setBatchCode(matched.get().getBatchCode());
                    } else {
                        dto.setStatus("ongoing");
                    }
                }
            }
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateCourseStatus(@PathVariable String id, @RequestParam String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = authentication.getName();
        boolean success = courseService.updateCourseStatusForUser(userId, id, status);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable String id, @RequestBody CourseDTO dto) {
        CourseDTO updated = courseService.update(id, dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = courseService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
