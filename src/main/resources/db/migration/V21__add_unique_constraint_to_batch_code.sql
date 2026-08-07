-- Rename any duplicate batch names to prevent migration failures
UPDATE batches b1
JOIN (
    SELECT batch_id, ROW_NUMBER() OVER (PARTITION BY LOWER(batch_name) ORDER BY batch_id) as rn
    FROM batches
) b2 ON b1.batch_id = b2.batch_id
SET b1.batch_name = CONCAT(b1.batch_name, '_DUP_', SUBSTRING(b1.batch_id, -4))
WHERE b2.rn > 1;

-- Add unique constraint on batch_name
ALTER TABLE batches ADD UNIQUE (batch_name);
