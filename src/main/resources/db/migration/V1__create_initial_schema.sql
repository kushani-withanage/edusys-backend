-- =====================================================================
-- EduSys initial schema
-- =====================================================================

-- ---------------------------------------------------------------------
-- Users & role sub-types
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id    VARCHAR(36)  NOT NULL,
    full_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    phone      VARCHAR(255),
    password   VARCHAR(255) NOT NULL,
    status     VARCHAR(255),
    created_at DATETIME(6),
    PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE students (
    student_id      VARCHAR(36) NOT NULL,
    address         VARCHAR(255),
    reg_no          VARCHAR(255),
    enrollment_date DATE,
    dob             DATE,
    PRIMARY KEY (student_id),
    CONSTRAINT uq_students_reg_no UNIQUE (reg_no),
    CONSTRAINT fk_students_user FOREIGN KEY (student_id) REFERENCES users (user_id)
);

CREATE TABLE teachers (
    teacher_id     VARCHAR(36) NOT NULL,
    specialization VARCHAR(255),
    join_date      DATE,
    PRIMARY KEY (teacher_id),
    CONSTRAINT fk_teachers_user FOREIGN KEY (teacher_id) REFERENCES users (user_id)
);

CREATE TABLE admins (
    admin_id   VARCHAR(36) NOT NULL,
    department VARCHAR(255),
    PRIMARY KEY (admin_id),
    CONSTRAINT fk_admins_user FOREIGN KEY (admin_id) REFERENCES users (user_id)
);

CREATE TABLE reviewers (
    reviewer_id    VARCHAR(36) NOT NULL,
    expertise_area VARCHAR(255),
    PRIMARY KEY (reviewer_id),
    CONSTRAINT fk_reviewers_user FOREIGN KEY (reviewer_id) REFERENCES users (user_id)
);

CREATE TABLE parents (
    parent_id  VARCHAR(36) NOT NULL,
    occupation VARCHAR(255),
    PRIMARY KEY (parent_id),
    CONSTRAINT fk_parents_user FOREIGN KEY (parent_id) REFERENCES users (user_id)
);

-- ---------------------------------------------------------------------
-- Academic structure
-- ---------------------------------------------------------------------
CREATE TABLE courses (
    course_id      VARCHAR(36)  NOT NULL,
    course_name    VARCHAR(255) NOT NULL,
    credits        INT,
    duration_weeks INT,
    description    TEXT,
    PRIMARY KEY (course_id)
);

CREATE TABLE batches (
    batch_id   VARCHAR(36)  NOT NULL,
    batch_name VARCHAR(255) NOT NULL,
    start_date DATE,
    end_date   DATE,
    PRIMARY KEY (batch_id)
);

CREATE TABLE batch_course (
    batch_id  VARCHAR(36) NOT NULL,
    course_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (batch_id, course_id),
    CONSTRAINT fk_batch_course_batch  FOREIGN KEY (batch_id)  REFERENCES batches (batch_id),
    CONSTRAINT fk_batch_course_course FOREIGN KEY (course_id) REFERENCES courses (course_id)
);

CREATE TABLE semesters (
    semester_id   VARCHAR(36) NOT NULL,
    semester_name VARCHAR(255),
    start_date    DATE,
    end_date      DATE,
    batch_id      VARCHAR(36) NOT NULL,
    PRIMARY KEY (semester_id),
    CONSTRAINT fk_semesters_batch FOREIGN KEY (batch_id) REFERENCES batches (batch_id)
);

CREATE TABLE academic_calendars (
    calendar_id VARCHAR(36)  NOT NULL,
    event_name  VARCHAR(255) NOT NULL,
    event_date  DATE,
    description TEXT,
    status      VARCHAR(255),
    batch_id    VARCHAR(36),
    PRIMARY KEY (calendar_id),
    CONSTRAINT fk_academic_calendars_batch FOREIGN KEY (batch_id) REFERENCES batches (batch_id)
);

-- ---------------------------------------------------------------------
-- Enrollment, fees & receipts
-- ---------------------------------------------------------------------
CREATE TABLE enrollments (
    enrollment_id VARCHAR(36) NOT NULL,
    student_id    VARCHAR(36) NOT NULL,
    batch_id      VARCHAR(36) NOT NULL,
    course_id     VARCHAR(36) NOT NULL,
    enroll_date   DATE,
    PRIMARY KEY (enrollment_id),
    CONSTRAINT fk_enrollments_student FOREIGN KEY (student_id) REFERENCES students (student_id),
    CONSTRAINT fk_enrollments_batch   FOREIGN KEY (batch_id)   REFERENCES batches (batch_id),
    CONSTRAINT fk_enrollments_course  FOREIGN KEY (course_id)  REFERENCES courses (course_id)
);

