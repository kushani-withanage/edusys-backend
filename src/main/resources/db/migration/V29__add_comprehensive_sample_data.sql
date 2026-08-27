-- =====================================================================
-- EduSys Comprehensive Sample Data Seeding for New Schema Tables
-- =====================================================================

-- 1. Seeding Career Levels (L1 - L7)
INSERT IGNORE INTO career_level (id, level_number, title, description, points_required, is_active) VALUES
('lvl0001', 1, 'Explorer', 'Basic programming fundamentals, CLI tools, and version control foundations.', 100, 1),
('lvl0002', 2, 'Builder', 'Responsive user interfaces, simple web applications, and styling systems.', 200, 1),
('lvl0003', 3, 'Developer', 'Proficient in writing full stack CRUD interfaces, working with relational databases, and integrating APIs.', 300, 1),
('lvl0004', 4, 'Engineer', 'Architectural design, writing test suites, optimizing performance, and handling security protocols.', 400, 1),
('lvl0005', 5, 'Architect', 'Designing highly scalable microservices, complex cloud architectures, and mentoring developers.', 500, 1),
('lvl0006', 6, 'Lead', 'Leads development teams, establishes CI/CD pipelines, and leads high-impact features.', 600, 1),
('lvl0007', 7, 'Master', 'Designs complex frameworks and makes major strategic technical decisions.', 8000, 1);

-- 2. Link existing students to their active batches (which was previously left NULL)
UPDATE students SET current_batch_id = 'bat0001' WHERE student_id IN ('usr0002', 'usr0003');
UPDATE students SET current_batch_id = 'bat0002' WHERE student_id = 'usr0004';
UPDATE students SET current_batch_id = 'bat0003' WHERE student_id IN ('usr0005', 'usr0006');

-- 3. Seed student career progression standing
INSERT IGNORE INTO student_career_progress (student_id, current_level_id, points_at_level) VALUES
('usr0002', 'lvl0001', 60),
('usr0003', 'lvl0002', 120),
('usr0004', 'lvl0003', 240),
('usr0005', 'lvl0004', 180),
('usr0006', 'lvl0001', 30);

-- 4. Seed Career Tasks
INSERT IGNORE INTO career_task (id, level_id, title, description, points_value, is_active, created_by, created_at) VALUES
('tsk0001', 'lvl0001', 'Complete Git Workflow & Pull Requests', 'Submit repository demonstrating branches, conflict merge resolution, and code reviews.', 50, 1, 'usr0001', NOW()),
('tsk0002', 'lvl0002', 'Develop Full Stack React CRUD App', 'Deploy a React frontend client talking to a REST server with relational schemas.', 150, 1, 'usr0001', NOW()),
('tsk0003', 'lvl0003', 'Design Patterns Implementation', 'Submit a codebase demonstrating usage of creational, structural, and behavioral design patterns.', 200, 1, 'usr0001', NOW());

-- 5. Link Career Tasks to Batches
INSERT IGNORE INTO career_task_batch (task_id, batch_id) VALUES
('tsk0001', 'bat0001'), ('tsk0001', 'bat0002'), ('tsk0001', 'bat0003'),
('tsk0002', 'bat0001'), ('tsk0002', 'bat0002'),
('tsk0003', 'bat0001'), ('tsk0003', 'bat0003');

-- 6. Seed Career Task Completion Statuses
INSERT IGNORE INTO career_student_task_status (id, task_id, student_id, status, points_awarded, marked_by, marked_at, comment) VALUES
('csts0001', 'tsk0001', 'usr0002', 'COMPLETED', 50, 'usr0007', NOW(), 'Excellent Git branch discipline and detailed PR descriptions.'),
('csts0002', 'tsk0002', 'usr0003', 'COMPLETED', 140, 'usr0007', NOW(), 'React state management is solid. UI could be slightly more polished.'),
('csts0003', 'tsk0001', 'usr0004', 'COMPLETED', 45, 'usr0007', NOW(), 'Conflicts were resolved correctly but commits could be squashed.'),
('csts0004', 'tsk0002', 'usr0002', 'IN_PROGRESS', NULL, NULL, NULL, NULL),
('csts0005', 'tsk0003', 'usr0004', 'NOT_STARTED', NULL, NULL, NULL, NULL);

