-- ---------------------------------------------------------------------
-- Change sections column type to LONGTEXT in courses table
-- ---------------------------------------------------------------------
ALTER TABLE courses MODIFY COLUMN sections LONGTEXT;
