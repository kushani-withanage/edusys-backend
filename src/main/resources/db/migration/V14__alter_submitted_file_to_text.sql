-- Alter submitted_file column in assignment_submissions to TEXT to support JSON arrays of multiple files
ALTER TABLE assignment_submissions MODIFY COLUMN submitted_file TEXT;
