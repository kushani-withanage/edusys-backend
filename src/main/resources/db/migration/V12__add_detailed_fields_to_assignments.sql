-- ---------------------------------------------------------------------
-- Expand assignments table to support detailed Moodle-like settings
-- ---------------------------------------------------------------------
ALTER TABLE assignments MODIFY COLUMN due_date DATETIME(6);
ALTER TABLE assignments ADD COLUMN display_description BOOLEAN DEFAULT FALSE;
ALTER TABLE assignments ADD COLUMN activity_instructions TEXT;
ALTER TABLE assignments ADD COLUMN additional_file_name VARCHAR(255);
ALTER TABLE assignments ADD COLUMN additional_file_url VARCHAR(500);
ALTER TABLE assignments ADD COLUMN only_show_files BOOLEAN DEFAULT FALSE;
ALTER TABLE assignments ADD COLUMN allow_submissions_from DATETIME(6);
ALTER TABLE assignments ADD COLUMN cut_off_date DATETIME(6);
ALTER TABLE assignments ADD COLUMN remind_grade_by DATETIME(6);
ALTER TABLE assignments ADD COLUMN always_show_description BOOLEAN DEFAULT FALSE;
ALTER TABLE assignments ADD COLUMN submission_type_online_text BOOLEAN DEFAULT FALSE;
ALTER TABLE assignments ADD COLUMN submission_type_file BOOLEAN DEFAULT TRUE;
ALTER TABLE assignments ADD COLUMN max_files INT DEFAULT 1;
ALTER TABLE assignments ADD COLUMN max_size VARCHAR(50) DEFAULT '250MB';
