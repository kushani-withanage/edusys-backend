-- ---------------------------------------------------------------------
-- Course Access Control customization grants
-- ---------------------------------------------------------------------
CREATE TABLE course_access_grants (
    id              VARCHAR(36)  NOT NULL,
    course_id       VARCHAR(36)  NOT NULL,
    course_name     VARCHAR(255) NOT NULL,
    batch_code      VARCHAR(255) NOT NULL,
    user_identifier VARCHAR(255) NOT NULL,
    granted_at      DATE,
    PRIMARY KEY (id),
    CONSTRAINT fk_course_access_grants_course FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE
);
