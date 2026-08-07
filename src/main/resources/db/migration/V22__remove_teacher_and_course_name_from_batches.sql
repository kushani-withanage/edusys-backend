-- Remove course_name and teacher columns from batches table
ALTER TABLE batches DROP COLUMN course_name;
ALTER TABLE batches DROP COLUMN teacher;