-- 7. Seed Questions Bank
INSERT IGNORE INTO questions (id, course_id, question_text, question_type, difficulty, default_marks, status, created_by, created_at, updated_at) VALUES
-- Programming Fundamentals (crs0001)
('qst0001', 'crs0001', 'What is the correct way to declare an integer variable in Java?', 'MCQ', 'EASY', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0002', 'crs0001', 'Which loop is guaranteed to execute at least once?', 'MCQ', 'EASY', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0003', 'crs0001', 'What is the logical operator for AND in Java?', 'MCQ', 'EASY', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0004', 'crs0001', 'Which of the following is not a primitive data type in Java?', 'MCQ', 'EASY', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0005', 'crs0001', 'How do you start writing a single-line comment in Java?', 'MCQ', 'EASY', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
-- Object Oriented Programming (crs0002)
('qst0006', 'crs0002', 'Which keyword is used to inherit a class in Java?', 'MCQ', 'EASY', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0007', 'crs0002', 'What is the process of hiding implementation details and showing only functionality?', 'MCQ', 'MEDIUM', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0008', 'crs0002', 'Which of the following allows a subclass to provide a specific implementation of a method in its superclass?', 'MCQ', 'MEDIUM', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0009', 'crs0002', 'Which modifier makes a variable accessible only within the same class?', 'MCQ', 'EASY', 5, 'PUBLISHED', 'usr0001', NOW(), NOW()),
('qst0010', 'crs0002', 'What type of method does not have a body in an abstract class?', 'MCQ', 'MEDIUM', 5, 'PUBLISHED', 'usr0001', NOW(), NOW());

-- 8. Seed Options for Questions
INSERT IGNORE INTO question_options (id, question_id, option_text, is_correct, order_index) VALUES
('opt0001', 'qst0001', 'int x;', 1, 0),
('opt0002', 'qst0001', 'float x;', 0, 1),
('opt0003', 'qst0001', 'integer x;', 0, 2),
('opt0004', 'qst0001', 'var int x;', 0, 3),

('opt0005', 'qst0002', 'do-while', 1, 0),
('opt0006', 'qst0002', 'while', 0, 1),
('opt0007', 'qst0002', 'for', 0, 2),
('opt0008', 'qst0002', 'for-each', 0, 3),

('opt0009', 'qst0003', '&&', 1, 0),
('opt0010', 'qst0003', '||', 0, 1),
('opt0011', 'qst0003', '!', 0, 2),
('opt0012', 'qst0003', '&', 0, 3),

('opt0013', 'qst0004', 'String', 1, 0),
('opt0014', 'qst0004', 'int', 0, 1),
('opt0015', 'qst0004', 'char', 0, 2),
('opt0016', 'qst0004', 'double', 0, 3),

('opt0017', 'qst0005', '//', 1, 0),
('opt0018', 'qst0005', '/*', 0, 1),
('opt0019', 'qst0005', '#', 0, 2),
('opt0020', 'qst0005', '<!--', 0, 3),

('opt0021', 'qst0006', 'extends', 1, 0),
('opt0022', 'qst0006', 'implements', 0, 1),
('opt0023', 'qst0006', 'inherits', 0, 2),
('opt0024', 'qst0006', 'exports', 0, 3),

('opt0025', 'qst0007', 'Abstraction', 1, 0),
('opt0026', 'qst0007', 'Encapsulation', 0, 1),
('opt0027', 'qst0007', 'Inheritance', 0, 2),
('opt0028', 'qst0007', 'Polymorphism', 0, 3),

('opt0029', 'qst0008', 'Method Overriding', 1, 0),
('opt0030', 'qst0008', 'Method Overloading', 0, 1),
('opt0031', 'qst0008', 'Method Overhiding', 0, 2),
('opt0032', 'qst0008', 'Method Overwrapping', 0, 3),

('opt0033', 'qst0009', 'private', 1, 0),
('opt0034', 'qst0009', 'public', 0, 1),
('opt0035', 'qst0009', 'protected', 0, 2),
('opt0036', 'qst0009', 'default', 0, 3),

('opt0037', 'qst0010', 'Abstract method', 1, 0),
('opt0038', 'qst0010', 'Static method', 0, 1),
('opt0039', 'qst0010', 'Private method', 0, 2),
('opt0040', 'qst0010', 'Final method', 0, 3);

-- 9. Seed Exams
INSERT IGNORE INTO exams (id, title, description, course_id, created_by, start_time, end_time, duration_minutes, shuffle_questions, shuffle_options, attempts_allowed, status, created_at) VALUES
('exm0001', 'Programming Fundamentals - Term Test', 'Mid-term assessment MCQ test.', 'crs0001', 'usr0001', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), 30, 0, 0, 1, 'PUBLISHED', NOW()),
('exm0002', 'Object Oriented Programming - Final Assessment', 'Comprehensive finals MCQ test.', 'crs0002', 'usr0001', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), 45, 0, 0, 1, 'PUBLISHED', NOW());

