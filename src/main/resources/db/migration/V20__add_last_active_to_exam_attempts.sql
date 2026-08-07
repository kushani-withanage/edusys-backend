-- Add last_active_at column to exam_attempts
ALTER TABLE exam_attempts ADD COLUMN last_active_at DATETIME(6) NULL;

-- Add unique constraint to exam_answers to allow upsert by (attempt_id, question_id)
ALTER TABLE exam_answers ADD CONSTRAINT uq_answers_attempt_question UNIQUE (attempt_id, question_id);
