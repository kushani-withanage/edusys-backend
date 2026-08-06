-- ---------------------------------------------------------------------
-- Expand courses table to support custom course templates
-- ---------------------------------------------------------------------
ALTER TABLE courses ADD COLUMN batch_code VARCHAR(255);
ALTER TABLE courses ADD COLUMN level VARCHAR(255);
ALTER TABLE courses ADD COLUMN is_compulsory BOOLEAN;
ALTER TABLE courses ADD COLUMN cert_reqs TEXT;
ALTER TABLE courses ADD COLUMN qualify_intro TEXT;
ALTER TABLE courses ADD COLUMN qualify_reqs TEXT;
ALTER TABLE courses ADD COLUMN sections TEXT;
