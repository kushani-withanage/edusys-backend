ALTER TABLE enrollments ADD COLUMN status VARCHAR(50) DEFAULT 'ongoing';
ALTER TABLE course_access_grants ADD COLUMN status VARCHAR(50) DEFAULT 'ongoing';
