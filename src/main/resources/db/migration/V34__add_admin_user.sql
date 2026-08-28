-- =====================================================================
-- EduSys Flyway Migration: Add default system administrator account
-- =====================================================================

INSERT INTO users (user_id, full_name, email, role, phone, password, status, created_at) VALUES 
('usr0000', 'System Administrator', 'admin@gmail.com', 'ADMIN', '+94770000000', '$2a$10$UF.e4MBltgY37.sQ9tdx1..InnNrX1379GFBTv04FVmURii86WM5i', 'ACTIVE', NOW());

INSERT INTO admins (admin_id, department) VALUES 
('usr0000', 'Management');
