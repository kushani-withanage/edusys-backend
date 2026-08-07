-- Migration to add NIC column to students table
ALTER TABLE students ADD COLUMN nic VARCHAR(30);

-- Populate existing student rows with unique fallback values derived from their IDs
UPDATE students SET nic = CONCAT('99000000', SUBSTRING(student_id, 4)) WHERE nic IS NULL;

-- Alter column to NOT NULL and add UNIQUE constraint
ALTER TABLE students MODIFY COLUMN nic VARCHAR(30) NOT NULL;
ALTER TABLE students ADD CONSTRAINT uq_students_nic UNIQUE (nic);
