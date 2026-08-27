-- 1. Drop old tables that are no longer needed
DROP TABLE IF EXISTS career_submission;
DROP TABLE IF EXISTS career_level_batch_access;

-- 2. Modify career_task: drop submission_type column
ALTER TABLE career_task DROP COLUMN submission_type;

-- 3. Create career_task_batch table
CREATE TABLE career_task_batch (
    task_id  VARCHAR(36) NOT NULL,
    batch_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (task_id, batch_id),
    CONSTRAINT fk_ctb_task  FOREIGN KEY (task_id)  REFERENCES career_task (id) ON DELETE CASCADE,
    CONSTRAINT fk_ctb_batch FOREIGN KEY (batch_id) REFERENCES batches (batch_id) ON DELETE CASCADE
);

-- 4. Create career_student_task_status table
CREATE TABLE career_student_task_status (
    id             VARCHAR(36) NOT NULL,
    task_id        VARCHAR(36) NOT NULL,
    student_id     VARCHAR(36) NOT NULL,
    status         VARCHAR(30) NOT NULL,
    points_awarded INT,
    marked_by      VARCHAR(36),
    marked_at      DATETIME(6),
    comment        TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_csts_task    FOREIGN KEY (task_id)    REFERENCES career_task (id) ON DELETE CASCADE,
    CONSTRAINT fk_csts_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_csts_marker  FOREIGN KEY (marked_by)  REFERENCES users (user_id) ON DELETE SET NULL,
    CONSTRAINT uq_csts_student_task UNIQUE (task_id, student_id)
);
