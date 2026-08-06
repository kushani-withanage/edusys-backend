-- Alter additional_file_url column to TEXT to support JSON arrays of multiple files
ALTER TABLE assignments MODIFY COLUMN additional_file_url TEXT;
