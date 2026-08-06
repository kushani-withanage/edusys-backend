-- ---------------------------------------------------------------------
-- Rename level column to course_level to avoid reserved keyword conflict in MySQL
-- ---------------------------------------------------------------------
ALTER TABLE courses CHANGE COLUMN level course_level VARCHAR(255);