CREATE TABLE fee_records (
    fee_id     VARCHAR(36)    NOT NULL,
    student_id VARCHAR(36)    NOT NULL,
    amount     DECIMAL(10, 2) NOT NULL,
    due_date   DATE,
    fee_type   VARCHAR(255),
    status     VARCHAR(255),
    PRIMARY KEY (fee_id),
    CONSTRAINT fk_fee_records_student FOREIGN KEY (student_id) REFERENCES students (student_id)
);

CREATE TABLE receipts (
    receipt_id     VARCHAR(36)  NOT NULL,
    receipt_no     VARCHAR(255) NOT NULL,
    fee_id         VARCHAR(36)  NOT NULL,
    payment_date   DATE,
    amount_paid    DECIMAL(10, 2),
    payment_method VARCHAR(255),
    PRIMARY KEY (receipt_id),
    CONSTRAINT uq_receipts_receipt_no UNIQUE (receipt_no),
    CONSTRAINT fk_receipts_fee FOREIGN KEY (fee_id) REFERENCES fee_records (fee_id)
);

-- ---------------------------------------------------------------------
-- Question bank & exams
-- ---------------------------------------------------------------------
CREATE TABLE question_bank (
    question_id   VARCHAR(36) NOT NULL,
    question_type VARCHAR(255),
    question_text TEXT        NOT NULL,
    marks         INT,
    created_by    VARCHAR(36),
    PRIMARY KEY (question_id),
    CONSTRAINT fk_question_bank_teacher FOREIGN KEY (created_by) REFERENCES teachers (teacher_id)
);

CREATE TABLE question_options (
    question_id  VARCHAR(36) NOT NULL,
    option_value VARCHAR(255),
    CONSTRAINT fk_question_options_question FOREIGN KEY (question_id) REFERENCES question_bank (question_id)
);

CREATE TABLE question_correct_answers (
    question_id    VARCHAR(36) NOT NULL,
    correct_answer VARCHAR(255),
    CONSTRAINT fk_question_correct_answers_question FOREIGN KEY (question_id) REFERENCES question_bank (question_id)
);

CREATE TABLE exams (
    exam_id          VARCHAR(36)  NOT NULL,
    title            VARCHAR(255) NOT NULL,
    start_time       DATETIME(6),
    duration_minutes INT,
    total_marks      INT,
    created_by       VARCHAR(36),
    PRIMARY KEY (exam_id),
    CONSTRAINT fk_exams_teacher FOREIGN KEY (created_by) REFERENCES teachers (teacher_id)
);

CREATE TABLE exam_question (
    exam_id     VARCHAR(36) NOT NULL,
    question_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (exam_id, question_id),
    CONSTRAINT fk_exam_question_exam     FOREIGN KEY (exam_id)     REFERENCES exams (exam_id),
    CONSTRAINT fk_exam_question_question FOREIGN KEY (question_id) REFERENCES question_bank (question_id)
);

CREATE TABLE exam_attempts (
    attempt_id  VARCHAR(36) NOT NULL,
    exam_id     VARCHAR(36) NOT NULL,
    student_id  VARCHAR(36) NOT NULL,
    start_time  DATETIME(6),
    submit_time DATETIME(6),
    status      VARCHAR(255),
    score       DOUBLE,
    PRIMARY KEY (attempt_id),
    CONSTRAINT fk_exam_attempts_exam    FOREIGN KEY (exam_id)    REFERENCES exams (exam_id),
    CONSTRAINT fk_exam_attempts_student FOREIGN KEY (student_id) REFERENCES students (student_id)
);

-- ---------------------------------------------------------------------
-- Assignments, submissions & grades
-- ---------------------------------------------------------------------
CREATE TABLE assignments (
    assignment_id VARCHAR(36)  NOT NULL,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    due_date      DATE,
    created_by    VARCHAR(36),
    PRIMARY KEY (assignment_id),
    CONSTRAINT fk_assignments_teacher FOREIGN KEY (created_by) REFERENCES teachers (teacher_id)
);

CREATE TABLE assignment_submissions (
    submission_id  VARCHAR(36) NOT NULL,
    assignment_id  VARCHAR(36) NOT NULL,
    student_id     VARCHAR(36) NOT NULL,
    submit_date    DATETIME(6),
    submitted_file VARCHAR(255),
    marks          DOUBLE,
    graded_by      VARCHAR(36),
    feedback       TEXT,
    PRIMARY KEY (submission_id),
    CONSTRAINT fk_assignment_submissions_assignment FOREIGN KEY (assignment_id) REFERENCES assignments (assignment_id),
    CONSTRAINT fk_assignment_submissions_student    FOREIGN KEY (student_id)    REFERENCES students (student_id),
    CONSTRAINT fk_assignment_submissions_teacher    FOREIGN KEY (graded_by)     REFERENCES teachers (teacher_id)
);

