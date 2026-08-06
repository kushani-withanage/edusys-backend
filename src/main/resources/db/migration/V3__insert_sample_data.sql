-- =====================================================================
-- EduSys Additional Sample Data (Gathered from Frontend Mock Data)
-- =====================================================================

-- 1. Seeding additional users with different roles
-- Admin user: admin@edusys.com / password: password123
-- Reviewer user: reviewer@edusys.com / password: password123
-- Parent user: parent@edusys.com / password: password123
INSERT IGNORE INTO users (user_id, full_name, email, role, phone, password, status, created_at) VALUES
('usr0007', 'Admin User', 'admin@edusys.com', 'ADMIN', '+94770000006', '$2b$10$AgoVrpMeGpWc73R0bfx9yu9N.8arSNL86Jgn0USpqXu6ORrwtYam2', 'ACTIVE', NOW()),
('usr0008', 'Reviewer User', 'reviewer@edusys.com', 'REVIEWER', '+94770000007', '$2b$10$AgoVrpMeGpWc73R0bfx9yu9N.8arSNL86Jgn0USpqXu6ORrwtYam2', 'ACTIVE', NOW()),
('usr0009', 'Parent User', 'parent@edusys.com', 'PARENT', '+94770000008', '$2b$10$AgoVrpMeGpWc73R0bfx9yu9N.8arSNL86Jgn0USpqXu6ORrwtYam2', 'ACTIVE', NOW());

INSERT IGNORE INTO admins (admin_id, department) VALUES 
('usr0007', 'Management');

INSERT IGNORE INTO reviewers (reviewer_id, expertise_area) VALUES 
('usr0008', 'Full Stack Development');

INSERT IGNORE INTO parents (parent_id, occupation) VALUES 
('usr0009', 'Engineer');

-- 2. Seeding Parent-Student Link
INSERT IGNORE INTO parent_student_links (link_id, parent_id, student_id, relationship_type, linked_date) VALUES
('lnk0001', 'usr0009', 'usr0002', 'Father', '2026-01-15');

-- 3. Seeding Academic Calendars (Events)
INSERT IGNORE INTO academic_calendars (calendar_id, event_name, event_date, description, status, batch_id) VALUES
('evt0001', 'Term 1 Exam: DBMS', '2026-07-20', '1-Hour assessment test', 'EXAM', 'bat0002'),
('evt0002', 'Mid-Term Summer Holiday', '2026-07-25', 'Full campus closure', 'HOLIDAY', NULL),
('evt0003', 'OOP Class Session', '2026-07-15', 'Review session with Mr. Kasun Jayasuriya', 'CLASS', 'bat0003');

-- 4. Seeding Inquiries
INSERT IGNORE INTO inquiries (inquiry_id, applicant_name, contact_info, status, inquiry_date, batch_id) VALUES
('inq0001', 'Sharadha Madusinghe', 'sharadha@gmail.com', 'New', '2026-07-11', 'bat0001'),
('inq0002', 'Dilshan Perera', 'dilshan@gmail.com', 'New', '2026-07-10', 'bat0001'),
('inq0003', 'Kavindi Samarasinghe', 'kavindi@gmail.com', 'Contacted', '2026-07-05', 'bat0001'),
('inq0004', 'Sachin Samarawickrama', 'sachin@gmail.com', 'Provisionally Enrolled', '2026-07-01', 'bat0001');

-- 5. Seeding Career Tasks
INSERT IGNORE INTO career_tasks (task_id, title, description, due_date, rubric_criteria, point_value) VALUES
('tsk0001', 'Complete Git Workflow & Pull Requests', 'Submit repository demonstrating branches, conflict merge resolution, and code reviews.', '2026-08-10', '100% Code Weight', 50),
('tsk0002', 'Develop Full Stack React CRUD App', 'Deploy a React frontend client talking to a REST server with relational schemas.', '2026-08-15', '80% Code Weight', 150);

-- 6. Seeding Career Submissions
INSERT IGNORE INTO career_submissions (submission_id, task_id, student_id, status, submitted_file, submit_date) VALUES
('sub0001', 'tsk0001', 'usr0002', 'PENDING', 'http://github.com/sachin/git-workflow', '2026-08-01 10:00:00'),
('sub0002', 'tsk0002', 'usr0003', 'PENDING', 'http://github.com/pawara/react-crud', '2026-08-02 11:30:00'),
('sub0003', 'tsk0001', 'usr0004', 'APPROVED', 'http://github.com/dinuka/git-workflow', '2026-07-30 09:15:00');

-- 7. Seeding Evaluations
INSERT IGNORE INTO evaluations (evaluation_id, submission_id, reviewer_id, comments, override_reason, evaluation_date, override_flag) VALUES
('evl0001', 'sub0003', 'usr0008', 'Great job resolving conflicts.', NULL, '2026-08-01', 0);

-- 8. Seeding Career Points Ledger
INSERT IGNORE INTO career_points_ledger (ledger_id, student_id, task_id, points_awarded, entry_date, remarks) VALUES
('ldg0001', 'usr0004', 'tsk0001', 50, '2026-08-01', 'Task Complete Git Workflow & Pull Requests approved');

-- 9. Seeding Assignments (Course Materials)
INSERT IGNORE INTO assignments (assignment_id, title, description, due_date, created_by) VALUES
('asg0001', 'Git branching structures roadmap.pdf', 'ICD110', '2026-07-10', 'usr0001'),
('asg0002', 'Flexbox and responsive UI grid layout guides.zip', 'ICM111', '2026-07-08', 'usr0001');

-- 10. Seeding Grades (Results)
INSERT IGNORE INTO grades (grade_id, student_id, course_id, submission_id, grade_value, published_date) VALUES
('grd0001', 'usr0002', 'crs0006', NULL, '88,90', '2026-07-25'),
('grd0002', 'usr0003', 'crs0006', NULL, '95,92', '2026-07-25'),
('grd0003', 'usr0004', 'crs0006', NULL, '78,80', '2026-07-25');
