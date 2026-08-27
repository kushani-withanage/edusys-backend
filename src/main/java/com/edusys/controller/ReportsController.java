package com.edusys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin
public class ReportsController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/merit-list")
    public ResponseEntity<List<Map<String, Object>>> getMeritList() {
        // Query to fetch student details, batch, career points, and grade average
        String sql = "SELECT u.user_id, u.full_name, b.batch_name, " +
                     "COALESCE(scp.points_at_level, 0) AS points " +
                     "FROM students s " +
                     "JOIN users u ON s.student_id = u.user_id " +
                     "LEFT JOIN batches b ON s.current_batch_id = b.batch_id " +
                     "LEFT JOIN student_career_progress scp ON s.student_id = scp.student_id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            String studentId = (String) row.get("user_id");
            String fullName = (String) row.get("full_name");
            String batchName = (String) row.get("batch_name");
            if (batchName == null) {
                batchName = "Unassigned";
            }
            Integer points = ((Number) row.get("points")).intValue();

            // Calculate GPA from grades
            String gradesSql = "SELECT grade_value FROM grades WHERE student_id = ?";
            List<String> gradeValues = jdbcTemplate.queryForList(gradesSql, String.class, studentId);
            double totalGpa = 0.0;
            int gradeCount = 0;

            for (String val : gradeValues) {
                double avg = parseGradeValue(val);
                totalGpa += avg;
                gradeCount++;
            }

            double gpa = 0.0;
            if (gradeCount > 0) {
                // Convert 0-100 average to a 4.0 GPA scale: (average / 100) * 4.0
                double rawAverage = totalGpa / gradeCount;
                gpa = Math.round((rawAverage / 100.0 * 4.0) * 100.0) / 100.0;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("fullName", fullName);
            map.put("batchCode", batchName);
            map.put("gpa", gpa);
            map.put("points", points);
            result.add(map);
        }

        // Sort by GPA descending, then points descending
        result.sort((o1, o2) -> {
            double g1 = (double) o1.get("gpa");
            double g2 = (double) o2.get("gpa");
            if (Double.compare(g2, g1) != 0) {
                return Double.compare(g2, g1);
            }
            int p1 = (int) o1.get("points");
            int p2 = (int) o2.get("points");
            return Integer.compare(p2, p1);
        });

        // Add rank
        for (int i = 0; i < result.size(); i++) {
            result.get(i).put("rank", i + 1);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<Map<String, Object>>> getAuditLogs() {
        List<LogEntry> allLogs = new ArrayList<>();

        // 1. User login logs
        String loginsSql = "SELECT full_name, last_login FROM users WHERE last_login IS NOT NULL";
        List<Map<String, Object>> logins = jdbcTemplate.queryForList(loginsSql);
        for (Map<String, Object> l : logins) {
            Object lastLoginObj = l.get("last_login");
            if (lastLoginObj != null) {
                LocalDateTime time = convertToLocalDateTime(lastLoginObj);
                allLogs.add(new LogEntry(
                    time,
                    (String) l.get("full_name"),
                    "Logged in to the system",
                    "Authentication"
                ));
            }
        }

        // 2. Exam attempts logs
        String examsSql = "SELECT u.full_name, e.title, ea.started_at, ea.submitted_at, ea.status FROM exam_attempts ea " +
                          "JOIN users u ON ea.student_id = u.user_id " +
                          "JOIN exams e ON ea.exam_id = e.id";
        List<Map<String, Object>> examAttempts = jdbcTemplate.queryForList(examsSql);
        for (Map<String, Object> ea : examAttempts) {
            Object startedObj = ea.get("started_at");
            Object submittedObj = ea.get("submitted_at");
            String status = (String) ea.get("status");
            String examTitle = (String) ea.get("title");
            String studentName = (String) ea.get("full_name");

            if (startedObj != null) {
                LocalDateTime time = convertToLocalDateTime(startedObj);
                allLogs.add(new LogEntry(
                    time,
                    studentName,
                    "Started exam attempt: " + examTitle,
                    "Exams"
                ));
            }

            if (submittedObj != null) {
                LocalDateTime time = convertToLocalDateTime(submittedObj);
                allLogs.add(new LogEntry(
                    time,
                    studentName,
                    "Submitted exam attempt: " + examTitle + " (Status: " + status + ")",
                    "Exams"
                ));
            }
        }

        // 3. Career progression logs
        String careerSql = "SELECT u_marker.full_name AS marker_name, t.title AS task_title, s.points_awarded, s.marked_at " +
                           "FROM career_student_task_status s " +
                           "JOIN users u_marker ON s.marked_by = u_marker.user_id " +
                           "JOIN career_task t ON s.task_id = t.id " +
                           "WHERE s.marked_at IS NOT NULL";
        List<Map<String, Object>> careerLogs = jdbcTemplate.queryForList(careerSql);
        for (Map<String, Object> cl : careerLogs) {
            Object markedObj = cl.get("marked_at");
            if (markedObj != null) {
                LocalDateTime time = convertToLocalDateTime(markedObj);
                Integer pts = cl.get("points_awarded") != null ? ((Number) cl.get("points_awarded")).intValue() : 0;
                allLogs.add(new LogEntry(
                    time,
                    (String) cl.get("marker_name"),
                    "Approved task: " + cl.get("task_title") + " (Awarded +" + pts + " pts)",
                    "Evaluation"
                ));
            }
        }

        // Sort by time descending
        allLogs.sort((o1, o2) -> o2.time.compareTo(o1.time));

        // Format to final list and cap at 15 items
        List<Map<String, Object>> result = new ArrayList<>();
        int count = 0;
        LocalDateTime now = LocalDateTime.now();
        for (LogEntry entry : allLogs) {
            if (count >= 15) break;

            Map<String, Object> map = new HashMap<>();
            map.put("time", getRelativeTime(entry.time, now));
            map.put("user", entry.user);
            map.put("action", entry.action);
            map.put("module", entry.module);
            result.add(map);
            count++;
        }

        return ResponseEntity.ok(result);
    }

    private double parseGradeValue(String gradeValue) {
        if (gradeValue == null) return 0.0;
        try {
            if (gradeValue.contains(",")) {
                String[] parts = gradeValue.split(",");
                double assignmentScore = Double.parseDouble(parts[0].trim());
                double examScore = Double.parseDouble(parts[1].trim());
                return (assignmentScore + examScore) / 2.0;
            } else {
                return Double.parseDouble(gradeValue.trim());
            }
        } catch (Exception e) {
            return 0.0;
        }
    }

    private LocalDateTime convertToLocalDateTime(Object dateObj) {
        if (dateObj instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) dateObj).toLocalDateTime();
        } else if (dateObj instanceof java.time.LocalDateTime) {
            return (LocalDateTime) dateObj;
        } else if (dateObj instanceof java.util.Date) {
            return LocalDateTime.ofInstant(((java.util.Date) dateObj).toInstant(), ZoneId.systemDefault());
        }
        return LocalDateTime.now();
    }

    private String getRelativeTime(LocalDateTime dateTime, LocalDateTime now) {
        Duration duration = Duration.between(dateTime, now);
        long seconds = duration.getSeconds();
        if (seconds < 60) return "Just now";
        long minutes = duration.toMinutes();
        if (minutes < 60) return minutes + " mins ago";
        long hours = duration.toHours();
        if (hours < 24) return hours + " hr" + (hours > 1 ? "s" : "") + " ago";
        long days = duration.toDays();
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private static class LogEntry {
        LocalDateTime time;
        String user;
        String action;
        String module;

        LogEntry(LocalDateTime time, String user, String action, String module) {
            this.time = time;
            this.user = user;
            this.action = action;
            this.module = module;
        }
    }
}
