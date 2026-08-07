-- Recreate missing tables to satisfy JPA entities and prevent validation errors
CREATE TABLE IF NOT EXISTS career_points_ledger (
    ledger_id      VARCHAR(36) NOT NULL,
    student_id     VARCHAR(36) NOT NULL,
    task_id        VARCHAR(36) NOT NULL,
    points_awarded INT,
    entry_date     DATE,
    remarks        VARCHAR(255),
    PRIMARY KEY (ledger_id),
    CONSTRAINT fk_career_points_ledger_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_career_points_ledger_task    FOREIGN KEY (task_id)    REFERENCES career_tasks (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS evaluations (
    evaluation_id   VARCHAR(36) NOT NULL,
    submission_id   VARCHAR(36) NOT NULL,
    reviewer_id     VARCHAR(36),
    comments        TEXT,
    override_reason TEXT,
    evaluation_date DATE,
    override_flag   BIT,
    PRIMARY KEY (evaluation_id),
    CONSTRAINT uq_evaluations_submission UNIQUE (submission_id),
    CONSTRAINT fk_evaluations_submission FOREIGN KEY (submission_id) REFERENCES career_submissions (id) ON DELETE CASCADE,
    CONSTRAINT fk_evaluations_reviewer   FOREIGN KEY (reviewer_id)   REFERENCES users (user_id) ON DELETE SET NULL
);
