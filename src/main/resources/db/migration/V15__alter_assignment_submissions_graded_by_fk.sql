-- Drop the foreign key constraint that restricts graded_by to the teachers table
ALTER TABLE assignment_submissions DROP FOREIGN KEY fk_assignment_submissions_teacher;

-- Add a new foreign key constraint that references the parent users table so admins and teachers can grade
ALTER TABLE assignment_submissions ADD CONSTRAINT fk_assignment_submissions_grader FOREIGN KEY (graded_by) REFERENCES users (user_id);
