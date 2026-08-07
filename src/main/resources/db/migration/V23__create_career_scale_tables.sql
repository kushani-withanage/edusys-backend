-- Drop old placeholder tables if they exist
DROP TABLE IF EXISTS evaluations;
DROP TABLE IF EXISTS career_points_ledger;
DROP TABLE IF EXISTS career_level_overrides;
DROP TABLE IF EXISTS student_career_progress;
DROP TABLE IF EXISTS career_submissions;
DROP TABLE IF EXISTS career_tasks;
DROP TABLE IF EXISTS career_levels;

-- 1. Create career_level
CREATE TABLE career_level (
    id              VARCHAR(36)  NOT NULL,
    level_number    INT          NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    points_required INT          NOT NULL,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uq_career_level_number UNIQUE (level_number)
);

-- 2. Create career_task
CREATE TABLE career_task (
    id              VARCHAR(36)  NOT NULL,
    level_id        VARCHAR(36)  NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    submission_type VARCHAR(20)  NOT NULL, -- LINK, IMAGE, PDF, FILE
    points_value    INT          NOT NULL,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_by      VARCHAR(36)  NOT NULL,
    created_at      DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_career_task_level FOREIGN KEY (level_id) REFERENCES career_level (id) ON DELETE CASCADE,
    CONSTRAINT fk_career_task_creator FOREIGN KEY (created_by) REFERENCES users (user_id)
);

-- 3. Create career_level_batch_access
CREATE TABLE career_level_batch_access (
    id         VARCHAR(36) NOT NULL,
    level_id   VARCHAR(36) NOT NULL,
    batch_id   VARCHAR(36) NOT NULL,
    is_open    BOOLEAN     NOT NULL DEFAULT TRUE,
    opened_by  VARCHAR(36) NOT NULL,
    opened_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_clba_level FOREIGN KEY (level_id) REFERENCES career_level (id) ON DELETE CASCADE,
    CONSTRAINT fk_clba_batch FOREIGN KEY (batch_id) REFERENCES batches (batch_id) ON DELETE CASCADE,
    CONSTRAINT fk_clba_creator FOREIGN KEY (opened_by) REFERENCES users (user_id)
);

-- 4. Create career_submission
CREATE TABLE career_submission (
    id                VARCHAR(36) NOT NULL,
    task_id           VARCHAR(36) NOT NULL,
    student_id        VARCHAR(36) NOT NULL,
    submission_type   VARCHAR(20) NOT NULL,
    submission_url    VARCHAR(1000),
    file_path         VARCHAR(1000),
    status            VARCHAR(30) NOT NULL, -- PENDING, APPROVED, REJECTED, REVISION_REQUESTED
    points_awarded    INT,
    reviewer_id       VARCHAR(36),
    reviewer_comment  TEXT,
    submitted_at      DATETIME(6) NOT NULL,
    reviewed_at       DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_cs_task FOREIGN KEY (task_id) REFERENCES career_task (id) ON DELETE CASCADE,
    CONSTRAINT fk_cs_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_cs_reviewer FOREIGN KEY (reviewer_id) REFERENCES users (user_id)
);

-- 5. Create student_career_progress
CREATE TABLE student_career_progress (
    student_id       VARCHAR(36) NOT NULL,
    current_level_id VARCHAR(36) NOT NULL,
    points_at_level  INT         NOT NULL DEFAULT 0,
    PRIMARY KEY (student_id),
    CONSTRAINT fk_scp_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_scp_level FOREIGN KEY (current_level_id) REFERENCES career_level (id)
);
