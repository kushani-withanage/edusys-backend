-- ---------------------------------------------------------------------
-- Drop foreign key constraint on assignment_id in assignment_submissions table
-- ---------------------------------------------------------------------
ALTER TABLE assignment_submissions DROP FOREIGN KEY fk_assignment_submissions_assignment;
