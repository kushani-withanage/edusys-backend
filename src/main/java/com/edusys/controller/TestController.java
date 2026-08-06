package com.edusys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.edusys.repository.UserRepository;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping
    public String test(){
        return "Project - EduSys";
    }

    @GetMapping("/users")
    public List<String> getUsers() {
        List<String> list = new ArrayList<>();
        userRepository.findAll().forEach(u -> list.add(u.getEmail() + " : " + u.getPassword() + " : " + u.getRole()));
        return list;
    }

    @GetMapping("/check-passwords")
    public List<String> checkPasswords() {
        List<String> results = new ArrayList<>();
        String[] candidates = {
            "password123", "password", "12345", "123456", "admin123", "Admin123", 
            "admin", "123", "nethma", "nethma123", "Nethma123", "sachin", "sachin123"
        };
        userRepository.findAll().forEach(u -> {
            boolean matched = false;
            for (String candidate : candidates) {
                if (passwordEncoder.matches(candidate, u.getPassword())) {
                    results.add(u.getEmail() + " : " + candidate + " (" + u.getRole() + ")");
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                results.add(u.getEmail() + " : UNKNOWN HASH: " + u.getPassword());
            }
        });
        return results;
    }

    @Autowired
    private com.edusys.repository.CourseAccessGrantRepository courseAccessGrantRepository;

    @Autowired
    private com.edusys.service.CourseAccessGrantService courseAccessGrantService;

    @GetMapping("/db-check")
    public String dbCheck() {
        try {
            long count = courseAccessGrantRepository.count();
            return "Connection successful. course_access_grants count: " + count;
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            return "Error: " + e.getMessage() + "\nStacktrace:\n" + sw.toString();
        }
    }

    @GetMapping("/test-grant")
    public String testGrant() {
        try {
            com.edusys.model.dto.CourseAccessGrantDTO dto = new com.edusys.model.dto.CourseAccessGrantDTO();
            dto.setCourseId("crs0001");
            dto.setCourseName("Test Course");
            dto.setBatchCode("ICD110");
            dto.setUserIdentifier("sachin@edusys.com");
            com.edusys.model.dto.CourseAccessGrantDTO created = courseAccessGrantService.create(dto);
            return "Success: " + created.getId();
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            return "Error: " + e.getMessage() + "\nStacktrace:\n" + sw.toString();
        }
    }

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @GetMapping("/db-columns")
    public List<String> dbColumns() {
        try {
            return jdbcTemplate.query("SHOW COLUMNS FROM course_access_grants", (rs, rowNum) -> 
                rs.getString("Field") + " : " + rs.getString("Type") + " : " + rs.getString("Null")
            );
        } catch (Exception e) {
            List<String> list = new ArrayList<>();
            list.add("Error: " + e.getMessage());
            return list;
        }
    }

    @GetMapping("/flyway")
    public List<String> flyway() {
        try {
            return jdbcTemplate.query("SELECT version, description, success FROM flyway_schema_history", (rs, rowNum) -> 
                rs.getString("version") + " : " + rs.getString("description") + " : " + rs.getBoolean("success")
            );
        } catch (Exception e) {
            List<String> list = new ArrayList<>();
            list.add("Error: " + e.getMessage());
            return list;
        }
    }
}

