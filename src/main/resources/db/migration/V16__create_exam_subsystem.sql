-- Drop existing basic placeholder tables to prevent conflicts
DROP TABLE IF EXISTS exam_question;
DROP TABLE IF EXISTS exam_attempts;
DROP TABLE IF EXISTS question_correct_answers;
DROP TABLE IF EXISTS question_options;
DROP TABLE IF EXISTS question_bank;
DROP TABLE IF EXISTS exams;

-- Create 'questions' table
CREATE TABLE questions (
    id             VARCHAR(36)  NOT NULL,
    course_id      VARCHAR(36)  NOT NULL,
    question_text  TEXT         NOT NULL,
    question_type  VARCHAR(20)  NOT NULL,
    difficulty     VARCHAR(10)  NOT NULL,
    default_marks  INT          NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    created_by     VARCHAR(36)  NOT NULL,
    created_at     DATETIME(6)  NOT NULL,
    updated_at     DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_questions_course FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE,
    CONSTRAINT fk_questions_creator FOREIGN KEY (created_by) REFERENCES users (user_id)
);

-- Create 'question_options' table
CREATE TABLE question_options (
    id          VARCHAR(36)  NOT NULL,
    question_id VARCHAR(36)  NOT NULL,
    option_text TEXT         NOT NULL,
    is_correct  BOOLEAN      NOT NULL DEFAULT FALSE,
    order_index INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_options_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
);

-- Create 'exams' table
CREATE TABLE exams (
    id                VARCHAR(36)  NOT NULL,
    title             VARCHAR(255) NOT NULL,
    description       TEXT,
    course_id         VARCHAR(36),
    created_by        VARCHAR(36)  NOT NULL,
    start_time        DATETIME(6)  NOT NULL,
    end_time          DATETIME(6)  NOT NULL,
    duration_minutes  INT          NOT NULL,
    shuffle_questions BOOLEAN      NOT NULL DEFAULT FALSE,
    shuffle_options   BOOLEAN      NOT NULL DEFAULT FALSE,
    attempts_allowed  INT          NOT NULL DEFAULT 1,
    status            VARCHAR(20)  NOT NULL,
    created_at        DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_exams_course FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE SET NULL,
    CONSTRAINT fk_exams_creator FOREIGN KEY (created_by) REFERENCES users (user_id)
);

-- Create 'exam_questions' table
CREATE TABLE exam_questions (
    exam_id        VARCHAR(36) NOT NULL,
    question_id    VARCHAR(36) NOT NULL,
    marks_override INT,
    order_index    INT         NOT NULL,
    PRIMARY KEY (exam_id, question_id),
    CONSTRAINT fk_eq_exam     FOREIGN KEY (exam_id)     REFERENCES exams (id) ON DELETE CASCADE,
    CONSTRAINT fk_eq_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
);

-- Create 'exam_audiences' table
CREATE TABLE exam_audiences (
    id          VARCHAR(36) NOT NULL,
    exam_id     VARCHAR(36) NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    target_id   VARCHAR(36) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_audience_exam FOREIGN KEY (exam_id) REFERENCES exams (id) ON DELETE CASCADE
);

-- Create 'exam_attempts' table
CREATE TABLE exam_attempts (
    id             VARCHAR(36) NOT NULL,
    exam_id        VARCHAR(36) NOT NULL,
    student_id     VARCHAR(36) NOT NULL,
    started_at     DATETIME(6) NOT NULL,
    submitted_at   DATETIME(6),
    status         VARCHAR(20) NOT NULL,
    score          DOUBLE,
    question_order TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_attempts_exam    FOREIGN KEY (exam_id)    REFERENCES exams (id) ON DELETE CASCADE,
    CONSTRAINT fk_attempts_student FOREIGN KEY (student_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- Create 'exam_answers' table
CREATE TABLE exam_answers (
    id                  VARCHAR(36) NOT NULL,
    attempt_id          VARCHAR(36) NOT NULL,
    question_id         VARCHAR(36) NOT NULL,
    selected_option_ids TEXT,
    is_correct          BOOLEAN,
    marks_awarded       DOUBLE,
    PRIMARY KEY (id),
    CONSTRAINT fk_answers_attempt  FOREIGN KEY (attempt_id)  REFERENCES exam_attempts (id) ON DELETE CASCADE,
    CONSTRAINT fk_answers_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
);
