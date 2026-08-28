-- 1. FIRST DROP THE OLD FOREIGN KEYS
ALTER TABLE questions DROP FOREIGN KEY fk_questions_course;
ALTER TABLE exams DROP FOREIGN KEY fk_exams_course;

-- 2. NOW SANITIZE THE DATA
UPDATE questions SET course_id = 'EXC0001' WHERE course_id = 'crs0001';
UPDATE questions SET course_id = 'EXC0002' WHERE course_id = 'crs0002';
UPDATE questions SET course_id = 'EXC0003' WHERE course_id = 'crs0003';
UPDATE questions SET course_id = 'EXC0001' WHERE course_id NOT IN (SELECT course_id FROM exam_courses);

UPDATE exams SET course_id = 'EXC0001' WHERE course_id = 'crs0001';
UPDATE exams SET course_id = 'EXC0002' WHERE course_id = 'crs0002';
UPDATE exams SET course_id = 'EXC0003' WHERE course_id = 'crs0003';
UPDATE exams SET course_id = 'EXC0001' WHERE course_id NOT IN (SELECT course_id FROM exam_courses) AND course_id IS NOT NULL;

-- 3. FINALLY ADD THE NEW FOREIGN KEYS
ALTER TABLE questions ADD CONSTRAINT fk_questions_course FOREIGN KEY (course_id) REFERENCES exam_courses(course_id) ON DELETE CASCADE;
ALTER TABLE exams ADD CONSTRAINT fk_exams_course FOREIGN KEY (course_id) REFERENCES exam_courses(course_id) ON DELETE SET NULL;