CREATE TABLE grades (
    grade_id       VARCHAR(36) NOT NULL,
    student_id     VARCHAR(36) NOT NULL,
    course_id      VARCHAR(36),
    submission_id  VARCHAR(36),
    grade_value    VARCHAR(255),
    published_date DATE,
    PRIMARY KEY (grade_id),
    CONSTRAINT uq_grades_submission UNIQUE (submission_id),
    CONSTRAINT fk_grades_student    FOREIGN KEY (student_id)    REFERENCES students (student_id),
    CONSTRAINT fk_grades_course     FOREIGN KEY (course_id)     REFERENCES courses (course_id),
    CONSTRAINT fk_grades_submission FOREIGN KEY (submission_id) REFERENCES assignment_submissions (submission_id)
);

-- ---------------------------------------------------------------------
-- Parents & inquiries
-- ---------------------------------------------------------------------
CREATE TABLE parent_student_links (
    link_id           VARCHAR(36) NOT NULL,
    parent_id         VARCHAR(36) NOT NULL,
    student_id        VARCHAR(36) NOT NULL,
    relationship_type VARCHAR(255),
    linked_date       DATE,
    PRIMARY KEY (link_id),
    CONSTRAINT fk_parent_student_links_parent  FOREIGN KEY (parent_id)  REFERENCES parents (parent_id),
    CONSTRAINT fk_parent_student_links_student FOREIGN KEY (student_id) REFERENCES students (student_id)
);

CREATE TABLE inquiries (
    inquiry_id     VARCHAR(36)  NOT NULL,
    applicant_name VARCHAR(255) NOT NULL,
    contact_info   VARCHAR(255),
    status         VARCHAR(255),
    inquiry_date   DATE,
    batch_id       VARCHAR(36),
    PRIMARY KEY (inquiry_id),
    CONSTRAINT fk_inquiries_batch FOREIGN KEY (batch_id) REFERENCES batches (batch_id)
);

-- ---------------------------------------------------------------------
-- Career module
-- ---------------------------------------------------------------------
CREATE TABLE career_tasks (
    task_id         VARCHAR(36)  NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    due_date        DATE,
    rubric_criteria TEXT,
    point_value     INT,
    PRIMARY KEY (task_id)
);

CREATE TABLE career_submissions (
    submission_id  VARCHAR(36) NOT NULL,
    task_id        VARCHAR(36) NOT NULL,
    student_id     VARCHAR(36) NOT NULL,
    status         VARCHAR(255),
    submitted_file VARCHAR(255),
    submit_date    DATETIME(6),
    PRIMARY KEY (submission_id),
    CONSTRAINT fk_career_submissions_task    FOREIGN KEY (task_id)    REFERENCES career_tasks (task_id),
    CONSTRAINT fk_career_submissions_student FOREIGN KEY (student_id) REFERENCES students (student_id)
);

CREATE TABLE evaluations (
    evaluation_id   VARCHAR(36) NOT NULL,
    submission_id   VARCHAR(36) NOT NULL,
    reviewer_id     VARCHAR(36),
    comments        TEXT,
    override_reason TEXT,
    evaluation_date DATE,
    override_flag   BIT,
    PRIMARY KEY (evaluation_id),
    CONSTRAINT uq_evaluations_submission UNIQUE (submission_id),
    CONSTRAINT fk_evaluations_submission FOREIGN KEY (submission_id) REFERENCES career_submissions (submission_id),
    CONSTRAINT fk_evaluations_reviewer   FOREIGN KEY (reviewer_id)   REFERENCES reviewers (reviewer_id)
);

CREATE TABLE career_levels (
    level_id    VARCHAR(36)  NOT NULL,
    level_name  VARCHAR(255) NOT NULL,
    description TEXT,
    min_points  INT,
    max_points  INT,
    PRIMARY KEY (level_id),
    CONSTRAINT uq_career_levels_level_name UNIQUE (level_name)
);

CREATE TABLE career_points_ledger (
    ledger_id      VARCHAR(36) NOT NULL,
    student_id     VARCHAR(36) NOT NULL,
    task_id        VARCHAR(36) NOT NULL,
    points_awarded INT,
    entry_date     DATE,
    remarks        VARCHAR(255),
    PRIMARY KEY (ledger_id),
    CONSTRAINT fk_career_points_ledger_student FOREIGN KEY (student_id) REFERENCES students (student_id),
    CONSTRAINT fk_career_points_ledger_task    FOREIGN KEY (task_id)    REFERENCES career_tasks (task_id)
);
