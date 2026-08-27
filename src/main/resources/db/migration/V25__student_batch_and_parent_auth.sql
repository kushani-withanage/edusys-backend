-- Alter users to make password nullable and add must_set_password column
ALTER TABLE users MODIFY COLUMN password VARCHAR(255) NULL;
ALTER TABLE users ADD COLUMN must_set_password TINYINT(1) NOT NULL DEFAULT 0;

-- Add current_batch_id to students table
ALTER TABLE students ADD COLUMN current_batch_id VARCHAR(36) NULL;
ALTER TABLE students ADD CONSTRAINT fk_students_current_batch FOREIGN KEY (current_batch_id) REFERENCES batches (batch_id) ON DELETE SET NULL;

-- Create student_batch_history table
CREATE TABLE student_batch_history (
    id VARCHAR(36) NOT NULL,
    student_id VARCHAR(36) NOT NULL,
    batch_id VARCHAR(36) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_sbh_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_sbh_batch FOREIGN KEY (batch_id) REFERENCES batches (batch_id) ON DELETE CASCADE
);

-- Create student_parent table
CREATE TABLE student_parent (
    student_id VARCHAR(36) NOT NULL,
    parent_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (student_id, parent_id),
    CONSTRAINT fk_sp_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_sp_parent FOREIGN KEY (parent_id) REFERENCES parents (parent_id) ON DELETE CASCADE
);
