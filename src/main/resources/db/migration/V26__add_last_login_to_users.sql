-- Add last_login column to users table
ALTER TABLE users ADD COLUMN last_login DATETIME(6) NULL;
