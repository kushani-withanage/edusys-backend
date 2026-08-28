CREATE TABLE exam_courses (
    course_id VARCHAR(36) PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL
);

INSERT INTO exam_courses (course_id, course_name) VALUES ('EXC0001', 'PRF');
INSERT INTO exam_courses (course_id, course_name) VALUES ('EXC0002', 'OOP');
INSERT INTO exam_courses (course_id, course_name) VALUES ('EXC0003', 'Internet Technologies');
