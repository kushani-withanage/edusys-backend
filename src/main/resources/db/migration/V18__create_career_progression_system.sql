-- Drop old placeholder tables to prevent column/FK conflicts
DROP TABLE IF EXISTS career_points_ledger;
DROP TABLE IF EXISTS evaluations;
DROP TABLE IF EXISTS career_submissions;
DROP TABLE IF EXISTS career_tasks;
DROP TABLE IF EXISTS career_levels;

-- 1. career_levels
CREATE TABLE career_levels (
    id              VARCHAR(36)  NOT NULL,
    level_number    INT          NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    points_required INT          NOT NULL,
    order_index     INT          NOT NULL,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_career_levels_level_number UNIQUE (level_number)
);

-- 2. career_tasks
CREATE TABLE career_tasks (
    id              VARCHAR(36)  NOT NULL,
    level_id        VARCHAR(36)  NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    instructions    TEXT,
    points_value    INT          NOT NULL,
    submission_type VARCHAR(20)  NOT NULL, -- LINK, IMAGE, PDF, FILE, TEXT
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_by      VARCHAR(36)  NOT NULL,
    created_at      DATETIME(6)  NOT NULL,
    updated_at      DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_career_tasks_level FOREIGN KEY (level_id) REFERENCES career_levels (id) ON DELETE CASCADE,
    CONSTRAINT fk_career_tasks_user  FOREIGN KEY (created_by) REFERENCES users (user_id)
);

-- 3. career_submissions
CREATE TABLE career_submissions (
    id               VARCHAR(36)  NOT NULL,
    task_id          VARCHAR(36)  NOT NULL,
    student_id       VARCHAR(36)  NOT NULL,
    submission_type  VARCHAR(20)  NOT NULL,
    submission_url   VARCHAR(1000),
    file_path        VARCHAR(1000),
    submission_text  TEXT,
    status           VARCHAR(30)  NOT NULL, -- PENDING, APPROVED, REJECTED, REVISION_REQUESTED
    points_awarded   INT,
    reviewer_id      VARCHAR(36),
    reviewer_comment TEXT,
    submitted_at     DATETIME(6)  NOT NULL,
    reviewed_at      DATETIME(6),
    resubmission_of  VARCHAR(36),
    PRIMARY KEY (id),
    CONSTRAINT fk_career_submissions_task    FOREIGN KEY (task_id)    REFERENCES career_tasks (id) ON DELETE CASCADE,
    CONSTRAINT fk_career_submissions_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_career_submissions_reviewer FOREIGN KEY (reviewer_id) REFERENCES users (user_id),
    CONSTRAINT fk_career_submissions_parent   FOREIGN KEY (resubmission_of) REFERENCES career_submissions (id) ON DELETE SET NULL
);

-- 4. student_career_progress
CREATE TABLE student_career_progress (
    student_id            VARCHAR(36) NOT NULL,
    current_level_id      VARCHAR(36) NOT NULL,
    total_points_at_level INT         NOT NULL DEFAULT 0,
    level_achieved_at     DATETIME(6) NOT NULL,
    PRIMARY KEY (student_id),
    CONSTRAINT fk_career_progress_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_career_progress_level   FOREIGN KEY (current_level_id) REFERENCES career_levels (id)
);

-- 5. career_level_overrides
CREATE TABLE career_level_overrides (
    id            VARCHAR(36)  NOT NULL,
    student_id    VARCHAR(36)  NOT NULL,
    level_id      VARCHAR(36)  NOT NULL,
    overridden_by VARCHAR(36)  NOT NULL,
    reason        TEXT         NOT NULL,
    created_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_career_override_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_career_override_level   FOREIGN KEY (level_id) REFERENCES career_levels (id),
    CONSTRAINT fk_career_override_creator FOREIGN KEY (overridden_by) REFERENCES users (user_id)
);
