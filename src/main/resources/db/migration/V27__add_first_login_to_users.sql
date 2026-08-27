-- Add first_login column to users table
ALTER TABLE users ADD COLUMN first_login DATETIME(6) NULL;

-- Populate first_login for existing users who already have last_login
UPDATE users SET first_login = last_login WHERE last_login IS NOT NULL;
