-- Disable foreign key checks to allow clearing tables
SET FOREIGN_KEY_CHECKS = 0;

-- Clear Exam Subsystem tables
TRUNCATE TABLE exam_answers;
TRUNCATE TABLE exam_attempts;
TRUNCATE TABLE exam_questions;
TRUNCATE TABLE exam_audiences;
TRUNCATE TABLE exams;
TRUNCATE TABLE question_options;
TRUNCATE TABLE questions;
TRUNCATE TABLE exam_courses;

-- Clear Assignment Subsystem tables
TRUNCATE TABLE assignment_submissions;
TRUNCATE TABLE assignments;

-- Clear Course Access & Grants
TRUNCATE TABLE course_access_grants;

-- Clear Career Scale Subsystem tables
TRUNCATE TABLE career_student_task_status;
TRUNCATE TABLE career_task_batch;
TRUNCATE TABLE student_career_progress;
TRUNCATE TABLE career_task;
TRUNCATE TABLE career_level;

-- Clear Finance & Academics tables
TRUNCATE TABLE inquiries;
TRUNCATE TABLE grades;
TRUNCATE TABLE receipts;
TRUNCATE TABLE fee_records;
TRUNCATE TABLE academic_calendars;
TRUNCATE TABLE enrollments;
TRUNCATE TABLE parent_student_links;
TRUNCATE TABLE student_batch_history;
TRUNCATE TABLE students;
TRUNCATE TABLE batch_course;
TRUNCATE TABLE batches;
TRUNCATE TABLE courses;
TRUNCATE TABLE semesters;

-- Clear User & Role tables
TRUNCATE TABLE parents;
TRUNCATE TABLE reviewers;
TRUNCATE TABLE teachers;
TRUNCATE TABLE admins;
TRUNCATE TABLE users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
