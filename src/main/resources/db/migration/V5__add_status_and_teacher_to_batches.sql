-- Add status, teacher, and course_name columns to batches table
ALTER TABLE batches ADD COLUMN status VARCHAR(255) DEFAULT 'Active';
ALTER TABLE batches ADD COLUMN teacher VARCHAR(255) DEFAULT 'Mr. Kasun Jayasuriya';
ALTER TABLE batches ADD COLUMN course_name VARCHAR(255) DEFAULT 'Programming Fundamentals';

-- Update existing seeded batches to match frontend mocks exactly
UPDATE batches SET status = 'Finished', teacher = 'Mr. Kasun Jayasuriya', course_name = 'Programming Fundamentals' WHERE batch_id = 'bat0001';
UPDATE batches SET status = 'Active', teacher = 'Mrs. Kushani Withanage', course_name = 'Database Management Systems' WHERE batch_id = 'bat0002';
UPDATE batches SET status = 'Active', teacher = 'Mr. Kasun Jayasuriya', course_name = 'Object Oriented Programming' WHERE batch_id = 'bat0003';
