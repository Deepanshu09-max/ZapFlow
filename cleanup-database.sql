-- Clear all data from tables in correct order (respecting foreign keys)
DELETE FROM "ZapRunOutbox";
DELETE FROM "ZapRun";
DELETE FROM "Action";
DELETE FROM "Trigger";
DELETE FROM "Zap";
DELETE FROM "User";

-- Reset sequences (if any)
-- ALTER SEQUENCE user_id_seq RESTART WITH 1;

-- Verify cleanup
SELECT 'Users' as table_name, COUNT(*) as count FROM "User"
UNION ALL
SELECT 'Zaps', COUNT(*) FROM "Zap"
UNION ALL
SELECT 'ZapRuns', COUNT(*) FROM "ZapRun"
UNION ALL
SELECT 'ZapRunOutbox', COUNT(*) FROM "ZapRunOutbox";