-- 10. Map Questions to Exams
INSERT IGNORE INTO exam_questions (exam_id, question_id, marks_override, order_index) VALUES
('exm0001', 'qst0001', NULL, 0),
('exm0001', 'qst0002', NULL, 1),
('exm0001', 'qst0003', NULL, 2),
('exm0001', 'qst0004', NULL, 3),
('exm0001', 'qst0005', NULL, 4),

('exm0002', 'qst0006', NULL, 0),
('exm0002', 'qst0007', NULL, 1),
('exm0002', 'qst0008', NULL, 2),
('exm0002', 'qst0009', NULL, 3),
('exm0002', 'qst0010', NULL, 4);

-- 11. Seed Target Audiences (Batches) for Exams
INSERT IGNORE INTO exam_audiences (id, exam_id, target_type, target_id) VALUES
('aud0001', 'exm0001', 'BATCH', 'bat0001'),
('aud0002', 'exm0001', 'BATCH', 'bat0002'),
('aud0003', 'exm0002', 'BATCH', 'bat0001'),
('aud0004', 'exm0002', 'BATCH', 'bat0003');

-- 12. Seed Exam Attempts and Graded Scores
INSERT IGNORE INTO exam_attempts (id, exam_id, student_id, started_at, submitted_at, status, score, question_order) VALUES
('atm0001', 'exm0001', 'usr0002', DATE_SUB(NOW(), INTERVAL 10 HOUR), DATE_SUB(NOW(), INTERVAL 9 HOUR), 'SUBMITTED', 80.0, 'qst0001,qst0002,qst0003,qst0004,qst0005'),
('atm0002', 'exm0001', 'usr0003', DATE_SUB(NOW(), INTERVAL 8 HOUR), DATE_SUB(NOW(), INTERVAL 7 HOUR), 'SUBMITTED', 100.0, 'qst0001,qst0002,qst0003,qst0004,qst0005'),
('atm0003', 'exm0002', 'usr0002', DATE_SUB(NOW(), INTERVAL 6 HOUR), NULL, 'STARTED', NULL, 'qst0006,qst0007,qst0008,qst0009,qst0010');

-- 13. Seed Answers Submitted for Attempts
INSERT IGNORE INTO exam_answers (id, attempt_id, question_id, selected_option_ids, is_correct, marks_awarded) VALUES
-- Sachin's answers (score 80% - 4 correct, 1 wrong)
('ans0001', 'atm0001', 'qst0001', '["opt0001"]', 1, 5.0),
('ans0002', 'atm0001', 'qst0002', '["opt0005"]', 1, 5.0),
('ans0003', 'atm0001', 'qst0003', '["opt0009"]', 1, 5.0),
('ans0004', 'atm0001', 'qst0004', '["opt0013"]', 1, 5.0),
('ans0005', 'atm0001', 'qst0005', '["opt0018"]', 0, 0.0), -- Incorrect option (/* instead of //)

-- Pawara's answers (score 100% - 5 correct)
('ans0006', 'atm0002', 'qst0001', '["opt0001"]', 1, 5.0),
('ans0007', 'atm0002', 'qst0002', '["opt0005"]', 1, 5.0),
('ans0008', 'atm0002', 'qst0003', '["opt0009"]', 1, 5.0),
('ans0009', 'atm0002', 'qst0004', '["opt0013"]', 1, 5.0),
('ans0010', 'atm0002', 'qst0005', '["opt0017"]', 1, 5.0);
